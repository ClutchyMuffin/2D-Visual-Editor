package com.example.cmpt3812024a3;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class AppController {

    private EntityModel model;
    private InteractionModel imodel;
    private ControllerState currentState;
    private double dx, dy, dw, dh;
    private double prevX, prevY, correctedX, correctedY;

    /**
     * Constructor for the Controller class
     */
    public AppController() {
        currentState = ready;
    }


    // ----------------- SETTERS ----------------- //

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


    // ----------------- HANDLERS ----------------- //

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


    // ----------------- STATES ----------------- //

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


    // ----------------- READY STATE ----------------- //

    ControllerState ready = new ControllerState() {

        public void handlePressed(MouseEvent event) {
            prevX = event.getX();
            prevY = event.getY();

            correctedX = event.getX() - imodel.getViewPortLeft();
            correctedY = event.getY() - imodel.getViewPortTop();

            if (imodel.getSelectedBox() != null && imodel.onHandle(correctedX, correctedY)) {
                System.out.println("resizing");
                currentState = resizing;
            }

            if (model.contains(correctedX, correctedY)) {
                imodel.setSelectedBox(model.whichBox(correctedX, correctedY));
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


    // ----------------- DRAGGING STATE ----------------- //

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


    // ----------------- CREATE/UNSELECT STATE ----------------- //

    ControllerState create_or_unselect = new ControllerState() {

        public void handleDragged(MouseEvent event) {
            model.addBox(correctedX, correctedY, 0 ,0);
            imodel.setSelectedBox(model.whichBox(correctedX, correctedY));
            currentState = creating;
        }

        public void handleReleased(MouseEvent event) {
            imodel.setSelectedBox(null);
            model.notifySubscribers();
            currentState = ready;
        }

    };


    // ----------------- CREATING STATE ----------------- //

    ControllerState creating = new ControllerState() {

        public void handleDragged(MouseEvent event) {
            dx = event.getX() - prevX;
            dy = event.getY() - prevY;
            prevX = event.getX();
            prevY = event.getY();
            imodel.getSelectedBox().updateSize(dx, dy);
            model.notifySubscribers();
        }

        public void handleReleased(MouseEvent event) {
            currentState = ready;
        }

    };


    // ----------------- PANNING STATE ----------------- //

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

    // ----------------- RESIZING STATE ----------------- //

    ControllerState resizing = new ControllerState() {

        public void handleDragged(MouseEvent event) {
            double newX = event.getX() - imodel.getViewPortLeft();
            double newY = event.getY() - imodel.getViewPortTop();
            dx = newX - correctedX;
            dy = newY - correctedY;

            int handle = imodel.whichHandle(correctedX, correctedY);
            switch (handle) {
                case 1:
                    imodel.getSelectedBox().updatePosition(imodel.getSelectedBox().getX() + dx, imodel.getSelectedBox().getY() + dy);
                    imodel.getSelectedBox().setW(imodel.getSelectedBox().getW() - dx);
                    imodel.getSelectedBox().setH(imodel.getSelectedBox().getH() - dy);
                    break;

                case 2:
                    imodel.getSelectedBox().updatePosition(imodel.getSelectedBox().getX(), imodel.getSelectedBox().getY() + dy);
                    imodel.getSelectedBox().setW(imodel.getSelectedBox().getW() + dx);
                    imodel.getSelectedBox().setH(imodel.getSelectedBox().getH() - dy);
                    break;

                case 3:
                    imodel.getSelectedBox().updatePosition(imodel.getSelectedBox().getX() + dx, imodel.getSelectedBox().getY());
                    imodel.getSelectedBox().setW(imodel.getSelectedBox().getW() - dx);
                    imodel.getSelectedBox().setH(imodel.getSelectedBox().getH() + dy);
                    break;

                case 4:
                    imodel.getSelectedBox().updatePosition(imodel.getSelectedBox().getX(), imodel.getSelectedBox().getY());
                    imodel.getSelectedBox().setW(imodel.getSelectedBox().getW() + dx);
                    imodel.getSelectedBox().setH(imodel.getSelectedBox().getH() + dy);
                    break;
            }

        }

        public void handleReleased(MouseEvent event) {
            System.out.println("releasing");
            currentState = ready;
        }
    };
}
