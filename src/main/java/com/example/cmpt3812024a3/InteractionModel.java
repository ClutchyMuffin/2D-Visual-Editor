package com.example.cmpt3812024a3;

public class InteractionModel {

    private Box selectedBox;

    /**
     * Constructor for the InteractionModel Class
     */
    public InteractionModel() {
        selectedBox = null;
    }

    /**
     * Get the selected Box
     * @return selected box
     */
    public Box getSelectedBox() {
        return selectedBox;
    }

    /**
     * Set the given box as the selected box
     * @param selectedBox box to set as selected
     */
    public void setSelectedBox(Box selectedBox) {
        this.selectedBox = selectedBox;
    }


}
