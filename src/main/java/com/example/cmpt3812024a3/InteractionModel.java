package com.example.cmpt3812024a3;

import java.util.ArrayList;

public class InteractionModel {

    private final ArrayList<Subscriber> subs;
    private Box selectedBox;
    private double worldSize = 2000;
    private double viewPortLeft, viewPortTop, viewPortWidth, viewPortHeight;
    private double handleRadius = 4;

    /**
     * Constructor for the InteractionModel Class
     */
    public InteractionModel() {
        selectedBox = null;
        subs = new ArrayList<>();
    }

    // ----------------- VIEWPORT ----------------- //

    /**
     * Move the viewport by the given values
     * @param dx x-axis movement
     * @param dy y-axis movement
     */
    public void moveViewPort(double dx, double dy) {

        double newViewPortLeft = viewPortLeft + dx;
        if (newViewPortLeft > 0) {
            viewPortLeft = 0;
        }
        else viewPortLeft = Math.max(newViewPortLeft, -worldSize + viewPortWidth);

        double newViewPortTop = viewPortTop + dy;
        if (newViewPortTop > 0) {
            viewPortTop = 0;
        }
        else viewPortTop = Math.max(newViewPortTop, -worldSize + viewPortHeight);
        notifySubscribers();
    }


    // ----------------- HANDLES ----------------- //

    public boolean onHandle(double mx, double my) {
        if (topLeftHandle(mx, my)) {
            System.out.println("topLeftHandle");
        }
        else if (topRightHandle(mx, my)) {
            System.out.println("topRightHandle");
        }
        else if (bottomLeftHandle(mx, my)) {
            System.out.println("bottomLeftHandle");
        }
        else if (bottomRightHandle(mx, my)) {
            System.out.println("bottomRightHandle");
        }
        return selectedBox != null && (topLeftHandle(mx,my) || topRightHandle(mx,my) || bottomLeftHandle(mx,my) || bottomRightHandle(mx,my));
    }

    public int whichHandle(double mx, double my) {
        if (topLeftHandle(mx, my)) {
            return 1;
        }
        else if (topRightHandle(mx, my)) {
            return 2;
        }
        else if (bottomLeftHandle(mx, my)) {
            return 3;
        }
        else if (bottomRightHandle(mx, my)) {
            return 4;
        }
        return 0;
    }

    public boolean topLeftHandle(double mx, double my) {
        double selX = selectedBox.getX();
        double selY = selectedBox.getY();
        return Math.hypot(selX - mx, selY - my) <= handleRadius;
    }

    public boolean topRightHandle(double mx, double my) {
        double selX = selectedBox.getX();
        double selY = selectedBox.getY();
        double selW = selectedBox.getW();
        return Math.hypot(selX + selW - mx, selY - my) <= handleRadius;
    }

    public boolean bottomLeftHandle(double mx, double my) {
        double selX = selectedBox.getX();
        double selY = selectedBox.getY();
        double selH = selectedBox.getH();
        return Math.hypot(selX - mx, selY + selH - my) <= handleRadius;
    }

    public boolean bottomRightHandle(double mx, double my) {
        double selX = selectedBox.getX();
        double selY = selectedBox.getY();
        double selW = selectedBox.getW();
        double selH = selectedBox.getH();
        return Math.hypot(selX + selW - mx, selY + selH - my) <= handleRadius;
    }


    // ----------------- GETTERS ----------------- //

    /**
     * Get the selected Box
     * @return selected box
     */
    public Box getSelectedBox() { return selectedBox; }

    /**
     * Returns the size of the world in pixels
     * @return world size
     */
    public double getWorldSize() { return worldSize; }

    /**
     * Returns the left offset of the viewport
     * @return left
     */
    public double getViewPortLeft() { return viewPortLeft; }

    /**
     * Returns the top offset of the viewport
     * @return top
     */
    public double getViewPortTop() { return viewPortTop; }

    /**
     * Returns the width of the viewport
     * @return top
     */
    public double getViewPortWidth() { return viewPortWidth; }

    /**
     * Returns the height of the viewport
     * @return top
     */
    public double getViewPortHeight() { return viewPortHeight; }

    /**
     * Returns the radius of the handle
     * @return handle radius value
     */
    public double getHandleRadius() { return handleRadius; }


    // ----------------- SETTERS ----------------- //

    /**
     * Set the given box as the selected box
     * @param selectedBox box to set as selected
     */
    public void setSelectedBox(Box selectedBox) { this.selectedBox = selectedBox; }

    /**
     * Sets the width of the viewport to the given value
     * @param newViewWidth value to set
     */
    public void setViewPortWidth(double newViewWidth) { this.viewPortWidth = newViewWidth; }

    /**
     * Sets the height of the viewport to the given value
     * @param newViewHeight value to set
     */
    public void setViewPortHeight(double newViewHeight) { this.viewPortHeight = newViewHeight; }

    /**
     * Sets the left offset of the viewport to the given value
     * @param newViewLeft value to set
     */
    public void setViewPortLeft(double newViewLeft) { this.viewPortLeft = newViewLeft; }

    /**
     * Sets the top offset of the viewport to the given value
     * @param newViewTop value to set
     */
    public void setViewPortTop(double newViewTop) { this.viewPortTop = newViewTop; }


    // ----------------- SUBSCRIBERS ----------------- //

    /**
     * Add a new subscriber to the model
     * @param sub new sub
     */
    public void addSubscriber(Subscriber sub) {
        subs.add(sub);
    }

    /**
     * Notify the subscribers about the change in the model
     */
    public void notifySubscribers() {
        subs.forEach(Subscriber::modelChanged);
    }

}
