package shapes;

public class Label extends Shape{
    
    private Point position = new Point();
    private String text;


    public Point getPosition() {
        return position;
    }

    public String getText() {
        return text;
    }


    public void setText(String text) {
        this.text = text;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
}