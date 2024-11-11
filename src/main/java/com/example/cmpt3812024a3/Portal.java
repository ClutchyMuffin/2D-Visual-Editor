package com.example.cmpt3812024a3;

public class Portal extends Box {
    private double scaleFactor;
    private double portalLeft, portalTop;

    public Portal(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.scaleFactor = 0.5;
        this.portalLeft = 0;
        this.portalTop = 0;
    }

    // ----------------- ADDERS ----------------- //
    public void addX(double dx) {
        super.addX(dx);
        this.portalLeft += dx;
    }
    public void addY(double dy) {
        super.addY(dy);
        this.portalTop += dy;
    }

    // ----------------- GETTERS ----------------- //
    public double getScaleFactor() { return scaleFactor; }
    public double getPortalLeft() { return portalLeft; }
    public double getPortalTop() { return portalTop; }

    // ----------------- SETTERS ----------------- //
    public void setScaleFactor(double scaleFactor) { this.scaleFactor = scaleFactor; }
    public void setPortalLeft(double portalLeft) { this.portalLeft = portalLeft; }
    public void setPortalTop(double portalTop) { this.portalTop = portalTop; }

    public void setX(double nx) {
        super.setX(nx);
        this.portalLeft = nx;
    }
    public void setY(double ny) {
        super.setY(ny);
        this.portalTop = ny;
    }

}
