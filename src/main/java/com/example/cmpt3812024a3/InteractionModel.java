package com.example.cmpt3812024a3;

import java.util.ArrayList;

public class InteractionModel {

    private final ArrayList<Subscriber> subs;
    private Box selectedBox;
    private double worldSize = 2000;
    private double viewPortLeft, viewPortTop, viewPortWidth, viewPortHeight;

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
        else if (newViewPortLeft < -worldSize + viewPortWidth) {
            viewPortLeft = -worldSize + viewPortWidth;
        }
        else {
            viewPortLeft = newViewPortLeft;
        }

        double newViewPortTop = viewPortTop + dy;
        if (newViewPortTop > 0) {
            viewPortTop = 0;
        }
        else if (newViewPortTop < -worldSize + viewPortHeight) {
            viewPortTop = -worldSize + viewPortHeight;
        }
        else {
            viewPortTop = newViewPortTop;
        }
        notifySubscribers();
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


    // ----------------- SETTERS ----------------- //

    /**
     * Set the given box as the selected box
     * @param selectedBox box to set as selected
     */
    public void setSelectedBox(Box selectedBox) { this.selectedBox = selectedBox; }

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
