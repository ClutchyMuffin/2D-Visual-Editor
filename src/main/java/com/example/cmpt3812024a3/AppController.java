package com.example.cmpt3812024a3;

import javafx.scene.input.MouseEvent;

public class AppController {

    private EntityModel model;
    private InteractionModel imodel;
    private ControllerState currentState;
    private double prevX, prevY, dx, dy, dw, dh;

    /**
     * Constructor for the Controller class
     */
    public AppController() {
        currentState = ready;
    }

    /**
     * Set the model
     * @param m model
     */
    public void setModel(EntityModel m) { this.model = m; }

    /**
     * Set the imodel
     * @param im imodel
     */
    public void setIModel(InteractionModel im) { this.imodel = im; }

    /**
     * Handle the Mouse Pressed event
     * @param event mouse event
     */
    public void handlePressed(MouseEvent event) { currentState.handlePressed(event); }

    /**
     * Handle the Mouse Dragged event
     * @param event mouse event
     */
    public void handleDragged(MouseEvent event) { currentState.handleDragged(event); }

    /**
     * Handle the Mouse Released event
     * @param event mouse event
     */
    public void handleReleased(MouseEvent event) { currentState.handleReleased(event); }

    /**
     * Public class that defines the controller state & methods
     */
    public abstract static class ControllerState {
        void handlePressed(MouseEvent event) { }
        void handleDragged(MouseEvent event) { }
        void handleReleased(MouseEvent event) { }
    }

    /**
     * READY state
     */
    ControllerState ready = new ControllerState() {

        public void handlePressed(MouseEvent event) {
            prevX = event.getX();
            prevY = event.getY();

            if (model.contains(event.getX(), event.getY())) {
                imodel.setSelectedBox(model.whichBox(event.getX(), event.getY()));
                currentState = dragging;
            }
            else {
                currentState = create_or_unselect;
            }
        }
    };

    /**
     * DRAGGING state
     */
    ControllerState dragging = new ControllerState() {

        public void handleDragged(MouseEvent event) { }

        public void handleReleased(MouseEvent event) { }

    };

    /**
     * CREATE OR UNSELECT state
     */
    ControllerState create_or_unselect = new ControllerState() {

        public void handleDragged(MouseEvent event) { }

        public void handleReleased(MouseEvent event) { }

    };

    /**
     * CREATING state
     */
    ControllerState creating = new ControllerState() {

        public void handleDragged(MouseEvent event) { };

        public void handleReleased(MouseEvent event) { }

    };



}