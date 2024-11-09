package com.example.cmpt3812024a3;

import java.util.ArrayList;

public class InteractionModel {

    private final ArrayList<Subscriber> subs;
    private Box selectedBox;
    private double worldSize = 2000;
    private double viewLeft, viewTop;

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
        viewLeft += dx;
        viewTop += dy;
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
     * Returns the left offset
     * @return left
     */
    public double getViewLeft() { return viewLeft; }

    /**
     * Returns the top offset
     * @return top
     */
    public double getViewTop() { return viewTop; }


    // ----------------- SETTERS ----------------- //

    /**
     * Set the given box as the selected box
     * @param selectedBox box to set as selected
     */
    public void setSelectedBox(Box selectedBox) { this.selectedBox = selectedBox; }

    /**
     * Sets the left offset to the given value
     * @param viewLeft value to set
     */
    public void setViewLeft(double viewLeft) { this.viewLeft = viewLeft; }

    /**
     * Sets the top offset to the given value
     * @param viewTop value to set
     */
    public void setViewTop(double viewTop) { this.viewTop = viewTop; }


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
