public class Rectangle extends Shape{
    
    private Point topLeftPoint;
    private float width;
    private float height;
    
    public float area(){
        return height*width;
    }

    public float perimiter(){
        return 2*(height+width);
    }

    public boolean contains(Point point){
        return (point.getX()>=topLeftPoint.getX() && point.getX()<= width && point.getY()>=topLeftPoint.getY() && point.getY()<= this.height) ? true : false;
    }

     public Point centroid(){
        Point p = new Point();
        p.setX(topLeftPoint.getX()+(this.width/2));
        p.setY(topLeftPoint.getY()+(this.height/2));
        return p;
    } 

    public String toString() {
        return " topLeftPoint(" + topLeftPoint.getX() +","+ topLeftPoint.getY() +"), width="+width+"height="+height;
    }

    public float getWidth(){
        return width;
    }

    public float getHeight(){
        return height;
    }

    public Point getTopLeftPoint(){
        return topLeftPoint;
    }

    public void setWidth(float width){
        this.width = width;
    }

    public void setHeight(float height){
        this.height = height;
    }

    public void setTopLeftPoint(Point topLeftPoint){
        this.topLeftPoint = topLeftPoint;
    }
}