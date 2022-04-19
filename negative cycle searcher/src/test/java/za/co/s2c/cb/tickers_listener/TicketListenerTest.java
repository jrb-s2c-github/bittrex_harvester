package za.co.s2c.cb.tickers_listener;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import za.co.s2c.cb.model.bittrex.Ticker;
import za.co.s2c.cb.model.bittrex.TickersResult;
import za.co.s2c.cb.model.linqua_franca.Asset;
import za.co.s2c.cb.model.linqua_franca.AssetLink;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketListenerTest {
    public static final String CYCLUB = "CYCLUB";
    public static final String USDT = "USDT";
    public static final String ASSET1_ASSET2 = CYCLUB + "-" + USDT;
    public static final double LAST_TRADE_RATE = 0.09535;
    public static final double ASK_RATE = 0.09589;
    public static final double BID_RATE = 0.09412;
    static TickersResult testData;

    @BeforeAll
    private static void buildTickersResult() {
        testData = new TickersResult();
        testData.setSequence(1);
        HashSet<Ticker> deltas = new HashSet<>(3);
        testData.setDeltas(deltas);

        Ticker ticker = new Ticker();
        ticker.setSymbol(ASSET1_ASSET2);
        ticker.setLastTradeRate(LAST_TRADE_RATE);
        ticker.setAskRate(ASK_RATE);
        ticker.setBidRate(BID_RATE);
        deltas.add(ticker);
    }

    @Test
    public void testTickersResultToModel() throws Exception {
//        TickersListener tickersListener = new TickersListener(true); TODO uncomment
//        HashMap<String, Asset> assets = new HashMap<String, Asset>();
//        tickersListener.tickersResultToModel(testData, assets);
//        Asset cyclub = assets.get(CYCLUB);
//        assertEquals(CYCLUB, cyclub.getCode());
//        assertEquals(1, cyclub.getNeighbours().size());
//        assertEquals(1, cyclub.getTransactionAmounts());
//
//        Asset usdt = assets.get(USDT);
//        assertEquals(USDT, usdt.getCode());
//        assertEquals(1, usdt.getNeighbours().size());
//        assertEquals(1, usdt.getTransactionAmounts());
//
//        AssetLink assetLinkFromCyclub = cyclub.getNeighbours().get(usdt);
//        assertEquals(ASSET1_ASSET2, assetLinkFromCyclub.getAsset1Asset2());
//        assertEquals(LAST_TRADE_RATE, assetLinkFromCyclub.getLastTradeRate());
//        assertEquals(BID_RATE, assetLinkFromCyclub.getBidRate());
//        assertEquals(ASK_RATE, assetLinkFromCyclub.getAskRate());
//        assertEquals(cyclub, assetLinkFromCyclub.getFrom());
//        assertEquals(usdt, assetLinkFromCyclub.getTo());
//
//        AssetLink assetLinkFromUsdt = usdt.getNeighbours().get(cyclub);
//        assertEquals(ASSET1_ASSET2, assetLinkFromUsdt.getAsset1Asset2());
//        assertEquals(1 / LAST_TRADE_RATE, assetLinkFromUsdt.getLastTradeRate());
//        assertEquals(1 / BID_RATE, assetLinkFromUsdt.getBidRate());
//        assertEquals(1 / ASK_RATE, assetLinkFromUsdt.getAskRate());
//        assertEquals(usdt, assetLinkFromUsdt.getFrom());
//        assertEquals(cyclub, assetLinkFromUsdt.getTo());
    }
}
