package com.example.cmpt3812024a3;

public class Box {

    private double x;
    private double y;
    private double width;
    private double height;

    public Box(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Check if given values lie within the box
     * @param mx x-coordinate
     * @param my y-coordinate
     * @return true or false
     */
    public boolean contains(double mx, double my) {
        return (mx >= this.x) && (mx <= this.x + this.width) &&
                (my >= this.y) && (my <= this.y + this.height) ;
    }

    /**
     * Move the box by the given values
     * @param dx x-coordinate
     * @param dy y-coordinate
     */
    public void move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }

    // Setters
    public void setX(double nx) { this.x = nx; }
    public void setY(double ny) { this.y = ny; }
    public void setWidth(double nwidth) { this.width = nwidth; }
    public void setHeight(double nheight) { this.height = nheight; }

}
