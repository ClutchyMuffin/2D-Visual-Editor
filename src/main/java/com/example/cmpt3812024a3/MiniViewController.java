package com.example.cmpt3812024a3;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class MiniViewController {

    private EntityModel model;
    private InteractionModel iModel;
    private ControllerState currentState;
    private double prevX, prevY;
    private double scale;

    public MiniViewController() {
        currentState = ready;
    }

    // ----------------- SETTERS ----------------- //

    /**
     * Set the model
     * @param model model
     */
    public void setModel(EntityModel model) { this.model = model; }

    /**
     * Set the imodel
     * @param iModel imodel
     */
    public void setiModel(InteractionModel iModel) { this.iModel = iModel; }

    /**
     * Set the scale of the miniView
     * @param scale scale
     */
    public void setScale(double scale) { this.scale = scale; }


    // ----------------- HANDLERS ----------------- //

    /**
     * Handle the Mouse Pressed event
     * @param event mouse event
     */
    public void handlePressed(MouseEvent event) { currentState.handlePressed(event); }

    /**
     * Handle the Mouse Dragged event
     * @param event mouse event
     */
    public void handleReleased(MouseEvent event) { currentState.handleReleased(event); }

    /**
     * Handle the Mouse Released event
     * @param event mouse event
     */
    public void handleDragged(MouseEvent event) { currentState.handleDragged(event); }


    // ----------------- STATES ----------------- //

    /**
     * Public class that defines the controller state & methods
     */
    public abstract static class ControllerState {
        void handlePressed(MouseEvent event) {}
        void handleDragged(MouseEvent event) {}
        void handleReleased(MouseEvent event) {}
    }


    // ----------------- READY STATE ----------------- //

    ControllerState ready = new ControllerState() {

        public void handlePressed(MouseEvent event) {
            prevX = event.getX();
            prevY = event.getY();

            if (event.isControlDown() && model.whichBox(prevX/scale, prevY/scale) instanceof Portal portal) {
                double portalWorldX, portalWorldY;
                portalWorldX = (prevX/scale - portal.getX() - portal.getPortalLeft())/portal.getScaleFactor();
                portalWorldY = (prevY/scale - portal.getY() - portal.getPortalTop())/portal.getScaleFactor();

                if (model.contains(portalWorldX, portalWorldY)) {
                    iModel.setSelectedBox(model.whichBox(portalWorldX, portalWorldY));
                    currentState = dragging;
                }
                else {
                    iModel.setSelectedBox(portal);
                    currentState = panning;
                }
            }
            else if (iModel.getSelectedBox() != null && iModel.onHandle(prevX/scale, prevY/scale) != 0) {
                currentState = resizing;
            }
            else if (model.contains(prevX/scale, prevY/scale)) {
                iModel.setSelectedBox(model.whichBox(prevX/scale, prevY/scale));
                currentState = dragging;
            }
            else {
                currentState = create_or_unselect;
            }
        }

        public void handleKeyPressed(KeyEvent event) {

            switch (event.getCode()) {
                case BACK_SPACE:
                case DELETE:
                    if (iModel.getSelectedBox() != null) {
                        model.removeBox(iModel.getSelectedBox());
                    }
                    break;
                case UP:
                    if (iModel.getSelectedBox() != null && iModel.getSelectedBox() instanceof Portal portal) {
                        portal.setScaleFactor(portal.getScaleFactor() + 0.05);
                        iModel.notifySubscribers();
                    }
                    break;
                case DOWN:
                    if (iModel.getSelectedBox() != null && iModel.getSelectedBox() instanceof Portal portal) {
                        portal.setScaleFactor(portal.getScaleFactor() - 0.05);
                        iModel.notifySubscribers();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    // ----------------- DRAGGING STATE ----------------- //

    ControllerState dragging = new ControllerState() {

        public void handleDragged(MouseEvent event) {

            double dx, dy;

            dx = event.getX() - prevX;
            dy = event.getY() - prevY;

            prevX = event.getX();
            prevY = event.getY();

            iModel.getSelectedBox().addX(dx/scale);
            iModel.getSelectedBox().addY(dy/scale);

            model.notifySubscribers();

        }

        public void handleReleased(MouseEvent event) {

            currentState = ready;

        }
    };


    // ----------------- CREATE/UNSELECT STATE ----------------- //

    ControllerState create_or_unselect = new ControllerState() {

        public void handleDragged(MouseEvent event) {
            if (event.isControlDown()) {
                model.addPortal(new Portal(prevX / scale, prevY / scale, 1,1));
            }
            else {
                model.addBox(prevX / scale, prevY / scale, 1 / scale, 1 / scale);
            }
            iModel.setSelectedBox(model.whichBox(prevX/scale, prevY/scale));
            currentState = creating;
        }

        public void handleReleased(MouseEvent event) {
            iModel.setSelectedBox(null);
            currentState = ready;
        }

    };


    // ----------------- CREATING STATE ----------------- //

    ControllerState creating = new ControllerState() {

        public void handleDragged(MouseEvent event) {

            double dw, dh;

            dw = Math.abs(event.getX() - prevX);
            dh = Math.abs(event.getY() - prevY);

            iModel.getSelectedBox().setX(Math.min(event.getX()/scale, prevX/scale));
            iModel.getSelectedBox().setY(Math.min(event.getY()/scale, prevY/scale));
            iModel.getSelectedBox().setWidth(dw/scale);
            iModel.getSelectedBox().setHeight(dh/scale);

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

            double dx, dy;

            dx = prevX - event.getX();
            dy = prevY - event.getY();

            prevX = event.getX();
            prevY = event.getY();

            if (event.isControlDown() && iModel.getSelectedBox() instanceof Portal portal) {

                dx /= portal.getScaleFactor();
                dy /= portal.getScaleFactor();

                portal.setPortalLeft(portal.getPortalLeft() - dx);
                portal.setPortalTop(portal.getPortalTop() - dy);
                iModel.notifySubscribers();
            }
        }

        public void handleReleased(MouseEvent event) {
            currentState = ready;
        }
    };


    // ----------------- RESIZING STATE ----------------- //

    ControllerState resizing = new ControllerState() {

        public void handleDragged(MouseEvent event) {

            // Adjust for scale
            double newX = event.getX();
            double newY = event.getY();
            double dX = (newX - prevX) / scale;
            double dY = (newY - prevY) / scale;

            int handle = iModel.onHandle(prevX / scale, prevY / scale);
            switch (handle) {
                case 1:  // Top-left handle
                    iModel.getSelectedBox().setX(iModel.getSelectedBox().getX() + dX);
                    iModel.getSelectedBox().setY(iModel.getSelectedBox().getY() + dY);
                    iModel.getSelectedBox().setWidth(iModel.getSelectedBox().getWidth() - dX);
                    iModel.getSelectedBox().setHeight(iModel.getSelectedBox().getHeight() - dY);
                    break;
                case 2:  // Top-right handle
                    iModel.getSelectedBox().setY(iModel.getSelectedBox().getY() + dY);
                    iModel.getSelectedBox().setWidth(iModel.getSelectedBox().getWidth() + dX);
                    iModel.getSelectedBox().setHeight(iModel.getSelectedBox().getHeight() - dY);
                    break;
                case 3:  // Bottom-left handle
                    iModel.getSelectedBox().setX(iModel.getSelectedBox().getX() + dX);
                    iModel.getSelectedBox().setWidth(iModel.getSelectedBox().getWidth() - dX);
                    iModel.getSelectedBox().setHeight(iModel.getSelectedBox().getHeight() + dY);
                    break;
                case 4:  // Bottom-right handle
                    iModel.getSelectedBox().setWidth(iModel.getSelectedBox().getWidth() + dX);
                    iModel.getSelectedBox().setHeight(iModel.getSelectedBox().getHeight() + dY);
                    break;
                default:
                    break;
            }

            // Update prevX and prevY to the current event position
            prevX = newX;
            prevY = newY;

            model.notifySubscribers(); // Update the view
        }

        public void handleReleased(MouseEvent event) {
            currentState = ready;
        }
    };
}
