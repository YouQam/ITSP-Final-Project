package shapes;

import java.awt.Color;

public abstract class Shape {
    
    private float strokeWidth;
    private Color strokeColor;
    private Color fillColor;
    private float transparency;

    public float getStrokeWidth(){
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public Color getStrokeColor(){
        return strokeColor;
    }

    public void setStrokeColor(Color strokeColor){
        this.strokeColor = strokeColor;
    } 

    public Color getFillColor(){
        return fillColor;
    }

    public void setFillColor(Color fillColor){
        this.fillColor = fillColor;
    }

    public float getTransparency() {
        return transparency;
    }

    public void setTransparency(float transparency) {
        this.transparency = transparency;
    }
}