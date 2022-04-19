package za.co.s2c.cb.tickers_listener;

import org.junit.jupiter.api.Test;
import za.co.s2c.cb.model.linqua_franca.Asset;
import za.co.s2c.cb.model.linqua_franca.AssetLink;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TradeSearcherTest {

    @Test
    public void testSearch() {
        // TODO: 2022/03/13  
//        sample1();
//        sample2();
    }

    private void sample2() {
        Asset a1 = Asset.builder().code("1").neighbours(new HashMap(1)).build();
        Asset a2 = Asset.builder().code("2").neighbours(new HashMap(1)).build();
        Asset a3 = Asset.builder().code("3").neighbours(new HashMap(1)).build();
        Asset a4 = Asset.builder().code("4").neighbours(new HashMap(1)).build();
        Asset a5 = Asset.builder().code("5").neighbours(new HashMap(1)).build();

        int pointAmount = 5;

        a1.getNeighbours().put(a2, AssetLink.builder().from(a1).to(a2).askRate(Math.pow(2.0, 1)).asset1Asset2("").build().afterBuild(true));
//        1 2 1
        a4.getNeighbours().put(a1, AssetLink.builder().from(a4).to(a1).askRate(Math.pow(2.0, 2)).asset1Asset2("").build().afterBuild(true));
//        4 1 2
        a2.getNeighbours().put(a3, AssetLink.builder().from(a2).to(a3).askRate(Math.pow(2.0, 2)).asset1Asset2("").build().afterBuild(true));
//        2 3 2
        a3.getNeighbours().put(a1, AssetLink.builder().from(a3).to(a1).askRate(Math.pow(2.0, -5)).asset1Asset2("").build().afterBuild(true));
//        3 1 -5

        Set<Asset> assets = new HashSet<>(pointAmount);
        assets.add(a1);
        assets.add(a2);
        assets.add(a3);
        assets.add(a4);
        assets.add(a5);

        Asset from = a4;

        HashMap<TradeSearcher.Point, Integer> reachable = new HashMap<>(pointAmount);
        HashMap<Asset, Double> distance = new HashMap<>(pointAmount);
        HashMap<TradeSearcher.Point, Integer> shortest = new HashMap<>(pointAmount);
        new TradeSearcher().shortestPaths(from, distance, reachable, shortest, assets);

        Map<String, String> result = interpretResults(pointAmount, reachable, distance, shortest);

        assertEquals("-", result.get("1"));
        assertEquals("-", result.get("2"));
        assertEquals("-", result.get("3"));
        assertEquals("0", result.get("4"));
        assertEquals("*", result.get("5"));
    }


    private void sample1() {
        Asset a1 = Asset.builder().code("1").neighbours(new HashMap(1)).build();
        Asset a2 = Asset.builder().code("2").neighbours(new HashMap(1)).build();
        Asset a3 = Asset.builder().code("3").neighbours(new HashMap(1)).build();
        Asset a4 = Asset.builder().code("4").neighbours(new HashMap(1)).build();
        Asset a5 = Asset.builder().code("5").neighbours(new HashMap(1)).build();
        Asset a6 = Asset.builder().code("6").neighbours(new HashMap(1)).build();

        int pointAmount = 6;

        a1.getNeighbours().put(a2, AssetLink.builder().from(a1).to(a2).askRate(Math.pow(2.0, 10.0)).asset1Asset2("").build().afterBuild(true)); // 2 to the power of the amount in the course material algo works with log2
        a2.getNeighbours().put(a3, AssetLink.builder().from(a2).to(a3).askRate(Math.pow(2.0, 5)).asset1Asset2("").build().afterBuild(true));
        a1.getNeighbours().put(a3, AssetLink.builder().from(a1).to(a3).askRate(Math.pow(2.0, 100)).asset1Asset2("").build().afterBuild(true));
        a3.getNeighbours().put(a5, AssetLink.builder().from(a3).to(a5).askRate(Math.pow(2.0, 7)).asset1Asset2("").build().afterBuild(true));
        a5.getNeighbours().put(a4, AssetLink.builder().from(a5).to(a4).askRate(Math.pow(2.0, 10)).asset1Asset2("").build().afterBuild(true));
        a4.getNeighbours().put(a3, AssetLink.builder().from(a4).to(a3).askRate(Math.pow(2.0, -18)).asset1Asset2("").build().afterBuild(true));
        a6.getNeighbours().put(a1, AssetLink.builder().from(a6).to(a1).askRate(Math.pow(2.0, -1)).asset1Asset2("").build().afterBuild(true));

        Set<Asset> assets = new HashSet<>(pointAmount);
        assets.add(a1);
        assets.add(a2);
        assets.add(a3);
        assets.add(a4);
        assets.add(a5);
        assets.add(a6);

        Asset from = a1;

        HashMap<TradeSearcher.Point, Integer> reachable = new HashMap<>(pointAmount);
        HashMap<Asset, Double> distance = new HashMap<>(pointAmount);
        HashMap<TradeSearcher.Point, Integer> shortest = new HashMap<>(pointAmount);
        new TradeSearcher().shortestPaths(from, distance, reachable, shortest, assets);

        Map<String, String> result = interpretResults(pointAmount, reachable, distance, shortest);

        assertEquals("0", result.get("1"));
        assertEquals("10", result.get("2"));
        assertEquals("-", result.get("3"));
        assertEquals("-", result.get("4"));
        assertEquals("-", result.get("5"));
        assertEquals("*", result.get("6"));
    }


    private Map<String, String> interpretResults(int pointAmount, HashMap<TradeSearcher.Point, Integer> reachable, HashMap<Asset, Double> distance, HashMap<TradeSearcher.Point, Integer> shortest) {
        Map<String, String> result = new HashMap<>(pointAmount);
        for (int i = 1; i <= pointAmount; i++) {
            result.put("" + i, "*");
        }

        for (TradeSearcher.Point currentPoint : reachable.keySet()) {
            if (reachable.get(currentPoint) == 0) {
                result.put(currentPoint.id.getCode(), "*");
            } else if (shortest.get(currentPoint) == 0) {
                result.put(currentPoint.id.getCode(), "-");
            } else {
                result.put(currentPoint.id.getCode(), String.valueOf(distance.get(currentPoint.id).intValue()));
            }
        }
        return result;
    }

}
