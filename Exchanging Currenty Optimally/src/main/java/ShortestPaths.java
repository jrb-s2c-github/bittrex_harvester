import java.util.*;

public class ShortestPaths {


    static class Point {
        Point (int id, long distance) {
            this.distance = distance;
            this.realDistance = distance;
            this.id = id;
        }
        int id;
        long distance;
        long realDistance;

        Map<Integer, Point> neighbours = new HashMap<Integer, Point>();
    }

    private static void shortestPaths(ArrayList<Integer>[] adj,
                                      ArrayList<Integer>[] cost,
                                      int from, long[] distance, int[] reachable, int[] shortest) {

        int pointAmount = adj.length;
        Map<Integer, Point> pointMap = new HashMap<Integer, Point>(pointAmount);

        int[] prev = new int[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            Point point = new Point(i, Long.MAX_VALUE);
            pointMap.put(i, point);
            prev[i] = -1;
        }

        pointMap.get(from).distance = 0;
        pointMap.get(from).realDistance = 0;

        for (int i = 0; i < pointAmount - 1; i++) {
            relaxingCycle(adj, cost, pointMap, prev, from);
        }
        // take copy of distance to determine any negative cycles from
        for (int i = 0; i < pointAmount; i++) {
            distance[i] = pointMap.get(i).distance;
            shortest[i] = 1;
        }
        // another round to determine negative cycle
        relaxingCycle(adj, cost, pointMap, prev, from);

        // determine vertices reachable from infinite loop
        Queue<Integer> processingQueue = new LinkedList<Integer>();
        for (int i = 0; i < pointAmount; i++) {
            long latestDistance = pointMap.get(i).distance;
            if (latestDistance < distance[i]) {
                processingQueue.add(i);
//                continue;
            }
        }
        while (! processingQueue.isEmpty()) {
            Integer index = processingQueue.poll();
            if (shortest[index] == 0) {
                continue; // TODO does break work? think not
            }

            for (Integer neighbour : adj[index]) {
                if (! processingQueue.contains(neighbour)) {
                    processingQueue.add(neighbour);
                }
              }
            shortest[index] = 0;
        }

        // determine distance
        for (int i = 0; i < pointAmount; i++) {
                distance[i] = pointMap.get(i).realDistance;
        }

        // determine edges reachable  from start vertex
        processingQueue = new LinkedList<Integer>();
        processingQueue.add(from);
        while (! processingQueue.isEmpty()) {
            Integer index = processingQueue.poll();
            if (reachable[index] == 1) {
                continue;
            }
              for (Integer neighbour : adj[index]) {
                  if (! processingQueue.contains(neighbour)) {
                      processingQueue.add(neighbour);
                  }
              }
            reachable[index] = 1;
        }

    }

    private static int relaxingCycle(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, Map<Integer, Point> pointMap, int[] prev, int from)
    {
        int result = 0;
        for (int i = from; i < adj.length+from; i++) {
            int processingIndex = i % (adj.length);
            Point processing = pointMap.get(processingIndex);
            for (int j = 0; j < adj[processingIndex].size(); j++) {

                int neighbourIndex = adj[processingIndex].get(j);

                Point neighbour = pointMap.get(neighbourIndex);

                Integer costi = cost[processingIndex].get(j);

                long processingDistance = processing.distance;

                long calcCost = processingDistance + costi;
                if (processingDistance < Long.MAX_VALUE && neighbour.distance > calcCost) {
                    // relax

                        neighbour.distance = calcCost;

                        prev[neighbourIndex] = processing.id;
                        result = 1;

                }

                processingDistance = processing.realDistance;
                calcCost = processingDistance + costi;
                if (processingDistance < Long.MAX_VALUE && neighbour.realDistance > calcCost) {
                    // relax
                    if (neighbourIndex != from) {
                        neighbour.realDistance = calcCost;
                    }
                }
            }
        }

        return result;
    }

    public static long[] calculate(ArrayList<Integer>[] adj,
                            ArrayList<Integer>[] cost, int s) {
        int pointAmount = adj.length;
        long distance[] = new long[pointAmount];
        int reachable[] = new int[pointAmount];
        int shortest[] = new int[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            distance[i] = Long.MAX_VALUE;
            reachable[i] = 0;
            shortest[i] = 1;
        }

         ShortestPaths.shortestPaths(adj, cost, s, distance, reachable, shortest);

        long result[] = new long[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            if (reachable[i] == 0) {
                result[i] = '*';
            } else if (shortest[i] == 0) {
                result[i] = '-';
            } else {
                result[i] = distance[i];
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
        ArrayList<Integer>[] cost = (ArrayList<Integer>[])new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y, w;
            x = scanner.nextInt();
            y = scanner.nextInt();
            w = scanner.nextInt();
            adj[x - 1].add(y - 1);
            cost[x - 1].add(w);
        }
        int s = scanner.nextInt() - 1;
        long distance[] = new long[n];
        int reachable[] = new int[n];
        int shortest[] = new int[n];
        for (int i = 0; i < n; i++) {
            distance[i] = Long.MAX_VALUE;
            reachable[i] = 0;
            shortest[i] = 1;
        }
        shortestPaths(adj, cost, s, distance, reachable, shortest);
        for (int i = 0; i < n; i++) {
            if (reachable[i] == 0) {
                System.out.println('*');
            } else if (shortest[i] == 0) {
                System.out.println('-');
            } else {
                System.out.println(distance[i]);
            }
        }
    }
}

