
public class Point extends Shape{
    
    private double x;
    private double y;
    
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public String toString() {
        return " (" + this.x +","+ this.y +")";
    }
    
    public double distance(double x, double y){
        return Math.sqrt(Math.pow(this.x-x,2)+Math.pow(this.y-y,2));
    }

    public double getX(){
        return x;
    }

    public void setX(double x){
        this.x = x;
    }

    public double getY(){
        return y;
    }

    public void setY(double y){
        this.y = y;
    }
}