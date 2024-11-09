package com.example.cmpt3812024a3;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class DetailView extends StackPane implements Subscriber {

    Canvas myCanvas;
    double width, height;
    GraphicsContext gc;
    EntityModel model;
    InteractionModel imodel;

    /**
     * Constructor of the View Class
     */
    public DetailView() {

        this.width = 800;
        this.height = 800;
        myCanvas = new Canvas(width, height);
        gc = myCanvas.getGraphicsContext2D();
        this.widthProperty().addListener((observable, oldValue, newValue) -> {
            myCanvas.setWidth(newValue.doubleValue());
            draw();
        });
        this.heightProperty().addListener((observable, oldValue, newValue) -> {
            myCanvas.setHeight(newValue.doubleValue());
            draw();
        });
        Platform.runLater(() -> myCanvas.requestFocus());
        this.getChildren().add(myCanvas);

    }

    /**
     * Set the given value as the model of the view
     * @param m model
     */
    public void setModel(EntityModel m) { this.model = m; }

    /**
     * Set the given value as the imodel of the view
     * @param im imodel
     */
    public void setIModel(InteractionModel im) { this.imodel = im; }

    /**
     * Set up the events of the view to be passed to controller
     * @param controller controller of the application
     */
    public void setupEvents(AppController controller) {
        setOnMousePressed(controller::handlePressed);
        setOnMouseDragged(controller::handleDragged);
        setOnMouseReleased(controller::handleReleased);
        setOnKeyPressed(controller::handleKeyPressed);
        setOnKeyReleased(controller::handleKeyReleased);
    }

    /**
     * Draw the boxes in the model on the canvas
     */
    public void draw() {
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        gc.save();
        gc.translate(imodel.getViewLeft(), imodel.getViewTop());
        model.getBoxes().forEach(entity -> {
            if (imodel.getSelectedBox() == entity) {
                gc.setFill(Color.ORANGE);
            }
            else {
                gc.setFill(Color.BLUE);
            }
            gc.setLineWidth(2);
            gc.fillRect(entity.getX(), entity.getY(), entity.getW(), entity.getH());
            gc.strokeRect(entity.getX(), entity.getY(), entity.getW(), entity.getH());
        });
        gc.restore();
    }

    @Override
    public void modelChanged() {
        draw();
    }
}
