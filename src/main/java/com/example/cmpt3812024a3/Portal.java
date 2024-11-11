package com.example.cmpt3812024a3;

public class Portal extends Box {
    private double scaleFactor;
    private double viewPortLeft, viewPortTop;

    public Portal(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.scaleFactor = 0.5;
        this.viewPortLeft = 0;
        this.viewPortTop = 0;
    }

    public double getScaleFactor() { return scaleFactor; }
    public void setScaleFactor(double scaleFactor) { this.scaleFactor = scaleFactor; }

    public double getViewPortLeft() { return viewPortLeft; }
    public void setViewPortLeft(double viewPortLeft) { this.viewPortLeft = viewPortLeft; }

    public double getViewPortTop() { return viewPortTop; }
    public void setViewPortTop(double viewPortTop) { this.viewPortTop = viewPortTop; }

}
