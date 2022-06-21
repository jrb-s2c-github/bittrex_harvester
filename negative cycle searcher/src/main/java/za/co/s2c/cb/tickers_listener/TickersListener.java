package za.co.s2c.cb.tickers_listener;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.s2c.cb.bittrex_client.BalancesService;
import za.co.s2c.cb.bittrex_client.MarketsService;
import za.co.s2c.cb.bittrex_client.OrdersService;
import za.co.s2c.cb.bittrex_model.Balance;
import za.co.s2c.cb.bittrex_model.Market;
import za.co.s2c.cb.bittrex_model.NewOrder;
import za.co.s2c.cb.bittrex_model.Order;
import za.co.s2c.cb.model.bittrex.Ticker;
import za.co.s2c.cb.model.bittrex.TickersResult;
import za.co.s2c.cb.model.linqua_franca.Asset;
import za.co.s2c.cb.model.linqua_franca.AssetLink;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Slf4j
@Component
class TickersListener /*implements ItemListener<java.lang.String>*/ {

    public static final String EUR = "EUR";
    public static final String USD = "USD";
    public static final String USDT = "USDT";
    public static final String ETH = "ETH";
    public static final String BTC = "BTC";

    @Autowired
    private HazelcastInstance hazelcastInstance;

    private Queue<String> streamedFromBittrex;
    private Map<String, Asset> assets;

    @Autowired
    OrdersService ordersService;

    @Autowired
    BalancesService balancesService;

    @Autowired
    MarketsService marketsService;
    private HashMap<String, Market> markets = new HashMap<>();
    private HashMap<String, Balance> balances = new HashMap<>();


    public void init() {

        int assetAmount = 0;
        streamedFromBittrex = hazelcastInstance.getQueue("tickers_stream_from_bittrex");
        assets = new HashMap<>(500);

        TradeSearcher ts = new TradeSearcher();

        getMarkets();

        getBalances();
        while (true) {


            String raw = streamedFromBittrex.poll();
            if (raw != null) {
                TickersResult tickersResult = rawToTickersResult(raw, streamedFromBittrex);
                tickersResultToModel(tickersResult, assets);
                log.trace("new TickersResult being processed: " + tickersResult);
                if (assets.size() != assetAmount) {
                    log.info("Known assets: " + assets.size());
                    assetAmount = assets.size();
                }

                int pointAmount = assets.size();
                String btc = "BTC";
                if (assets.get(btc) != null && streamedFromBittrex.size() < 3) {
                    log.warn("Searching ...");
                    calculateShortestPath(ts, pointAmount, btc);
                } else {
                    log.warn(streamedFromBittrex.size() + " behind -> not searching :( ");
                }
//                calculateShortestPath(ts, pointAmount, "ETH");
//                calculateShortestPath(ts, pointAmount, "ADA");
//                calculateShortestPath(ts, pointAmount, "XRP");
//                calculateShortestPath(ts, pointAmount, "LTC");
//                calculateShortestPath(ts, pointAmount, "USDC");
//                calculateShortestPath(ts, pointAmount, "USDT");
            }
        }
    }

    // TODO: 2022/03/18 only retrieve balances for the feeder assets 
    private void getBalances() {
        Set<Balance> balanceSet = balancesService.getBalances();
        for(Balance balance : balanceSet) {
            if (balance.getCurrencySymbol().equals(EUR))
                balances.put(EUR, balance);
            if (balance.getCurrencySymbol().equals(USD))
                balances.put(USD, balance);
//            if (balance.getCurrencySymbol().equals(BTC))
//                balances.put(BTC, balance);
            if (balance.getCurrencySymbol().equals(ETH))
                balances.put(ETH, balance);
            if (balance.getCurrencySymbol().equals(USDT))
                balances.put(USDT, balance);
        }
    }

    private void getMarkets() {
        Set<Market> marketSet = marketsService.getMarkets();
        for (Market market : marketSet) {
            markets.put(market.getSymbol(), market);
        }
    }

    // TODO: 2022/03/17 remove guid as parameter
    private Order trade(String marketSymbol, String direction, double quantity, String guid, double price) {
        Market market = markets.get(marketSymbol);

        // TODO can be removed after removing variable fromIndex and toIndex
//        String priceString = BigDecimal.valueOf(price)
//                .setScale(market.getPrecision(), RoundingMode.HALF_UP)
//                .toString();

        NewOrder newOrder = new NewOrder();
        newOrder.setMarketSymbol(marketSymbol);
//        newOrder.setDirection("SELL");
        newOrder.setDirection(direction);
//        newOrder.setType("LIMIT");
        newOrder.setType("CEILING_LIMIT"); // TODO move to limit, only use this for testing
//        newOrder.setQuantity(quantity + "");
        newOrder.setCeiling(quantity + ""); // TODO move to quantity once not used CEILING anymore, only use this for testing
        newOrder.setLimit(price+"");
//        newOrder.setTimeInForce("FILL_OR_KILL");
        newOrder.setTimeInForce("IMMEDIATE_OR_CANCEL");
        newOrder.setUseAwards(Boolean.FALSE.toString());
//        newOrder.setClientOrderId(guid);
        log.warn("Order to be placed: " + newOrder);

        Order tradeResult = null;
        try {
            tradeResult = ordersService.newOrders(newOrder);
        } catch (Throwable e) {
            tradeResult = new Order();
            e.printStackTrace();
            tradeResult.setMarketSymbol(newOrder.getMarketSymbol());
            tradeResult.setDirection(newOrder.getDirection());
            tradeResult.setStatus("EXCEPTION!");

        } finally {
            log.warn("Traded: " + tradeResult.toString());
        }
        return tradeResult;
    }

    private void calculateShortestPath(TradeSearcher ts, int pointAmount, String cc) {
        HashMap<TradeSearcher.Point, Integer> reachable = new HashMap<>(pointAmount);
        HashMap<Asset, Double> distance = new HashMap<>(pointAmount);
        HashMap<TradeSearcher.Point, Integer> shortest = new HashMap<>(pointAmount);
        Asset from = assets.get(cc);
        ts.shortestPaths(from, distance, reachable, shortest, assets.values());

//        shortest.keySet().stream().filter(assetKey -> shortest.get(assetKey) == 0).forEach(i -> log.warn("Negative cycle found from " + cc+ "!"));
        for (TradeSearcher.Point currentPoint : shortest.keySet()) {
            if (shortest.get(currentPoint) == 0) {
                StringBuffer sb = new StringBuffer();

                Asset parent = currentPoint.id;
                List<Asset> visited = new ArrayList<>(10);
                double conversionRatio = 1;
                boolean notFound = true;
                while (notFound) {
                    visited.add(parent);

                    parent = parent.getParent();
                    if (parent == null) {
                        // TODO investigate why parents can be null nr 1
                        visited.clear();
                        notFound = false;
                    } else if (visited.contains(parent)) {
                        visited.add(parent);
                        notFound = false;
                    }
                }

                if (visited.size() == 0) {
                    // TODO investigate why parents can be null nr 2
                    continue;
                }

                int size = visited.size();
                int start = visited.indexOf(visited.get(size - 1));
                int tradeHops = size - start - 1;
                sb.append("Negative cycle found of size ").append(tradeHops).append("! ");

//                if (size > 2 && size < 5) {
                List<AssetLink> tradeRecording = new ArrayList<>(tradeHops);
                for (int i = start; i < size - 1; i++) {
//                        TODO unit tests
                    Asset to = visited.get(i + 1);
                    Asset fromz = visited.get(i);
                    AssetLink link = fromz.getNeighbours().get(to);
                    tradeRecording.add(link);
                    double tradeRate = link.getTradeRate();
//                    double displayRate = tradeRate > 1 ? tradeRate : 1 / tradeRate;

//                    double tradeRate = link.getTradeRate();
                    sb.append(fromz.getCode()).append("(").append(fromz.getTransactionAmounts()).append(")")
//                    sb.append(fromz.getCode()).append("(").append(fromz.getTransactionAmounts()).append(")")
                            .append("->")
                            .append(to.getCode())//.append("(").append(to.getTransactionAmounts()).append("/").append(link.getTradeRate()).append(")")
//                            .append(to.getCode()).append("(").append(to.getTransactionAmounts()).append(")")
                            /*.append(" with rate of ").append(askRate)*/.append("; ");
                    conversionRatio *= tradeRate;
                }
//                }

                // TODO trading from here ==> split into own method
                if (conversionRatio > 1) {
                    Balance balance = balances.get(tradeRecording.get(0).getFrom().getCode());
                    if (balance == null) {
                        // not one of the registered feeder assets
                        continue;
                    }

                    StringBuffer sbTR = new StringBuffer();
                    for (int i = 0; i < tradeRecording.size();i++)
                    {
                        sbTR.append(tradeRecording.get(i)).append("; ");
                    }
                    log.warn("Looking at trading using steps: " + sbTR.toString());

                    Trades trades = buildSequenceOfTrade(tradeRecording, balance.getAvailable());
                    if (trades == null) {
                        // not a valid sequence of trades
                        continue;
                    }
                    sbTR = new StringBuffer();
                    for (int i = 0; i < trades.getTradeRequests().size();i++)
                    {
                        sbTR.append(trades.getTradeRequests().get(i)).append("; ");
                    }
                    log.warn("Trade requests established: " + sbTR.toString());



                    sb.append(": ").append(conversionRatio);
                    log.warn(sb.toString());
                    // TODO: 2022/03/16 extract method
                    String guid = UUID.randomUUID().toString();

//                    if (conversionRatio > 1.01) {
                        sb = new StringBuffer();
                        String firstAsset = tradeRecording.get(0).getFrom().getCode();
                        if (firstAsset.equals(USDT) /*|| firstAsset.equals(EUR)*/ || firstAsset.equals("ETH")) {
                            String result = "SUCCEEDED";
                            double quantityTraded = 20;

//                            for (AssetLink currentAssetLink : tradeRecording) {
                            for (TradeRequest tradeRequest : trades.getTradeRequests()) {
                                String direction = "BUY";
                                double tradeRate = tradeRequest.getPrice();
//                                if (! tradeRequest.isBuy()) {
//                                    direction = "SELL";
////                                    quantity = quantity * currentAssetLink.getBidRate(); // TODO put back should CEILING trading not be using anymore
//                                    tradeRate = 1 / tradeRate;
//                                }

                                // TODO: 2022/03/19 ##########
//                              // TODO: 2022/03/19 only trade the amount traded during the previous trade
                                Order tradeResult = trade(tradeRequest.getSymbol(), direction, tradeRequest.getQuantity(), guid, tradeRate);

                                if (! tradeResult.getStatus().equalsIgnoreCase("CLOSED")) {
                                    // bail out for now TODO
                                    result = "FAILED";
                                    break;
                                }
                                quantityTraded = Double.valueOf(tradeResult.getProceeds());
                            }

                            sb.append(" -> traded and ").append(result);
//                            firstAsset = currentAssetLink.getFrom().getCode();
//                                String direction = "BUY";
//                                double tradeRate = currentAssetLink.getTradeRate();
//                                if (! isBuy(currentAssetLink)) {
//                                    direction = "SELL";
////                                    quantity = quantity * currentAssetLink.getBidRate(); // TODO put back should CEILING trading not be using anymore
//                                    tradeRate = 1 / tradeRate;
//                                }
//
//                                Order tradeResult = trade(currentAssetLink.getAsset1Asset2(), direction, quantity, guid, tradeRate);
//
//                                if (! tradeResult.getStatus().equalsIgnoreCase("CLOSED")) {
//                                    // bail out for now TODO
//                                    result = "FAILED";
//                                    break;
//                                }
//                                quantity = Double.valueOf(tradeResult.getProceeds());
//                            }
//
//                            sb.append(" -> traded and ").append(result);
                        }

                    // TODO: 2022/03/19 rather logs steps of TradeRequest as found ?
                        log.warn(sb.toString());
//                    }


                }
            }
        }


    }



    private boolean isBuy(AssetLink assetLink) {
        // buy base currency in quote currency
        boolean result = true;
        Market market = markets.get(assetLink.getAsset1Asset2());
        if (market.getBaseCurrencySymbol().equals(assetLink.getFrom().getCode()))
            result = false;

        return result;
        // logic below when this method still returned isSell, e.g. the inverse of the current method
//        if (current.getAsset1Asset2().equals("USD-EUR") && firstAsset.equals(EUR))
//            return false;
//
//        if (current.getAsset1Asset2().equals("USD-EUR") && firstAsset.equals(USD))
//            return true;
//
////        if (current.getAsset1Asset2().contains(EUR) || current.getAsset1Asset2().contains(USD))
////            return false;
//
//        return current.getAsset1Asset2().indexOf(firstAsset, 0) == 0;
    }

    void tickersResultToModel(TickersResult tickersResult, Map<String, Asset> assets) {

        Iterator<Ticker> iterator = tickersResult.getDeltas().iterator();

//        log.trace("new TickersResult processed: " + tickersResult); // TODO
        while (iterator.hasNext()) {
            Ticker tic = iterator.next();

            int fromIndex = 1;
            int toIndex = 0;
//            if (tic.getSymbol().equals("USD-EUR")) {
//                fromIndex = 0;
//                toIndex = 1;
//            }

            String[] symbols = tic.getSymbol().split("-");
            Asset from = assets.get(symbols[fromIndex]);
            Asset to = assets.get(symbols[toIndex]);

            if (from == null) {
                from = Asset.builder().code(symbols[fromIndex]).neighbours(new HashMap(100)).build();
            }
            from.incrementTransactions();

            if (to == null) {
                to = Asset.builder().code(symbols[toIndex]).neighbours(new HashMap(100)).build();
            }
            to.incrementTransactions();

            Map<Asset, AssetLink> fromNeighbours = from.getNeighbours();
            AssetLink assetLink = fromNeighbours.get(to);
            if (assetLink == null) {
                assetLink = AssetLink.builder().asset1Asset2(tic.getSymbol()).from(from).to(to).build();

            }
            double askRate = tic.getAskRate();
            assetLink.setAskRate(askRate);
            assetLink.setBidRate(tic.getBidRate());
            assetLink.setLastTradeRate(tic.getLastTradeRate());
            assetLink.afterBuild(isBuy(assetLink));
            fromNeighbours.put(to, assetLink);

            Map<Asset, AssetLink> toNeighbours = to.getNeighbours();
            assetLink = toNeighbours.get(from);
            if (assetLink == null) {
                assetLink = AssetLink.builder().asset1Asset2(tic.getSymbol()).from(to).to(from).build();

            }
            assetLink.setAskRate(tic.getAskRate());
            assetLink.setBidRate(tic.getBidRate());
            assetLink.setLastTradeRate(tic.getLastTradeRate());

            assetLink.afterBuild(isBuy(assetLink));
            toNeighbours.put(from, assetLink);

            assets.put(symbols[fromIndex], from);
            assets.put(symbols[toIndex], to);
        }

//        log.trace("new TickersResult processed: " + tickersResult);
    }

    private TickersResult rawToTickersResult(String raw, Queue<String> streamedFromBittrex) {
        int tickersAmount = streamedFromBittrex.size();
        log.trace("Tickers in queue: " + tickersAmount);
        if (tickersAmount > 10) {
            log.warn("There are more than 10 tickers in the queue! " + tickersAmount);
        }

        JsonObject msg = null;
        try {
            msg = DataConverter.decodeMessage(raw);
        } catch (Exception e) {
            log.error("Could not read compressed Tickers!", e);
            e.printStackTrace();
        }
        var gson = new GsonBuilder().create();

        TickersResult tickersResult = gson.fromJson(msg, TickersResult.class);
        return tickersResult;
    }

    private Trades buildSequenceOfTrade(List<AssetLink> tradeRecording, double quantityStartAsset) {

        MinTradeRequirement minTradeRequirement = new MinTradeRequirement();
        ArrayList<TradeRequest> tradeRequests = new ArrayList<>(tradeRecording.size());
        Trades result = Trades.builder().tradeRequests(tradeRequests).minTradeRequirement(minTradeRequirement).build();

        double quantityOfAsset = quantityStartAsset;

        boolean goForward = true;
        int index = 0;
        do {
            AssetLink current = tradeRecording.get(index);

            TradeRequest tradeRequest = null;
            if (index == tradeRequests.size()) {
                tradeRequest = tradeRequest.builder().symbol(current.getAsset1Asset2()).build();
                tradeRequests.add(index, tradeRequest);
            } else {
                tradeRequest = tradeRequests.get(index);
            }
            Market market = markets.get(current.getAsset1Asset2());
            if (! market.getStatus().equals("ONLINE")) {
                // one trade in the chain is not available ==> bail out!
                return null;
            }

            boolean isBuy = isBuy(current);
            if (! goForward) {
                // buy becomes sell when going backward in trade cycle
                isBuy = ! isBuy;
            }

            double nextAssetQuantity;
            if (isBuy) {
                nextAssetQuantity =  quantityOfAsset / current.getAskRate();
            } else {
                nextAssetQuantity = quantityOfAsset * current.getBidRate();
//                nextAssetQuantity = quantityOfAsset / current.getBidRate();
            }

            if (nextAssetQuantity < market.getMinTradeSize()) {
//                if (index == 0)
                    // not enough of first asset to start trade ==> bail out
                    return null; // TOOO test backward leg but for now just bail
//                goForward = false;
//                quantityOfAsset = market.getMinTradeSize();
            } else {
                tradeRequest.setBuy(isBuy);
                tradeRequest.setPrice(isBuy ? current.getAskRate() : 1 / current.getBidRate());
                tradeRequest.setQuantity(quantityOfAsset);
                quantityOfAsset = nextAssetQuantity;
            }

            if (goForward) {
                index++;
            } else {
                index--;
                if (index == -1) {
                    goForward = true;
                    index = 0;
                }

            }
        } while (index < tradeRecording.size());

        return result;
    }


}

