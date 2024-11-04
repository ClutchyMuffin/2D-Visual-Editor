package com.example.cmpt3812024a3;

import java.util.ArrayList;

public class EntityModel {

    private final ArrayList<Box> boxes;
    private final ArrayList<Subscriber> subs;

    /**
     * Constructor for the Model class
     */
    public EntityModel() {
        boxes = new ArrayList<>();
        subs = new ArrayList<>();
    }

    /**
     * Retrieve the list of boxes in the model
     * @return arraylist of boxes
     */
    public ArrayList<Box> getBoxes() { return this.boxes; }

    /**
     * Add a new box to the models list
     * @param nx x-coordinate of new box
     * @param ny y-coordinate of new box
     * @param nw width of new box
     * @param nh height of new box
     */
    public void addBox(double nx, double ny, double nw, double nh) {
        boxes.add(new Box(nx, ny, nw, nh));
        notifySubscribers();
    }

    /**
     * Remove the box from the models list
     * @param box box to remove
     */
    public void removeBox(Box box) {
        boxes.remove(box);
        notifySubscribers();
    }

    /**
     * Move a box by the given values
     * @param box box to move
     * @param dx x-value to move
     * @param dy y-value to move
     */
    public void moveBox(Box box, double dx, double dy) {
        box.move(dx, dy);
        notifySubscribers();
    }

    /**
     * Check if any box matches the given coordinates
     * @param x x-coordinate
     * @param y y-coordinate
     * @return true or false
     */
    public boolean contains(double x, double y) {
        return boxes.stream().anyMatch(box -> box.contains(x,y));
    }

    /**
     * Return the box that matches the given coordinates
     * @param x x-coordinate
     * @param y y-coordinate
     * @return the box that matches given coordinates
     */
    public Box whichBox(double x, double y) {
        return boxes.stream()
                .filter(box -> box.contains(x,y))
                .findFirst()
                .orElse(null);
    }

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
