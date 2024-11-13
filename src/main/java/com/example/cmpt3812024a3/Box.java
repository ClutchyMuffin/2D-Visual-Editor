/**
 * Name = Sayed Farzaan Rafi Bhat
 * NSID = kfn036
 * Stu# = 11356043
 */

package com.example.cmpt3812024a3;

public class Box {

    private double x;
    private double y;
    private double width;
    private double height;

    /**
     * Constructor of the Box Class
     * @param x top-left
     * @param y top-right
     * @param width width
     * @param height height
     */
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

    // ----------------- ADDERS ----------------- //
    public void addX(double x) { this.x += x; }
    public void addY(double y) { this.y += y; }
    public void addWidth(double newWidth) { width += newWidth; }
    public void addHeight(double newHeight) { height += newHeight;  }

    // ----------------- GETTERS ----------------- //
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }

    // ----------------- SETTERS ----------------- //
    public void setX(double nx) { this.x = nx; }
    public void setY(double ny) { this.y = ny; }
    public void setWidth(double nw) { this.width = nw; }
    public void setHeight(double nh) { this.height = nh; }

}
