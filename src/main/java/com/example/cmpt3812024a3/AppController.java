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
    public void setModel(EntityModel m) {
        this.model = m;
    }

    /**
     * Set the imodel
     * @param im imodel
     */
    public void setIModel(InteractionModel im) {
        this.iModel = im;
    }


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
            prevX = event.getX();
            prevY = event.getY();

            worldX = event.getX() + iModel.getViewPortLeft();
            worldY = event.getY() + iModel.getViewPortTop();

            if (event.isControlDown() && model.whichBox(worldX, worldY) instanceof Portal portal) {
                double portalWorldX, portalWorldY;
                portalWorldX = (worldX - portal.getX() - portal.getPortalLeft())/portal.getScaleFactor();
                portalWorldY = (worldY - portal.getY() - portal.getPortalTop())/portal.getScaleFactor();
                if (model.contains(portalWorldX, portalWorldY)) {
                    iModel.setSelectedBox(model.whichBox(portalWorldX, portalWorldY));
                    currentState = dragging;
                }
                else {
                    currentState = panning;
                }
            }
            else if (iModel.getSelectedBox() != null && iModel.onHandle(worldX, worldY) != 0) {
                currentState = resizing;
            }
            else if (model.contains(worldX, worldY)) {
                iModel.setSelectedBox(model.whichBox(worldX, worldY));
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

            iModel.getSelectedBox().addX(dx);
            iModel.getSelectedBox().addY(dy);

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

            double dw, dh;

            dw = Math.abs(event.getX() + iModel.getViewPortLeft() - worldX);
            dh = Math.abs(event.getY() + iModel.getViewPortTop() - worldY);

            iModel.getSelectedBox().setX(Math.min(event.getX() + iModel.getViewPortLeft(), worldX));
            iModel.getSelectedBox().setY(Math.min(event.getY() + iModel.getViewPortTop(), worldY));
            iModel.getSelectedBox().setWidth(dw);
            iModel.getSelectedBox().setHeight(dh);

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
            iModel.setSelectedBox(null);
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
            else {
                iModel.moveViewPort(dx, dy);
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

            double newX = event.getX() + iModel.getViewPortLeft();
            double newY = event.getY() + iModel.getViewPortTop();
            double dX = newX - worldX;
            double dY = newY - worldY;

            switch (handle) {
                case 1:  // Top-left handle
                    iModel.getSelectedBox().setX(iModel.getSelectedBox().getX() + dX);
                    iModel.getSelectedBox().setY(iModel.getSelectedBox().getY() + dY);
                    iModel.getSelectedBox().setWidth(iModel.getSelectedBox().getWidth() - dX);
                    iModel.getSelectedBox().setHeight(iModel.getSelectedBox().getHeight() - dY);
                    break;
                case 2:  // Top-right handle
                    iModel.getSelectedBox().setX(iModel.getSelectedBox().getX());
                    iModel.getSelectedBox().setY(iModel.getSelectedBox().getY() + dY);
                    iModel.getSelectedBox().setWidth(iModel.getSelectedBox().getWidth() + dX);
                    iModel.getSelectedBox().setHeight(iModel.getSelectedBox().getHeight() - dY);
                    break;
                case 3:  // Bottom-left handle
                    iModel.getSelectedBox().setX(iModel.getSelectedBox().getX() + dX);
                    iModel.getSelectedBox().setY(iModel.getSelectedBox().getY());
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

            worldX = newX;
            worldY = newY;
            model.notifySubscribers();

        }

        public void handleReleased(MouseEvent event) {
            currentState = ready;
        }
    };
}
