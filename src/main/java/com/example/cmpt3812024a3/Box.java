package com.example.cmpt3812024a3;

public class Box {

    private double x;
    private double y;
    private double w;
    private double h;

    /**
     * Constructor of the Box Class
     * @param x top-left
     * @param y top-right
     * @param w width
     * @param h height
     */
    public Box(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    /**
     * Check if given values lie within the box
     * @param mx x-coordinate
     * @param my y-coordinate
     * @return true or false
     */
    public boolean contains(double mx, double my) {
        return (mx >= this.x) && (mx <= this.x + this.w) &&
                (my >= this.y) && (my <= this.y + this.h) ;
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

    public void updateSize(double dw, double dh) {
        this.w += dw;
        this.h += dh;
    }

    public void updatePosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // ----------------- GETTERS ----------------- //
    public double getX() { return x; }
    public double getY() { return y; }
    public double getW() { return w; }
    public double getH() { return h; }

    // ----------------- SETTERS ----------------- //
    public void setX(double nx) { this.x = nx; }
    public void setY(double ny) { this.y = ny; }
    public void setW(double nw) { this.w = nw; }
    public void setH(double nh) { this.h = nh; }

}
