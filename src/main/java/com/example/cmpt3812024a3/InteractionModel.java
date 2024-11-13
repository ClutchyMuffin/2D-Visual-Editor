package com.example.cmpt3812024a3;

import java.util.ArrayList;

public class InteractionModel {

    private final ArrayList<Subscriber> subs;
    private Box selectedBox;
    private final double worldSize = 2000;
    private double viewPortLeft, viewPortTop, viewPortWidth, viewPortHeight;
    private final double handleRadius = 5;

    /**
     * Constructor for the InteractionModel Class
     */
    public InteractionModel() {
        viewPortLeft = 0;
        viewPortTop = 0;
        viewPortWidth = 800;
        viewPortHeight = 800;
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

        double newViewPortLeft, newViewPortTop;

        newViewPortLeft = viewPortLeft + dx;
        if (newViewPortLeft + viewPortWidth > worldSize) {
            newViewPortLeft = worldSize - viewPortWidth;
            setViewPortLeft(newViewPortLeft);
        }
        else if (newViewPortLeft < 0) {
            setViewPortLeft(0);
        }

        newViewPortTop = viewPortTop + dy;
        if (newViewPortTop + viewPortHeight > worldSize) {
            newViewPortTop = worldSize - viewPortHeight;
            setViewPortTop(newViewPortTop);
        }
        else if (newViewPortTop < 0) {
            setViewPortTop(0);
        }

        viewPortLeft += dx;
        viewPortTop += dy;
        notifySubscribers();
    }


    // ----------------- HANDLES ----------------- //

    /**
     * Return an integer to denote which handle is clicked
     * @param mx x-coordinate
     * @param my y-coordinate
     * @return 1 = top-left, 2 = top-right, 3 = bottom-left, 4 = bottom-right
     */
    public int onHandle(double mx, double my) {

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

    /**
     * Check if the top-left handle is clicked
     * @param mx x-coordinate
     * @param my y-coordinate
     * @return true if clicked, else false
     */
    public boolean topLeftHandle(double mx, double my) {
        return Math.hypot(selectedBox.getX() - mx,
                          selectedBox.getY() - my) <= handleRadius;
    }

    /**
     * Check if the top-right handle is clicked
     * @param mx x-coordinate
     * @param my y-coordinate
     * @return true if clicked, else false
     */
    public boolean topRightHandle(double mx, double my) {
        return Math.hypot(selectedBox.getX() + selectedBox.getWidth() - mx,
                          selectedBox.getY() - my) <= handleRadius;

    }

    /**
     * Check if the bottom-left handle is clicked
     * @param mx x-coordinate
     * @param my y-coordinate
     * @return true if clicked, else false
     */
    public boolean bottomLeftHandle(double mx, double my) {
        return Math.hypot(selectedBox.getX() - mx,
                          selectedBox.getY() + selectedBox.getHeight() - my) <= handleRadius;

    }

    /**
     * Check if the bottom-right handle is clicked
     * @param mx x-coordinate
     * @param my y-coordinate
     * @return true if clicked, else false
     */
    public boolean bottomRightHandle(double mx, double my) {
        return Math.hypot(selectedBox.getX() + selectedBox.getWidth() - mx,
                          selectedBox.getY() + selectedBox.getHeight() - my) <= handleRadius;
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
    public void setSelectedBox(Box selectedBox) {
        this.selectedBox = selectedBox;
        notifySubscribers();
    }

    /**
     * Sets the width of the viewport to the given value
     * @param newViewWidth value to set
     */
    public void setViewPortWidth(double newViewWidth) {
        this.viewPortWidth = newViewWidth;
        notifySubscribers();
    }

    /**
     * Sets the height of the viewport to the given value
     * @param newViewHeight value to set
     */
    public void setViewPortHeight(double newViewHeight) {
        this.viewPortHeight = newViewHeight;
        notifySubscribers();
    }

    /**
     * Sets the left offset of the viewport to the given value
     * @param newViewLeft value to set
     */
    public void setViewPortLeft(double newViewLeft) {
        this.viewPortLeft = newViewLeft;
        notifySubscribers();
    }

    /**
     * Sets the top offset of the viewport to the given value
     * @param newViewTop value to set
     */
    public void setViewPortTop(double newViewTop) {
        this.viewPortTop = newViewTop;
        notifySubscribers();
    }


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
