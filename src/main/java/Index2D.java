import java.io.Serializable;

public class Index2D implements Pixel2D, Serializable{
    private int _x, _y;
    public Index2D() {this(0,0);}
    public Index2D(int x, int y) {_x=x;_y=y;}
    public Index2D(double x, double y) {
        _x=(int)x;
        _y=(int)y;
    }
    public Index2D(Pixel2D t) {this(t.getX(), t.getY());}

    //ishay
    public Index2D(String pos) { //"1,2" - -1,2
        String [] split=pos.split (",");
        this._x=Integer.parseInt(split[0]);
        this._y=Integer.parseInt(split[1]);



    }

    @Override
    public int getX() {
        return _x;
    }

    @Override
    public int getY() {
        return _y;
    }

    //ishay
    public double distance2D(Pixel2D t) {
        double ans = -1;
        double x = Math.abs(t.getX()-this.getX());
        double y = Math.abs(t.getY()-this.getY());
        ans = Math.sqrt(x*x+y*y);
        return ans;
    }
    @Override           //ishay
    public String toString() {//1,2       String ans = "";

        int x = this.getX();
        int y = this.getY();
        String values = "("+ x + "," + y + ")";
        String ans = values;

        return ans;
    }
    @Override           //ishay
    public boolean equals(Object t) {
        if(!(t instanceof Index2D temp)){
            return false;
        }

        return temp._x == this._x && temp._y == this._y;
    }
}
