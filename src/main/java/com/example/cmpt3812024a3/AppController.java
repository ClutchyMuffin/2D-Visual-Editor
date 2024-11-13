package com.example.cmpt3812024a3;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class AppController {

    private EntityModel model;
    private InteractionModel iModel;
    private ControllerState currentState;
    private double prevX, prevY;
    private double worldX, worldY;

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
    public void setModel(EntityModel m) { this.model = m; }

    /**
     * Set the imodel
     * @param im imodel
     */
    public void setIModel(InteractionModel im) { this.iModel = im; }


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
    public void handleDragged(MouseEvent event) { currentState.handleDragged(event); }

    /**
     * Handle the Mouse Released event
     * @param event mouse event
     */
    public void handleReleased(MouseEvent event) { currentState.handleReleased(event); }

    /**
     * Handle the Key Pressed event
     * @param event key event
     */
    public void handleKeyPressed(KeyEvent event) { currentState.handleKeyPressed(event); }

    /**
     * Handle the Key Released event
     * @param event key event
     */
    public void handleKeyReleased(KeyEvent event) { currentState.handleKeyReleased(event); }


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

            // View Coordinates
            prevX = event.getX();
            prevY = event.getY();

            // World Coordinates
            worldX = event.getX() + iModel.getViewPortLeft();
            worldY = event.getY() + iModel.getViewPortTop();

            // Pressed on a portal while CTRL is pressed
            if (event.isControlDown() && model.whichBox(worldX, worldY) instanceof Portal portal) {

                // Calculate world coordinate through the Portal
                double portalWorldX, portalWorldY;
                portalWorldX = (worldX - portal.getX() - portal.getPortalLeft())/portal.getScaleFactor();
                portalWorldY = (worldY - portal.getY() - portal.getPortalTop())/portal.getScaleFactor();

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
            else if (iModel.getSelectedBox() != null && iModel.onHandle(worldX, worldY) != 0) {
                currentState = resizing;
            }

            // Clicked on a box in the world
            else if (model.contains(worldX, worldY)) {
                iModel.setSelectedBox(model.whichBox(worldX, worldY));
                currentState = dragging;
            }

            // Clicked on the background
            else { currentState = create_or_unselect; }
        }

        public void handleKeyPressed(KeyEvent event) {

            // Different states and events based on the key pressed
            switch (event.getCode()) {
                case DELETE:
                case BACK_SPACE:
                    if (iModel.getSelectedBox() != null) {
                        model.removeBox(iModel.getSelectedBox());
                        iModel.setSelectedBox(null);
                    }
                    break;

                case SHIFT:
                    currentState = panning;
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

            // Calculate distance moved and add to the position of the box
            double dx = event.getX() - prevX;
            double dy = event.getY() - prevY;

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
                model.addPortal(new Portal(worldX, worldY, 0,0));
            }
            else {
                model.addBox(worldX, worldY, 0 ,0);
            }
            iModel.setSelectedBox(model.whichBox(worldX, worldY));
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

            double newX = event.getX() + iModel.getViewPortLeft();
            double newY = event.getY() + iModel.getViewPortTop();

            // Calculate distance moved and add to width and height
            double dw = Math.abs(newX - worldX);
            double dh = Math.abs(newY - worldY);
            iModel.getSelectedBox().setWidth(dw);
            iModel.getSelectedBox().setHeight(dh);

            // Set the Coordinates as the minimum of the new position and the original click
            iModel.getSelectedBox().setX(Math.min(newX, worldX));
            iModel.getSelectedBox().setY(Math.min(newY, worldY));

            iModel.notifySubscribers();
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
            else {
                iModel.moveViewPort(dx, dy);
            }
        }

        public void handleReleased(MouseEvent event) {
            if (event.isControlDown()) {
                currentState = ready;
            }
            else {
                currentState = panning;
            }
        }

        public void handleKeyReleased(KeyEvent event) {
            currentState = ready;
        }
    };


    // ----------------- RESIZING STATE ----------------- //

    ControllerState resizing = new ControllerState() {

        public void handleDragged(MouseEvent event) {

            int handle = iModel.onHandle(worldX, worldY);

            // Calculate distance moved
            double newX = event.getX() + iModel.getViewPortLeft();
            double newY = event.getY() + iModel.getViewPortTop();
            double dx = newX - worldX;
            double dy = newY - worldY;

            Box box = iModel.getSelectedBox();

            switch (handle) {
                case 1:  // Top-left handle
                    box.addX(dx);
                    box.addY(dy);
                    box.addWidth(-dx);
                    box.addHeight(-dy);
                    manageAcrossHandle(box);
                    break;

                case 2:  // Top-right handle
                    box.addY(dy);
                    box.addWidth(dx);
                    box.addHeight(-dy);
                    manageAcrossHandle(box);
                    break;

                case 3:  // Bottom-left handle
                    box.addX(dx);
                    box.addWidth(-dx);
                    box.addHeight(dy);
                    manageAcrossHandle(box);
                    break;

                case 4:  // Bottom-right handle
                    box.addWidth(dx);
                    box.addHeight(dy);
                    manageAcrossHandle(box);
                    break;

                default:
                    break;
            }

            // Update based on current event position
            worldX = newX;
            worldY = newY;
            model.notifySubscribers();

        }

        public void handleReleased(MouseEvent event) {
            currentState = ready;
        }
    };

    private static void manageAcrossHandle(Box box) {
        if (box.getWidth() < 0) {
            box.addX(box.getWidth());
            box.setWidth(-box.getWidth());
        }
        if (box.getHeight() < 0) {
            box.addY(box.getHeight());
            box.setHeight(-box.getHeight());
        }
    }
}
