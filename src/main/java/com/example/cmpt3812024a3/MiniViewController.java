package com.example.cmpt3812024a3;

import javafx.scene.input.MouseEvent;

public class MiniViewController {

    private EntityModel model;
    private InteractionModel iModel;
    private ControllerState currentState;
    private double prevX, prevY;
    private double scale;

    /**
     * Constructor for the MiniViewController class
     */
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

            // World Coordinates (MiniView displays the whole world)
            prevX = event.getX();
            prevY = event.getY();

            // Pressed on a portal while CTRL is pressed
            if (event.isControlDown() && model.whichBox(prevX/scale, prevY/scale) instanceof Portal portal) {

                // Calculate world coordinate through the Portal
                double portalWorldX, portalWorldY;
                portalWorldX = (prevX/scale - portal.getX() - portal.getPortalLeft())/portal.getScaleFactor();
                portalWorldY = (prevY/scale - portal.getY() - portal.getPortalTop())/portal.getScaleFactor();

                // Clicked on a box through a portal
                if (model.contains(portalWorldX, portalWorldY)) {
                    iModel.setSelectedBox(model.whichBox(portalWorldX, portalWorldY));
                    currentState = dragging;
                }
                // Clicked on the background through a portal
                else {
                    iModel.setSelectedBox(portal);
                    currentState = panning;
                }
            }

            // Clicked on the handle of a Box
            else if (iModel.getSelectedBox() != null && iModel.onHandle(prevX/scale, prevY/scale) != 0) {
                currentState = resizing;
            }

            // Clicked on a box in the world
            else if (model.contains(prevX/scale, prevY/scale)) {
                iModel.setSelectedBox(model.whichBox(prevX/scale, prevY/scale));
                currentState = dragging;
            }

            // Clicked on the background
            else { currentState = create_or_unselect; }
        }
    };


    // ----------------- DRAGGING STATE ----------------- //

    ControllerState dragging = new ControllerState() {

        public void handleDragged(MouseEvent event) {

            // Calculate distance moved and add to the position of the box
            double dx = (event.getX() - prevX)/scale;
            double dy = (event.getY() - prevY)/scale;

            prevX = event.getX();
            prevY = event.getY();

            iModel.getSelectedBox().addX(dx);
            iModel.getSelectedBox().addY(dy);

            model.notifySubscribers();
        }

        public void handleReleased(MouseEvent event) { currentState = ready; }

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

            // Calculate distance moved and add to width and height
            double dw = (Math.abs(event.getX() - prevX))/scale;
            double dh = (Math.abs(event.getY() - prevY))/scale;
            iModel.getSelectedBox().setWidth(dw);
            iModel.getSelectedBox().setHeight(dh);

            // Set the Coordinates as the minimum of the new position and the original click
            iModel.getSelectedBox().setX(Math.min(event.getX()/scale, prevX/scale));
            iModel.getSelectedBox().setY(Math.min(event.getY()/scale, prevY/scale));


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

            // Calculate distance moved
            double dx = prevX - event.getX();
            double dy = prevY - event.getY();
            prevX = event.getX();
            prevY = event.getY();

            if (event.isControlDown() && iModel.getSelectedBox() instanceof Portal portal) {

                // Portal panning, scale the distance to the portal
                dx *= portal.getScaleFactor();
                dy *= portal.getScaleFactor();

                // Update portal properties
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

            int handle = iModel.onHandle(prevX / scale, prevY / scale);

            // Calculate distance moved
            double newX = event.getX();
            double newY = event.getY();
            double dX = (newX - prevX) / scale;
            double dY = (newY - prevY) / scale;

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

            // Update based on current event position
            prevX = newX;
            prevY = newY;
            model.notifySubscribers();

        }

        public void handleReleased(MouseEvent event) {
            currentState = ready;
        }
    };
}
