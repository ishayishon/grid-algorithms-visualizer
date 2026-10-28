import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BehaviorCheckTest {
    private static int[][] distances(int[][] a, int sx, int sy, boolean cyclic) {
        int w=a.length, h=a[0].length;
        int[][] d=new int[w][h];
        for(int[] row:d) Arrays.fill(row,-1);
        if(a[sx][sy]==0) return d;
        ArrayDeque<int[]> q=new ArrayDeque<>();
        q.add(new int[]{sx,sy}); d[sx][sy]=0;
        while(!q.isEmpty()) {
            int[] p=q.remove();
            for(int[] step:new int[][]{{1,0},{-1,0},{0,1},{0,-1}}) {
                int x=p[0]+step[0],y=p[1]+step[1];
                if(cyclic) {x=Math.floorMod(x,w);y=Math.floorMod(y,h);}
                if(x<0||x>=w||y<0||y>=h||a[x][y]==0||d[x][y]>=0) continue;
                d[x][y]=d[p[0]][p[1]]+1;q.add(new int[]{x,y});
            }
        }
        return d;
    }
    private static int[][] randomMap(Random r) {
        int[][] a=new int[3+r.nextInt(5)][3+r.nextInt(5)];
        for(int[] row:a) for(int y=0;y<row.length;y++) row[y]=r.nextInt(4);
        return a;
    }
    private static boolean adjacent(Pixel2D a,Pixel2D b,int w,int h,boolean cyclic) {
        int dx=Math.abs(a.getX()-b.getX()),dy=Math.abs(a.getY()-b.getY());
        if(cyclic){dx=Math.min(dx,w-dx);dy=Math.min(dy,h-dy);}
        return dx+dy==1;
    }
    @Test void shortestDistanceMapsMatchIndependentBfs() {
        Random r=new Random(20261028);
        for(boolean cyclic:new boolean[]{false,true}) for(int run=0;run<100;run++) {
            int[][] a=randomMap(r);int sx=r.nextInt(a.length),sy=r.nextInt(a[0].length);
            Map m=new Map(a);m.setCyclic(cyclic);
            Map2D actual=m.allDistance(new Index2D(sx,sy),0);
            int[][] expected=distances(a,sx,sy,cyclic);
            for(int x=0;x<a.length;x++) for(int y=0;y<a[0].length;y++)
                assertEquals(expected[x][y],actual.getPixel(x,y),"cyclic="+cyclic+" run="+run);
        }
    }
    @Test void twoPointPathsAreShortestAndGeometricallyValid() {
        Random r=new Random(37);
        for(boolean cyclic:new boolean[]{false,true}) for(int run=0;run<100;run++) {
            int[][] a=randomMap(r);int w=a.length,h=a[0].length;
            Pixel2D start=new Index2D(r.nextInt(w),r.nextInt(h));
            Pixel2D end=new Index2D(r.nextInt(w),r.nextInt(h));
            Map m=new Map(a);m.setCyclic(cyclic);
            int expected=distances(a,start.getX(),start.getY(),cyclic)[end.getX()][end.getY()];
            Pixel2D[] path=m.shortestPath(start,end,0);
            if(expected<0){assertNull(path);continue;}
            assertNotNull(path);assertEquals(expected+1,path.length);
            assertTrue((path[0].equals(start)&&path[path.length-1].equals(end)) ||
                       (path[0].equals(end)&&path[path.length-1].equals(start)));
            for(int i=0;i<path.length;i++) {
                assertNotEquals(0,m.getPixel(path[i]));
                if(i>0)assertTrue(adjacent(path[i-1],path[i],w,h,cyclic));
            }
        }
    }
    @Test void floodFillMatchesIndependentConnectedRegion() {
        Random r=new Random(19);
        for(boolean cyclic:new boolean[]{false,true}) for(int run=0;run<100;run++) {
            int[][] a=randomMap(r);int sx=r.nextInt(a.length),sy=r.nextInt(a[0].length),color=a[sx][sy];
            int[][] binary=new int[a.length][a[0].length];
            for(int x=0;x<a.length;x++)for(int y=0;y<a[0].length;y++)binary[x][y]=a[x][y]==color?1:0;
            int[][] d=distances(binary,sx,sy,cyclic);int expectedCount=0;
            Map m=new Map(a);m.setCyclic(cyclic);
            for(int[] row:d)for(int v:row)if(v>=0)expectedCount++;
            assertEquals(expectedCount,m.fill(new Index2D(sx,sy),9));
            for(int x=0;x<a.length;x++)for(int y=0;y<a[0].length;y++)
                assertEquals(d[x][y]>=0?9:a[x][y],m.getPixel(x,y));
            assertEquals(0,m.fill(new Index2D(sx,sy),9));
        }
    }
    @Test void connectedComponentCountsMatchIndependentBfs() {
        Random r=new Random(95);
        for(boolean cyclic:new boolean[]{false,true}) for(int run=0;run<100;run++) {
            int[][] a=randomMap(r);boolean[][] seen=new boolean[a.length][a[0].length];int count=0;
            for(int x=0;x<a.length;x++)for(int y=0;y<a[0].length;y++) {
                if(a[x][y]==0||seen[x][y])continue;
                count++;int[][] d=distances(a,x,y,cyclic);
                for(int xx=0;xx<a.length;xx++)for(int yy=0;yy<a[0].length;yy++)if(d[xx][yy]>=0)seen[xx][yy]=true;
            }
            Map m=new Map(a);m.setCyclic(cyclic);
            assertEquals(count,m.numberOfConnectedComponents(0),"cyclic="+cyclic+" run="+run);
        }
    }
    @Test void multiPointRoutesAreContinuousAndVisitAllTargets() {
        Random r=new Random(41);
        for(boolean cyclic:new boolean[]{false,true})for(int run=0;run<100;run++) {
            int[][] a=randomMap(r);a[0][0]=1;int[][] d=distances(a,0,0,cyclic);
            ArrayList<Pixel2D> reachable=new ArrayList<>();
            for(int x=0;x<a.length;x++)for(int y=0;y<a[0].length;y++)if(d[x][y]>=0)reachable.add(new Index2D(x,y));
            if(reachable.size()<3)continue;
            java.util.Collections.shuffle(reachable,r);
            Pixel2D[] points=reachable.subList(0,Math.min(5,reachable.size())).toArray(Pixel2D[]::new);
            Map m=new Map(a);m.setCyclic(cyclic);Pixel2D[] path=m.shortestPath(points,0);
            assertNotNull(path);
            String context="cyclic="+cyclic+" run="+run+" targets="+Arrays.toString(points)+" route="+Arrays.toString(path);
            for(Pixel2D p:points)assertTrue(Arrays.asList(path).contains(p),context);
            for(int i=0;i<path.length;i++) {
                assertNotEquals(0,m.getPixel(path[i]),context);
                if(i>0)assertTrue(adjacent(path[i-1],path[i],a.length,a[0].length,cyclic),context);
            }
        }
    }
    @Test void twoPointRouteFollowsDocumentedStartAndEndOrder() {
        Map m=new Map(3,3,1);m.setCyclic(false);
        Pixel2D start=new Index2D(0,0),end=new Index2D(2,2);
        Pixel2D[] path=m.shortestPath(start,end,0);
        assertEquals(start,path[0],"First path coordinate should be the documented start p1");
        assertEquals(end,path[path.length-1]);
    }
    @Test void unreachableMultiPointRouteReturnsNullAsDocumented() {
        Map m=new Map(new int[][]{{1,0,1},{1,0,1},{1,0,1}});m.setCyclic(false);
        assertNull(m.shortestPath(new Pixel2D[]{new Index2D(0,0),new Index2D(0,2)},0));
    }
    @Test void tooFewTargetsReturnNullAsDocumented() {
        Map m=new Map(3,3,1);
        assertNull(m.shortestPath(new Pixel2D[]{new Index2D(0,0)},0));
    }
    @Test void invalidTargetInputsReturnNull() {
        Map m=new Map(3,3,1);
        assertNull(m.shortestPath((Pixel2D[])null,0));
        assertNull(m.shortestPath(new Pixel2D[0],0));
        assertNull(m.shortestPath(new Pixel2D[]{new Index2D(0,0),null},0));
        assertNull(m.shortestPath(new Pixel2D[]{new Index2D(0,0),new Index2D(9,9)},0));
        m.setPixel(1,1,0);
        assertNull(m.shortestPath(new Pixel2D[]{new Index2D(0,0),new Index2D(1,1)},0));
    }
    @Test void repeatedTargetsDoNotBreakRouteContinuity() {
        Map m=new Map(4,4,1);m.setCyclic(false);
        Pixel2D[] points={new Index2D(0,0),new Index2D(0,0),new Index2D(3,3),new Index2D(0,0)};
        Pixel2D[] path=m.shortestPath(points,0);
        assertNotNull(path);
        for(Pixel2D point:points)assertTrue(Arrays.asList(path).contains(point));
        for(int i=1;i<path.length;i++)assertTrue(adjacent(path[i-1],path[i],4,4,false));
    }
}
