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

    public void setModel(EntityModel model) { this.model = model; }
    public void setiModel(InteractionModel iModel) { this.iModel = iModel; }
    public void setScale(double scale) { this.scale = scale; }

    public void handlePressed(MouseEvent event) { currentState.handlePressed(event); }
    public void handleReleased(MouseEvent event) { currentState.handleReleased(event); }
    public void handleDragged(MouseEvent event) { currentState.handleDragged(event); }
    public void handleKeyPressed(KeyEvent event) { currentState.handleKeyPressed(event); }
    public void handleKeyReleased(KeyEvent event) { currentState.handleKeyReleased(event); }


    public abstract static class ControllerState {
        void handlePressed(MouseEvent event) {}
        void handleDragged(MouseEvent event) {}
        void handleReleased(MouseEvent event) {}
        void handleKeyPressed(KeyEvent event) {}
        void handleKeyReleased(KeyEvent event) {}
    }

    ControllerState ready = new ControllerState() {

        public void handlePressed(MouseEvent event) {
            prevX = event.getX();
            prevY = event.getY();

            if (iModel.getSelectedBox() != null && iModel.onHandle(prevX/scale, prevY/scale) != 0) {
                currentState = resizing;
            }
            else if (model.contains(prevX/scale, prevY/scale)) {
                iModel.setSelectedBox(model.whichBox(prevX/scale, prevY/scale));
                currentState = dragging;
            }
            else {
                currentState = preparing;
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
                case SHIFT:
                    currentState = panning;
                default:
                    break;
            }

        }

    };


    ControllerState preparing = new ControllerState() {

        public void handleDragged(MouseEvent event) {
            model.addBox(prevX/scale, prevY/scale, 1/scale, 1/scale);
            iModel.setSelectedBox(model.whichBox(prevX/scale, prevY/scale));
            currentState = creating;
        }

        public void handleReleased(MouseEvent event) {
            iModel.setSelectedBox(null);
            currentState = ready;
        }

    };


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

            iModel.moveViewPort(dx, dy);
        }

        public void handleKeyReleased(KeyEvent event) {
            currentState = ready;
        }
    };

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
