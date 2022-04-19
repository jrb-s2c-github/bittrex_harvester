package za.co.s2c.cb.tickers_listener;

import za.co.s2c.cb.model.linqua_franca.Asset;
import za.co.s2c.cb.model.linqua_franca.AssetLink;

import java.util.*;

public class TradeSearcher {


    static class Point {
        Point(Asset id, Double distance) {
            this.distance = distance; // distance from start point to point 'id'
            this.realDistance = distance;
            this.id = id;
        }

        Asset id;
        Double distance;
        Double realDistance;

        Map<Integer, Point> neighbours = new HashMap<Integer, Point>();
    }

    public void shortestPaths(Asset from, Map<Asset, Double> distance, Map<Point, Integer> reachable, Map<Point, Integer> shortest, Collection<Asset> assets) {

        int pointAmount = assets.size();
        Map<Asset, Point> pointMap = new HashMap<>(pointAmount); // rename to linkedPointMap/distanceMap/distanceMatrix as only points connected to others are entered as key towards linked points here

        List prev = new ArrayList(pointAmount);
        for (Asset asset : assets) {
            Point point = new Point(asset, Double.MAX_VALUE);
            pointMap.put(asset, point);
//            prev[Asset] = -1;
        }

        Point fromPoint = new Point(from, 0.0);
        pointMap.put(from, fromPoint);
        fromPoint.realDistance = 0.0;

        for (int i = 0; i < pointAmount - 1; i++) {
            relaxingCycle(pointMap, prev, from, assets);
        }
        // take copy of distance to determine any negative cycles from
        for (Asset asset : pointMap.keySet()) {
//        for (int i = 0; i < pointAmount; i++) {
//            distance[i] = pointMap.get(i).distance;
            distance.put(asset, pointMap.get(asset).distance);
            shortest.put(pointMap.get(asset), 1);
        }
        // another round to determine negative cycle
        relaxingCycle(pointMap, prev, from, assets);

        // determine vertices reachable from infinite loop
        Queue<Point> processingQueue = new LinkedList<>();
//        for (int i = 0; i < pointAmount; i++) {
        for (Asset asset : pointMap.keySet()) {
            Point point = pointMap.get(asset);
            Double latestDistance = point.distance;
            if (latestDistance < distance.get(asset)) {
                processingQueue.add(point);
//                continue;
            }
        }
        while (!processingQueue.isEmpty()) {
            Point processingPoint = processingQueue.poll();
            if (shortest.get(processingPoint) == 0) {
                continue; // TODO does break work? think not
            }

//            for (Integer neighbour : adj[index]) {
            for (Asset neighbour : processingPoint.id.getNeighbours().keySet()) {
                Point nPoint = pointMap.get(neighbour);
                if (!processingQueue.contains(nPoint)) {
                    processingQueue.add(nPoint);
                }
            }
//            shortest[index] = 0;
            shortest.put(processingPoint, 0);
        }

        // determine distance
//        for (int i = 0; i < pointAmount; i++) {
        for (Asset asset : pointMap.keySet()) {
//                distance[i] = pointMap.get(i).realDistance;
            distance.put(asset, pointMap.get(asset).realDistance);
        }

        // determine edges reachable  from start vertex
        processingQueue = new LinkedList<>();
        processingQueue.add(pointMap.get(from));
        while (!processingQueue.isEmpty()) {
            Point processingPoint = processingQueue.poll();
            if (reachable.get(processingPoint) != null && reachable.get(processingPoint) == 1) {
                continue;
            }
//              for (Integer neighbour : adj[index]) {
            for (Asset neighbour : processingPoint.id.getNeighbours().keySet()) {
                Point nPoint = pointMap.get(neighbour);
                if (!processingQueue.contains(nPoint)) {
                    processingQueue.add(nPoint);
                }
            }
            reachable.put(processingPoint, 1);
//            reachable[index] = 1;
        }
    }

    private static int relaxingCycle(Map<Asset, Point> pointMap, List<Asset> prev, Asset from, Collection<Asset> assets) {
        int result = 0;
        Asset current = from; // start with from
        Iterator<Asset> assetIterator = assets.iterator();
        boolean toContinue = true;
        Asset prevAsset = current;
        LinkedList<Asset> breadthFirstSearchDiscovered = new LinkedList<Asset>();
        Set<Asset> breadthFirstSearchVisited = new HashSet<>(assets.size());
        breadthFirstSearchVisited.add(from);

//        for (int i = from; i < adj.length+from; i++) {
        while (current != null) {
//            int processingIndex = i % (adj.length);
            Point processing = pointMap.get(current);

            for (Asset neighbourAsset : current.getNeighbours().keySet()) {
//                if (breadthFirstSearchVisited.contains(neighbourAsset)) {
//                    continue;
//                }

//            for (int j = 0; j < adj[processingIndex].size(); j++) {

//                int neighbourIndex = adj[processingIndex].get(j);

// TODO: 2022/03/30 #########
                if (current.equals(prevAsset)) {
                    continue;
                }
//                if (prev.contains(neighbourAsset)) {
//                    continue;
//                }
                Point neighbour = pointMap.get(neighbourAsset);

//                breadthFirstSearchDiscovered.add(neighbour.id);
                AssetLink neighbourCost = current.getNeighbours().get(neighbourAsset);
                if (neighbourCost == null) {
                    for (AssetLink candidate : current.getNeighbours().values()) {
                        if (candidate.getTo().getCode().equals(neighbourAsset.getCode())) {
                            neighbourCost = candidate;
                            break;
                        }
                    }
                }

//                Integer costi = cost[processingIndex].get(j);
//                Double costi = neighbourCost.getAskRate();
                Double costi = neighbourCost.getLogWeight();

                Double processingDistance = processing.distance;

                Double calcCost = processingDistance + costi;
                if (processingDistance < Double.MAX_VALUE && neighbour.distance > calcCost) {
                    // relax

                    neighbour.distance = calcCost;

//                        prev[neighbourIndex] = processing.id;
                    prev.add(current);
                    result = 1;

                }

                processingDistance = processing.realDistance;
                calcCost = processingDistance + costi;
                if (processingDistance < Double.MAX_VALUE && neighbour.realDistance > calcCost) {
                    // relax
                    if (neighbourAsset != from) {
                        neighbour.realDistance = calcCost;
                        neighbour.id.setParent(current);
                    }
                }
            }

            for (Asset neighbourAsset : current.getNeighbours().keySet()) {
                if (!breadthFirstSearchVisited.contains(neighbourAsset)) {
                    breadthFirstSearchDiscovered.add(neighbourAsset);
                }
            }

            if (breadthFirstSearchDiscovered.peek() != null) {
                breadthFirstSearchVisited.add(current);

                prevAsset = current;
                current = breadthFirstSearchDiscovered.remove();
            } else {
                current = null;
            }

        }

        return result;
    }

//    public static long[] calculate(ArrayList<Integer>[] adj,
//                            ArrayList<Integer>[] cost, int s) {
//        int pointAmount = adj.length;
////        long distance[] = new long[pointAmount];
//        Map<Asset, Long> distance = new HashMap<>(pointAmount);
////        int reachable[] = new int[pointAmount];
//        Map<Asset, Long> reachable = new HashMap<>(pointAmount);
////        int shortest[] = new int[pointAmount];
//        Map<Asset, Long> shortest = new HashMap<>(pointAmount);
//        for (int i = 0; i < pointAmount; i++) {
//            distance[i] = Long.MAX_VALUE;
//            reachable[i] = 0;
//            shortest[i] = 1;
//        }
//
//        TradeSearcher.shortestPaths(adj, cost, s, distance, reachable, shortest);
//
//        long result[] = new long[pointAmount];
//        for (int i = 0; i < pointAmount; i++) {
//            if (reachable[i] == 0) {
//                result[i] = '*';
//            } else if (shortest[i] == 0) {
//                result[i] = '-';
//            } else {
//                result[i] = distance[i];
//            }
//        }
//        return result;
//    }

//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        int n = scanner.nextInt();
//        int m = scanner.nextInt();
//        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
//        ArrayList<Integer>[] cost = (ArrayList<Integer>[])new ArrayList[n];
//        for (int i = 0; i < n; i++) {
//            adj[i] = new ArrayList<Integer>();
//            cost[i] = new ArrayList<Integer>();
//        }
//        for (int i = 0; i < m; i++) {
//            int x, y, w;
//            x = scanner.nextInt();
//            y = scanner.nextInt();
//            w = scanner.nextInt();
//            adj[x - 1].add(y - 1);
//            cost[x - 1].add(w);
//        }
//        int s = scanner.nextInt() - 1;
//        long distance[] = new long[n];
//        int reachable[] = new int[n];
//        int shortest[] = new int[n];
//        for (int i = 0; i < n; i++) {
//            distance[i] = Long.MAX_VALUE;
//            reachable[i] = 0;
//            shortest[i] = 1;
//        }
//        shortestPaths(adj, cost, s, distance, reachable, shortest);
//        for (int i = 0; i < n; i++) {
//            if (reachable[i] == 0) {
//                System.out.println('*');
//            } else if (shortest[i] == 0) {
//                System.out.println('-');
//            } else {
//                System.out.println(distance[i]);
//            }
//        }
//    }
}

