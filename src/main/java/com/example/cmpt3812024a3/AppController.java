package com.example.cmpt3812024a3;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class AppController {

    private EntityModel model;
    private InteractionModel imodel;
    private ControllerState currentState;
    private double prevX, prevY, dx, dy, adjustedX, adjustedY;

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
    public void setModel(EntityModel m) {
        this.model = m;
    }

    /**
     * Set the imodel
     * @param im imodel
     */
    public void setIModel(InteractionModel im) {
        this.imodel = im;
    }

    /**
     * Handle the Mouse Pressed event
     * @param event mouse event
     */
    public void handlePressed(MouseEvent event) {
        currentState.handlePressed(event);
    }

    /**
     * Handle the Mouse Dragged event
     * @param event mouse event
     */
    public void handleDragged(MouseEvent event) {
        currentState.handleDragged(event);
    }

    /**
     * Handle the Mouse Released event
     * @param event mouse event
     */
    public void handleReleased(MouseEvent event) {
        currentState.handleReleased(event);
    }

    /**
     * Handle the Key Pressed event
     * @param event key event
     */
    public void handleKeyPressed(KeyEvent event) {
        currentState.handleKeyPressed(event);
    }

    /**
     * Handle the Key Released event
     * @param event key event
     */
    public void handleKeyReleased(KeyEvent event) {
        currentState.handleKeyReleased(event);
    }

    /**
     * Public class that defines the controller state & methods
     */
    public abstract static class ControllerState {
        void handlePressed(MouseEvent event) { }
        void handleDragged(MouseEvent event) { }
        void handleReleased(MouseEvent event) { }
        void handleKeyPressed(KeyEvent event) { }
        void handleKeyReleased(KeyEvent event) { }
    }

    /**
     * READY state
     */
    ControllerState ready = new ControllerState() {

        public void handlePressed(MouseEvent event) {
            prevX = event.getX();
            prevY = event.getY();

            adjustedX = event.getX() - imodel.getViewLeft();
            adjustedY = event.getY() - imodel.getViewTop();

            if (model.contains(adjustedX, adjustedY)) {
                imodel.setSelectedBox(model.whichBox(adjustedX, adjustedY));
                model.notifySubscribers();
                currentState = dragging;
            }
            else {
                currentState = create_or_unselect;
            }
        }

        public void handleKeyPressed(KeyEvent event) {
            switch (event.getCode()) {
                case DELETE:
                case BACK_SPACE:
                    if (imodel.getSelectedBox() != null) {
                        model.removeBox(imodel.getSelectedBox());
                        imodel.setSelectedBox(null);
                    }
                    break;
                case SHIFT:
                    currentState = panning;
                default:
                    break;
            }
        }
    };

    /**
     * DRAGGING state
     */
    ControllerState dragging = new ControllerState() {

        public void handleDragged(MouseEvent event) {
            dx = event.getX() - prevX;
            dy = event.getY() - prevY;
            prevX = event.getX();
            prevY = event.getY();
            model.moveBox(imodel.getSelectedBox(), dx, dy);
        }

        public void handleReleased(MouseEvent event) {
            currentState = ready;
        }

    };

    /**
     * CREATE OR UNSELECT state
     */
    ControllerState create_or_unselect = new ControllerState() {

        public void handleDragged(MouseEvent event) {
            model.addBox(adjustedX, adjustedY, 0 ,0);
            imodel.setSelectedBox(model.whichBox(adjustedX, adjustedY));
            currentState = creating;
        }

        public void handleReleased(MouseEvent event) {
            imodel.setSelectedBox(null);
            model.notifySubscribers();
            currentState = ready;
        }

    };

    /**
     * CREATING state
     */
    ControllerState creating = new ControllerState() {

        public void handleDragged(MouseEvent event) {
            dx = event.getX() - prevX;
            dy = event.getY() - prevY;
            prevX = event.getX();
            prevY = event.getY();
            imodel.getSelectedBox().update(dx, dy);
            model.notifySubscribers();
        }

        public void handleReleased(MouseEvent event) {
            currentState = ready;
        }

    };

    /**
     * PANNING state
     */
    ControllerState panning = new ControllerState() {

        public void handlePressed(MouseEvent event) {
            prevX = event.getX();
            prevY = event.getY();
        }

        public void handleDragged(MouseEvent event) {
            dx = event.getX() - prevX;
            dy = event.getY() - prevY;
            prevX = event.getX();
            prevY = event.getY();
            imodel.moveViewPort(dx, dy);
        }

        public void handleKeyReleased(KeyEvent event) {
            currentState = ready;
        }
    };
}
