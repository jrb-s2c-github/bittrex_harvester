//import org.junit.Assert;
//import org.junit.Test;
import org.junit.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Jan-Rudolph on 2017-01-14.
 */
public class ShortestPathsTest {
    @Test
    public void test() {

        ArrayList<Integer>[] adj;
        ArrayList<Integer>[] cost;
        int pointAmount = 0;
        long[] calculated = null;

        // trivial
        pointAmount = 6;
        adj = (ArrayList<Integer>[])new ArrayList[pointAmount];
        cost = (ArrayList<Integer>[])new ArrayList[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }

        calculated = ShortestPaths.calculate(adj, cost, 3 - 1);
        Assert.assertEquals(42, calculated[0]);
        Assert.assertEquals(42, calculated[1]);
        Assert.assertEquals(0, calculated[2]);
        Assert.assertEquals(42, calculated[3]);
        Assert.assertEquals(42, calculated[4]);
        Assert.assertEquals(42, calculated[5]);

        // case 1
        pointAmount = 6;
        adj = (ArrayList<Integer>[])new ArrayList[pointAmount];
        cost = (ArrayList<Integer>[])new ArrayList[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }

        adj[1-1].add( 2-1);
        adj[2-1].add( 3-1);
        adj[1-1].add( 3-1);
        adj[3-1].add( 5-1);
        adj[5-1].add( 4-1);
        adj[4-1].add( 3-1);
        adj[6-1].add( 1-1);

        cost[1-1].add( 10);
        cost[2-1].add( 5);
        cost[1-1].add( 100);
        cost[3-1].add( 7);
        cost[5-1].add( 10);
        cost[4-1].add( -18);
        cost[6-1].add( -1);

        calculated = ShortestPaths.calculate(adj, cost, 1 - 1);
        Assert.assertEquals(0, calculated[0]);
        Assert.assertEquals(10, calculated[1]);
        Assert.assertEquals(45, calculated[2]);
        Assert.assertEquals(45, calculated[3]);
        Assert.assertEquals(45, calculated[4]); // ASCII for -
        Assert.assertEquals(42, calculated[5]); // ASCII for *

//        // case 1a
        pointAmount = 6;
        adj = (ArrayList<Integer>[])new ArrayList[pointAmount];
        cost = (ArrayList<Integer>[])new ArrayList[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }

        adj[1-1].add( 2-1);
        adj[2-1].add( 3-1);
        adj[1-1].add( 3-1);
        adj[3-1].add( 5-1);
        adj[5-1].add( 4-1);
        adj[4-1].add( 3-1);
        adj[6-1].add( 1-1);

        cost[1-1].add( 10);
        cost[2-1].add( 5);
        cost[1-1].add( 100);
        cost[3-1].add( 7);
        cost[5-1].add( 10);
        cost[4-1].add( -18);
        cost[6-1].add( -1);

        calculated = ShortestPaths.calculate(adj, cost, 6 - 1);
        Assert.assertEquals(-1, calculated[0]);
        Assert.assertEquals(9, calculated[1]);
        Assert.assertEquals(45, calculated[2]);
        Assert.assertEquals(45, calculated[3]);
        Assert.assertEquals(45, calculated[4]);
        Assert.assertEquals(0, calculated[5]);


//        // case 1b
        pointAmount = 6;
        adj = (ArrayList<Integer>[])new ArrayList[pointAmount];
        cost = (ArrayList<Integer>[])new ArrayList[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }

        adj[1-1].add( 2-1);
        adj[2-1].add( 3-1);
        adj[1-1].add( 3-1);
        adj[3-1].add( 5-1);
        adj[5-1].add( 4-1);
        adj[4-1].add( 3-1);
        adj[6-1].add( 1-1);

        cost[1-1].add( 10);
        cost[2-1].add( 5);
        cost[1-1].add( 100);
        cost[3-1].add( 7);
        cost[5-1].add( 10);
        cost[4-1].add( -18);
        cost[6-1].add( -1);

        calculated = ShortestPaths.calculate(adj, cost, 4 - 1);
        Assert.assertEquals(42, calculated[0]);
        Assert.assertEquals(42, calculated[1]);
        Assert.assertEquals(45, calculated[2]);
        Assert.assertEquals(45, calculated[3]);
        Assert.assertEquals(45, calculated[4]);
        Assert.assertEquals(42, calculated[5]);


        // case 1c - one vertex between split and infinite loop
        pointAmount = 7;
        adj = (ArrayList<Integer>[])new ArrayList[pointAmount];
        cost = (ArrayList<Integer>[])new ArrayList[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }

        adj[1-1].add( 2-1);
        adj[2-1].add( 3-1);
        adj[1-1].add( 3-1);
        adj[4-1].add( 6-1);
        adj[6-1].add( 5-1);
        adj[5-1].add( 4-1);
        adj[7-1].add( 1-1);
        adj[3-1].add( 4-1);

        cost[1-1].add( 10);
        cost[2-1].add( 5);
        cost[1-1].add( 100);
        cost[4-1].add( 7);
        cost[6-1].add( 10);
        cost[5-1].add( -18);
        cost[7-1].add( -1);
        cost[3-1].add( 1);

        calculated = ShortestPaths.calculate(adj, cost, 1 - 1);
        Assert.assertEquals(0, calculated[0]);
        Assert.assertEquals(10, calculated[1]);
        Assert.assertEquals(15, calculated[2]);
        Assert.assertEquals(45, calculated[3]);
        Assert.assertEquals(45, calculated[4]);
        Assert.assertEquals(45, calculated[5]); // ASCII for -
        Assert.assertEquals(42, calculated[6]); // ASCII for *


        // case 2
        pointAmount = 5;
        adj = (ArrayList<Integer>[])new ArrayList[pointAmount];
        cost = (ArrayList<Integer>[])new ArrayList[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }

        adj[1-1].add( 2-1);
        adj[4-1].add( 1-1);
        adj[2-1].add( 3-1);
        adj[3-1].add( 1-1);

        cost[1-1].add( 1);
        cost[4-1].add( 2);
        cost[2-1].add( 2);
        cost[3-1].add( -5 );

        calculated = ShortestPaths.calculate(adj, cost, 4 - 1);
        Assert.assertEquals(45, calculated[0]);
        Assert.assertEquals(45, calculated[1]);
        Assert.assertEquals(45, calculated[2]);
        Assert.assertEquals(0, calculated[3]);
        Assert.assertEquals(42, calculated[4]);

        // case 2 b
        pointAmount = 5;
        adj = (ArrayList<Integer>[])new ArrayList[pointAmount];
        cost = (ArrayList<Integer>[])new ArrayList[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }

        adj[1-1].add( 2-1);
        adj[4-1].add( 1-1);
        adj[2-1].add( 3-1);
        adj[3-1].add( 1-1);

        cost[1-1].add( 1);
        cost[4-1].add( 2);
        cost[2-1].add( 2);
        cost[3-1].add( -5 );

        calculated = ShortestPaths.calculate(adj, cost, 2 - 1);
        Assert.assertEquals(45, calculated[0]);
        Assert.assertEquals(45, calculated[1]);
        Assert.assertEquals(45, calculated[2]);
        Assert.assertEquals(42, calculated[3]);
        Assert.assertEquals(42, calculated[4]);

        // case 2 c
        pointAmount = 5;
        adj = (ArrayList<Integer>[])new ArrayList[pointAmount];
        cost = (ArrayList<Integer>[])new ArrayList[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }

        adj[1-1].add( 2-1);
        adj[4-1].add( 1-1);
        adj[2-1].add( 3-1);
        adj[3-1].add( 1-1);

        cost[1-1].add( 1);
        cost[4-1].add( 2);
        cost[2-1].add( 2);
        cost[3-1].add( -5 );

        calculated = ShortestPaths.calculate(adj, cost, 5 - 1);
        Assert.assertEquals(42, calculated[0]);
        Assert.assertEquals(42, calculated[1]);
        Assert.assertEquals(42, calculated[2]);
        Assert.assertEquals(42, calculated[3]);
        Assert.assertEquals(0, calculated[4]);

        // case 2 d
        pointAmount = 5;
        adj = (ArrayList<Integer>[])new ArrayList[pointAmount];
        cost = (ArrayList<Integer>[])new ArrayList[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }

        adj[1-1].add( 2-1);
        adj[4-1].add( 1-1);
        adj[2-1].add( 3-1);
        adj[3-1].add( 1-1);
        adj[3-1].add( 5-1);

        cost[1-1].add( 1);
        cost[4-1].add( 2);
        cost[2-1].add( 2);
        cost[3-1].add( -5 );
        cost[3-1].add( 7 );

        calculated = ShortestPaths.calculate(adj, cost, 4 - 1);
        Assert.assertEquals(45, calculated[0]);
        Assert.assertEquals(45, calculated[1]);
        Assert.assertEquals(45, calculated[2]);
        Assert.assertEquals(0, calculated[3]);
        Assert.assertEquals(45, calculated[4]);


        // case 2 e
        pointAmount = 5;
        adj = (ArrayList<Integer>[])new ArrayList[pointAmount];
        cost = (ArrayList<Integer>[])new ArrayList[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }

        adj[1-1].add( 2-1);
        adj[4-1].add( 1-1);
        adj[2-1].add( 3-1);
        adj[3-1].add( 1-1);
        adj[3-1].add( 5-1);
        adj[5-1].add( 4-1);

        cost[1-1].add( 1);
        cost[4-1].add( 2);
        cost[2-1].add( 2);
        cost[3-1].add( -5 );
        cost[3-1].add( 7 );
        cost[5-1].add( 10 );

        calculated = ShortestPaths.calculate(adj, cost, 4 - 1);
        Assert.assertEquals(45, calculated[0]);
        Assert.assertEquals(45, calculated[1]);
        Assert.assertEquals(45, calculated[2]);
        Assert.assertEquals(45, calculated[3]);
        Assert.assertEquals(45, calculated[4]);



        // my test of infinite loop
        pointAmount = 4;
        adj = (ArrayList<Integer>[])new ArrayList[pointAmount];
        cost = (ArrayList<Integer>[])new ArrayList[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }

        adj[1-1].add( 2-1);
        adj[2-1].add( 3-1);
        adj[3-1].add( 4-1);
        adj[4-1].add( 1-1);

        cost[1-1].add( 1);
        cost[2-1].add( 2);
        cost[3-1].add( 3);
        cost[4-1].add( -7 );

        calculated = ShortestPaths.calculate(adj, cost, 2 - 1);
        Assert.assertEquals(45, calculated[0]);
        Assert.assertEquals(45, calculated[1]);
        Assert.assertEquals(45, calculated[2]);
        Assert.assertEquals(45, calculated[3]);

        // my case: point after infinite loop
        pointAmount = 6;
        adj = (ArrayList<Integer>[])new ArrayList[pointAmount];
        cost = (ArrayList<Integer>[])new ArrayList[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }

        adj[1-1].add( 2-1);
        adj[2-1].add( 3-1);
        adj[1-1].add( 3-1);
        adj[3-1].add( 5-1);
        adj[5-1].add( 4-1);
        adj[4-1].add( 3-1);
        adj[4-1].add( 6-1);

        cost[1-1].add( 10);
        cost[2-1].add( 5);
        cost[1-1].add( 100);
        cost[3-1].add( 7);
        cost[5-1].add( 10);
        cost[4-1].add( -18);
        cost[4-1].add( 1);

        calculated = ShortestPaths.calculate(adj, cost, 1 - 1);
        Assert.assertEquals(0, calculated[0]);
        Assert.assertEquals(10, calculated[1]);
        Assert.assertEquals(45, calculated[2]);
        Assert.assertEquals(45, calculated[3]);
        Assert.assertEquals(45, calculated[4]);
        Assert.assertEquals(45, calculated[5]);

        // my test of finite loop
        pointAmount = 4;
        adj = (ArrayList<Integer>[])new ArrayList[pointAmount];
        cost = (ArrayList<Integer>[])new ArrayList[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }

        adj[1-1].add( 2-1);
        adj[2-1].add( 3-1);
        adj[3-1].add( 4-1);
        adj[4-1].add( 1-1);

        cost[1-1].add( 1);
        cost[2-1].add( 2);
        cost[3-1].add( 3);
        cost[4-1].add( 4 );

        calculated = ShortestPaths.calculate(adj, cost, 1 - 1);
        Assert.assertEquals(0, calculated[0]);
        Assert.assertEquals(1, calculated[1]);
        Assert.assertEquals(3, calculated[2]);
        Assert.assertEquals(6, calculated[3]);

        // my test of finite loop - last one negative
        pointAmount = 4;
        adj = (ArrayList<Integer>[])new ArrayList[pointAmount];
        cost = (ArrayList<Integer>[])new ArrayList[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }

        adj[1-1].add( 2-1);
        adj[2-1].add( 3-1);
        adj[3-1].add( 4-1);
        adj[4-1].add( 1-1);

        cost[1-1].add( 1);
        cost[2-1].add( 2);
        cost[3-1].add( 3);
        cost[4-1].add( -4 );

        calculated = ShortestPaths.calculate(adj, cost, 1 - 1);
        Assert.assertEquals(0, calculated[0]);
        Assert.assertEquals(1, calculated[1]);
        Assert.assertEquals(3, calculated[2]);
        Assert.assertEquals(6, calculated[3]);

        // test of infinite loop hanging from one point
        pointAmount = 6;
        adj = (ArrayList<Integer>[])new ArrayList[pointAmount];
        cost = (ArrayList<Integer>[])new ArrayList[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }

        adj[1-1].add( 2-1);
        adj[2-1].add( 3-1);
        adj[2-1].add( 4-1);
        adj[4-1].add( 5-1);
        adj[5-1].add( 6-1);
        adj[6-1].add( 2-1);

        cost[1-1].add( 1);
        cost[2-1].add( 2);
        cost[2-1].add( 3);
        cost[4-1].add( -4 );
        cost[5-1].add( -5 );
        cost[6-1].add( -6 );

        calculated = ShortestPaths.calculate(adj, cost, 1 - 1);
        Assert.assertEquals(0, calculated[0]);
        Assert.assertEquals(45, calculated[1]);
        Assert.assertEquals(45, calculated[2]);
        Assert.assertEquals(45, calculated[3]);
        Assert.assertEquals(45, calculated[4]);
        Assert.assertEquals(45, calculated[5]);

        // double loop, one infinite
        pointAmount = 8;
        adj = (ArrayList<Integer>[])new ArrayList[pointAmount];
        cost = (ArrayList<Integer>[])new ArrayList[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }

        adj[1-1].add( 2-1);
        adj[2-1].add( 3-1);
        adj[2-1].add( 4-1);
        adj[4-1].add( 5-1);
        adj[5-1].add( 6-1);
        adj[6-1].add( 4-1);
        adj[4-1].add( 7-1);
        adj[7-1].add( 8-1);
        adj[8-1].add( 4-1);

        cost[1-1].add( 1);
        cost[2-1].add( 2);
        cost[2-1].add( 3);
        cost[4-1].add( 5 );
        cost[5-1].add( -5 );
        cost[6-1].add( -6 );
        cost[4-1].add( 13 );
        cost[7-1].add( 19 );
        cost[8-1].add( 20 );

        calculated = ShortestPaths.calculate(adj, cost, 1 - 1);
        Assert.assertEquals(0, calculated[0]);
        Assert.assertEquals(1, calculated[1]);
        Assert.assertEquals(3, calculated[2]);
        Assert.assertEquals(45, calculated[3]);
        Assert.assertEquals(45, calculated[4]);
        Assert.assertEquals(45, calculated[5]);
        Assert.assertEquals(45, calculated[5]);
        Assert.assertEquals(45, calculated[5]);

        // one negative edge
        pointAmount = 2;
        adj = (ArrayList<Integer>[])new ArrayList[pointAmount];
        cost = (ArrayList<Integer>[])new ArrayList[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }

        adj[1-1].add( 2-1);

        cost[1-1].add( -15);

        calculated = ShortestPaths.calculate(adj, cost, 1 - 1);
        Assert.assertEquals(0, calculated[0]);
        Assert.assertEquals(-15, calculated[1]);


        // one negative and one positive edge
        pointAmount = 3;
        adj = (ArrayList<Integer>[])new ArrayList[pointAmount];
        cost = (ArrayList<Integer>[])new ArrayList[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }

        adj[1-1].add( 2-1);
        adj[2-1].add( 3-1);

        cost[1-1].add( -15);
        cost[2-1].add( 12);

        calculated = ShortestPaths.calculate(adj, cost, 1 - 1);
        Assert.assertEquals(0, calculated[0]);
        Assert.assertEquals(-15, calculated[1]);
        Assert.assertEquals(-3, calculated[2]);


        // one negative and one positive edge
        pointAmount = 3;
        adj = (ArrayList<Integer>[])new ArrayList[pointAmount];
        cost = (ArrayList<Integer>[])new ArrayList[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }

        adj[1-1].add( 2-1);
        adj[2-1].add( 3-1);

        cost[1-1].add( 3);
        cost[2-1].add( -12);

        calculated = ShortestPaths.calculate(adj, cost, 2 - 1);
        Assert.assertEquals(42, calculated[0]);
        Assert.assertEquals(0 , calculated[1]);
        Assert.assertEquals(-12, calculated[2]);

        // start part of infinite loop and proceeding to second graph
        pointAmount = 5;
        adj = (ArrayList<Integer>[])new ArrayList[pointAmount];
        cost = (ArrayList<Integer>[])new ArrayList[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }
        adj[1-1].add( 2-1);
        adj[2-1].add( 3-1);
        adj[3-1].add( 1-1);
        adj[1-1].add( 4-1);
        adj[4-1].add( 5-1);
//        adj[4-1].add( 5-1);

        cost[1-1].add( -12);
        cost[2-1].add( 2);
        cost[3-1].add( 5);
        cost[1-1].add( 3);
        cost[4-1].add( 4);

        calculated = ShortestPaths.calculate(adj, cost, 1 - 1);
        Assert.assertEquals(45, calculated[0]);
        Assert.assertEquals(45, calculated[1]);
        Assert.assertEquals(45, calculated[2]);
        Assert.assertEquals(45, calculated[3]);
        Assert.assertEquals(45, calculated[4]);

        // negative graph cycle from course notes
        pointAmount = 5;
        adj = (ArrayList<Integer>[])new ArrayList[pointAmount];
        cost = (ArrayList<Integer>[])new ArrayList[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }

        adj[1-1].add( 2-1);
        adj[1-1].add( 5-1);
        adj[2-1].add( 5-1);
        adj[3-1].add( 2-1);
        adj[3-1].add( 4-1);
        adj[5-1].add( 3-1);
        adj[5-1].add( 4-1);

        cost[1-1].add( 4);
        cost[1-1].add( 3);
        cost[2-1].add( -2);
        cost[3-1].add( 4);
        cost[3-1].add( 2);
        cost[5-1].add( -3);
        cost[5-1].add( 1);

        calculated = ShortestPaths.calculate(adj, cost, 1 - 1);
        Assert.assertEquals(0, calculated[0]);
        Assert.assertEquals(45, calculated[1]);
        Assert.assertEquals(45, calculated[2]);
        Assert.assertEquals(45, calculated[3]);
        Assert.assertEquals(45, calculated[4]);

        // isomorph but to last but no negative cycle
        pointAmount = 5;
        adj = (ArrayList<Integer>[])new ArrayList[pointAmount];
        cost = (ArrayList<Integer>[])new ArrayList[pointAmount];
        for (int i = 0; i < pointAmount; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }

        adj[1-1].add( 2-1);
        adj[1-1].add( 5-1);
        adj[2-1].add( 5-1);
        adj[3-1].add( 2-1);
        adj[3-1].add( 4-1);
        adj[5-1].add( 3-1);
        adj[5-1].add( 4-1);

        cost[1-1].add( 4);
        cost[1-1].add( 3);
        cost[2-1].add( 7);
        cost[3-1].add( 4);
        cost[3-1].add( 2);
        cost[5-1].add( 13);
        cost[5-1].add( 1);

        calculated = ShortestPaths.calculate(adj, cost, 1 - 1);
        Assert.assertEquals(0, calculated[0]);
        Assert.assertEquals(4, calculated[1]);
        Assert.assertEquals(16, calculated[2]);
        Assert.assertEquals(4, calculated[3]);
        Assert.assertEquals(3, calculated[4]);
    }
}
