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

    // ----------------- BOXES ----------------- //

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

    // ----------------- ENTITY CHECKS ----------------- //

    /**
     * Check if any box matches the given coordinates
     * @param mx x-coordinate
     * @param my y-coordinate
     * @return true or false
     */
    public boolean contains(double mx, double my) {
        return boxes.stream().anyMatch(box -> box.contains(mx,my));
    }

    /**
     * Return the box that matches the given coordinates
     * @param mx x-coordinate
     * @param my y-coordinate
     * @return the box that matches given coordinates
     */
    public Box whichBox(double mx, double my) {
        return boxes.stream()
                .filter(box -> box.contains(mx,my))
                .findFirst()
                .orElse(null);
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
