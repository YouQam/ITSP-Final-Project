import java.util.ArrayList;

public class Polyline extends Shape{
    
    private Point[] points;
    private int numPoints;

    public int getNumPoints() {
        return numPoints;
    }
    public Point[] getPoints() {
        return points;
    }

    public void setPoints(Point[] points) {
        this.points = points;
    }
    public void setNumPoints(int numPoints) {
        this.numPoints = numPoints;
    }
}