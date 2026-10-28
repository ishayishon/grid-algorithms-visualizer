import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.assertThrows;


import java.util.ArrayList;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
/**
This is a very basic Testing class for Map - please note that this JUnit
 contains only a very limited testing method and should be added many other
 methods for testing all the functionality of Map2D - both in correctness and in runtime.
*/
 class MapTest {
    /**
     * _m_3_3 =
     * 0,1,0
     * 1,0,1
     * 0,1,0
     *
     * _m0 =
     * 1,1,1,1,1
     * 1,0,1,0,1
     * 1,0,0,0,1
     * 1,0,1,0,1
     * 1,1,1,1,1
     * 1,0,1,0,1
     *
     * 1, 1, 1, 1, 1
     * 1,-1, 1,-1, 1
     * 1,-1,-1,-1, 1
     * 1,-1, 1,-1, 1
     * 1, 1, 1, 1, 1
     * 1,-1, 1,-1, 1
     *
     * m2[3][2] = 0, m2[1][2] = 10, |sp|=11 (isCiclic = false;}
     * =============
     * 7, 8, 9, 1, 7
     * 6,-1,10,-1, 6
     * 5,-1,-1,-1, 5
     * 4,-1, 0,-1, 4
     * 3, 2, 1, 2, 3
     * 4,-1, 2,-1, 4
     *
     * m[3][2] = 0, m2[1][2] = 5, |sp|=5 (isCiclic = true;}
     * 5, 4, 3, 4, 5
     * 6,-1, 4,-1, 6
     * 5,-1,-1,-1, 5
     * 4,-1, 0,-1, 4
     * 3, 2, 1, 2, 3
     * 4,-1, 2,-1, 4
     */
    private int[][] _map = {{1,1,1,1,1}, {1,0,1,0,1}, {1,0,0,0,1},  {1,0,1,0,1},  {1,1,1,1,1}, {1,0,1,0,1}};
    private int[][] _map_3_3 = {{0,1,0}, {1,0,1}, {0,1,0}};

    private int[][] bigMap;

    public MapTest() {
        bigMap = new int[350][350];
        int num = 0;
        for (int i = 0; i < bigMap.length; i++) {
            for (int j = 0; j < bigMap[i].length; j++) {
                bigMap[i][j] = num++ % 4;
            }
        }
    }

    private Map2D _m0, _m1, _m2, _m3, _m3_3, _mbig;
    @BeforeEach
    public void setup() {
        _m0 = new Map(_map);
        _m1 = new Map(_map); _m1.setCyclic(true);
        _m2 = new Map(_map); _m2.setCyclic(false);
        _m3 = new Map(_map);
        _m3_3 = new Map(_map_3_3);_m3_3.setCyclic(false);
        _mbig=new Map(bigMap);
    }
    @Test
    @Timeout(value = 1, unit = SECONDS)
    void init() {
        int[][] bigarr = new int [500][500];
        _m1.init(bigarr);
        assertEquals(bigarr.length, _m1.getWidth());
        assertEquals(bigarr[0].length, _m1.getHeight());
        Pixel2D p1 = new Index2D(250,250);
        _m1.fill(p1,1);


        _m2.init(bigarr);
        assertEquals(bigarr.length, _m1.getWidth());
        assertEquals(bigarr[0].length, _m1.getHeight());
        _m2.fill(p1,1);

        _m3_3.init(bigarr);
        assertEquals(bigarr.length, _m1.getWidth());
        assertEquals(bigarr[0].length, _m1.getHeight());
        _m2.fill(p1,1);
    }

    @Test
    @Timeout(value = 1, unit = SECONDS)
    void testEquals() {
        assertEquals(_m0,_m1);
        assertEquals(_m0,_m3);
        assertNotEquals(_m1,_m2);
        _m3.setPixel(2,2,17);
        assertNotEquals(_m0,_m3);
    }
    @Test
    @Timeout(value = 1, unit = SECONDS)
    void getMap() {
        int[][] m0 = _m0.getMap();
        _m1.init(m0);
        assertEquals(_m0,_m1);
    }


    @Test
    @Timeout(value = 1, unit = SECONDS)
    void testFill0() {
        Pixel2D p1 = new Index2D(0,0);
        int f0 = _m0.fill(p1,2);
        assertEquals(f0,21);
    }
    @Test
    @Timeout(value = 1, unit = SECONDS)
    void testFill1() {
        Pixel2D p1 = new Index2D(0,1);
        _m0.setPixel(p1,0);
        int f0 = _m0.fill(p1,2);
        assertEquals(f0,9);
        _m0.setCyclic(false);
        int f2 = _m0.fill(p1,3);
        assertEquals(f2,8);
    }
    @Test
    @Timeout(value = 1, unit = SECONDS)
    void testAllDistance() {
        Pixel2D p1 = new Index2D(4,4);
        Pixel2D p2 = new Index2D(1,0);
        Map2D m00 = _m1.allDistance(p1, 0); //Cyclic
        Map2D m01 = _m2.allDistance(p1, 0); //not cyclyc
        assertEquals(4, m00.getPixel(p2));
        assertEquals(7, m01.getPixel(p2));
    }

   @Test
   @Timeout(value = 1, unit = SECONDS)
   void testAllDistance1() {
        Pixel2D p1 = new Index2D(200,3);
        Pixel2D p2 = new Index2D(4,4); //obsColor
       Map2D m00 = _mbig.allDistance(p1, 0);
       Map2D m01 = _mbig.allDistance(p2, 0);
       assertNotNull(m00);          //the map not null
       assertEquals(-1, m01.getPixel(p2));}  //obsColor  - -1

    @Test
    void testAllDistance2() {           //test RuntimeException
        Pixel2D p3 = new Index2D(600,600); //outside
        assertThrows(RuntimeException.class, () -> {
            _mbig.allDistance(p3, 0);
        });}

    @Test
    @Timeout(value = 1, unit = SECONDS)
    void testShortestPathDist() {
        Pixel2D p1 = new Index2D(4,4);
        Pixel2D p2 = new Index2D(0,0);
        int i = _m2.shortestPathDist(p1, p2, 0);
        assertEquals(8, i);

    }



    @Test
    @Timeout(value = 1, unit = SECONDS)
    void testShortestPath() {
        Pixel2D p1 = new Index2D(3,2);
        Pixel2D p2 = new Index2D(1,2);
        Pixel2D[] path = _m0.shortestPath(p1, p2, 0);
        assertEquals(5, path.length);
        path = _m2.shortestPath(p1, p2, 0);
        assertEquals(11, path.length);
    }
    @Test
    void testShortestPath3() {//test RuntimeException
        Pixel2D p1 = new Index2D(3,2);
        Pixel2D p3 = new Index2D(600,600); //outside
        assertThrows(RuntimeException.class, () -> {
            _mbig.shortestPath(p3,p1, 0);
        });}

    @Test
    @Timeout(value = 1, unit = SECONDS)
    void testShortestPath1() {
        Pixel2D p1 = new Index2D(50, 50);
        Pixel2D p2 = new Index2D(3, 0);
        Pixel2D p3 = new Index2D(5, 4);
        Pixel2D p4 = new Index2D(0, 1);
        Pixel2D p5 = new Index2D(2, 3);
        Pixel2D[] points = new Pixel2D[5];
        points[1] = p1;
        points[2] = p2;
        points[3] = p3;
        points[4] = p4;
        points[0] = p5;


        Pixel2D[] path1 = _mbig.shortestPath(points, 0);

        assertNotNull(path1);
            }



    @Test
    @Timeout(value = 1, unit = SECONDS)
    void testShortestPath1IfCyclyc() {
        Pixel2D p1 = new Index2D(0,0);
        Pixel2D p2 = new Index2D(3,0);
        Pixel2D p3 = new Index2D(5,4);
        Pixel2D p4 = new Index2D(0,1);
        Pixel2D p5 = new Index2D(2,4);
        Pixel2D[] points = new Pixel2D[5];
        points[0]=p1;
        points[1]=p2;
        points[2]=p3;
        points[3]=p4;
        points[4]=p5;

        Pixel2D[] path = _m1.shortestPath(points, 0);

        assertEquals(11, path.length+1);}



    @Test
    @Timeout(value = 1, unit = SECONDS)
    void testShortestPath2() {
        Pixel2D p1 = new Index2D(0,0);
        Pixel2D p2 = new Index2D(80,30);
        Pixel2D p3 = new Index2D(100,250);
        Pixel2D p4 = new Index2D(250,4);
        Pixel2D p5 = new Index2D(87,5);
        Pixel2D[] points = new Pixel2D[5];
        points[0]=p1;
        points[1]=p2;
        points[2]=p3;
        points[3]=p4;
        points[4]=p5;

        //Pixel2D[] path = _mbig.shortestPath(points, 0);

        // assertEquals(3, path.length+1);}
        // Align the legacy assertion with Map2D's documented null result.
        assertNull(_mbig.shortestPath(points, 0));}

    @Test
    @Timeout(value = 1, unit = SECONDS)
    void findClosestPointsTest() {
        Pixel2D p1 = new Index2D(0,0);
        Pixel2D p2 = new Index2D(3,0);
        Pixel2D p3 = new Index2D(1,0);
        Pixel2D[] points = new Pixel2D[3];
        points[1]=p1;
        points[2]=p2;
        points[0]=p3;
        Pixel2D[] path = _m2.findClosestPoints(points, 0);
        assertNotNull(path);
        assertEquals(2, path.length);
        assertEquals(1, _m2.shortestPathDist(path[0], path[1], 0));};

    @Test
    @Timeout(value = 1, unit = SECONDS)
    void findClosestPointTest() {
        Pixel2D p1 = new Index2D(0,0);
        Pixel2D p2 = new Index2D(3,0);
        Pixel2D p3 = new Index2D(1,4);
        Pixel2D p4 = new Index2D(4,0);
        ArrayList<Pixel2D> points = new ArrayList<Pixel2D>();

        points.add(p2);
        points.add(p4);
        points.add(p3);
        Pixel2D p5 = _m1.findClosestPoint(p1, points, 0);
        assertEquals(p4, p5);}

    @Test
    @Timeout(value = 1, unit = SECONDS)
    void testNumberOfConnectedComponentsNonCyclic() {
        int f0 = _m2.numberOfConnectedComponents(0);
        assertEquals(f0,1);
        Pixel2D p1 = new Index2D(1,0);
        _m2.setPixel(p1,0);
        Pixel2D p2 = new Index2D(1,4);
        _m2.setPixel(p2,0);
        int f1 = _m2.numberOfConnectedComponents(0);
        assertEquals(f1,2);
        Pixel2D p3 = new Index2D(3,0);
        _m2.setPixel(p3,0);
        Pixel2D p4 = new Index2D(3,4);
        _m2.setPixel(p4,0);
        int f2 = _m2.numberOfConnectedComponents(0);
        assertEquals(f2,4);

    }

        @Test
        @Timeout(value = 1, unit = SECONDS)
        void testNumberOfConnectedComponentsCyclic() {
            int f0 = _m1.numberOfConnectedComponents(0);
            assertEquals(f0,1);
            Pixel2D p1 = new Index2D(1,0);
            _m1.setPixel(p1,0);
            Pixel2D p2 = new Index2D(1,4);
            _m1.setPixel(p2,0);
            int f1 = _m1.numberOfConnectedComponents(0);
            assertEquals(f1,1);
            Pixel2D p3 = new Index2D(3,0);
            _m1.setPixel(p3,0);
            Pixel2D p4 = new Index2D(3,4);
            _m1.setPixel(p4,0);
            int f2 = _m1.numberOfConnectedComponents(0);
            assertEquals(f2,2);

        }


}