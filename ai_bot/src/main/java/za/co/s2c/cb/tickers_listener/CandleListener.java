package za.co.s2c.cb.tickers_listener;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.hazelcast.collection.IList;
import com.hazelcast.collection.ISet;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.s2c.cb.model.bittrex.Candle;
import za.co.s2c.cb.model.linqua_franca.CandleRepository;

import java.util.Queue;
import java.util.UUID;

// TODO: 2022/03/17 configure logging levels
@Slf4j
@Component
class CandleListener {

//    public static final String EUR = "EUR";
//    public static final String USD = "USD";
//    public static final String USDT = "USDT";
//    public static final String ETH = "ETH";
//    public static final String BTC = "BTC";
    public static final int NO_ACTION = 0;
    public static final int BUY_ACTION = 1;
    public static final int SHORT_ACTION = 2;

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Autowired
    private CandleRepository candleRepository;

    private Queue<String> streamedFromBittrex;
    private IMap<String, ISet<za.co.s2c.cb.model.linqua_franca.Candle>> candlesAvailableForTrading;

    public void init() {
        streamedFromBittrex = hazelcastInstance.getQueue("candles_stream_from_bittrex");
//        candlesAvailableForTrading = hazelcastInstance.getMap("candles_available_for_trading");

        while (true) {
            String raw = streamedFromBittrex.poll();
            if (raw != null) {
                Candle bittrexCandle = rawToTickersResult(raw, streamedFromBittrex);

                log.trace("new result being processed: " + bittrexCandle);

                za.co.s2c.cb.model.linqua_franca.Candle candle = za.co.s2c.cb.model.linqua_franca.Candle.builder().
                        id(UUID.randomUUID()).
                        close(bittrexCandle.getDelta().getClose()).
                        decision(NO_ACTION).
                        high(bittrexCandle.getDelta().getHigh()).
                        interval(bittrexCandle.getInterval()).
                        low(bittrexCandle.getDelta().getLow()).
                        marketSymbol(bittrexCandle.getMarketSymbol()).
                        open(bittrexCandle.getDelta().getOpen()).
                        quoteVolume(bittrexCandle.getDelta().getQuoteVolume()).
                        sequence(bittrexCandle.getSequence()).
                        volume(bittrexCandle.getDelta().getVolume()).
                        startsAt(bittrexCandle.getDelta().getStartsAt()).
                        build();


                candleRepository.save(candle);
//                ISet<za.co.s2c.cb.model.linqua_franca.Candle> candles = candlesAvailableForTrading.get(candle.getMarketSymbol());
//                if (candles == null) {
                ISet<za.co.s2c.cb.model.linqua_franca.Candle> candles = hazelcastInstance.getSet(candle.getMarketSymbol());
//                }

                // check whether a decision to trade on past candles are triggered by the latest candle
                double profitMargin = 0.01;
                for (za.co.s2c.cb.model.linqua_franca.Candle candidateTrade : candles) {
                    double buyProfit = (candle.getHigh() - candidateTrade.getClose()) / candidateTrade.getClose();
                    if (buyProfit > profitMargin) {
                        candles.remove(candidateTrade);
                        candleRepository.delete(candidateTrade);
                        candleRepository.save(candidateTrade.replicateWithDifferentDecision(BUY_ACTION, candle.getId(), candle.getStartsAt()));
                    }

                    double sellProfit = (candle.getLow() - candidateTrade.getClose()) / candidateTrade.getClose();
                    if (sellProfit < -profitMargin) {
                        candles.remove(candidateTrade);
                        candleRepository.delete(candidateTrade);
                        candleRepository.save(candidateTrade.replicateWithDifferentDecision(SHORT_ACTION, candle.getId(), candle.getStartsAt()));
                    }
                }

                // lastly add the new candle
                candles.add(candle);

                log.trace("new result has been processed: " + candle);
            }
        }
    }

    private Candle rawToTickersResult(String raw, Queue<String> streamedFromBittrex) {
        int tickersAmount = streamedFromBittrex.size();
        log.trace("Tickers in queue: " + tickersAmount);
        if (tickersAmount > 10) {
            log.warn("There are more than 10 tickers in the queue! " + tickersAmount);
        }

        JsonObject msg = null;
        try {
            msg = DataConverter.decodeMessage(raw);
        } catch (Exception e) {
            log.error("Could not read compressed message!", e);
            e.printStackTrace();
        }
        var gson = new GsonBuilder().create();

        Candle candleResult = gson.fromJson(msg, Candle.class);
        return candleResult;
    }

}

