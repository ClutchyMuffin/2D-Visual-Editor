package com.example.cmpt3812024a3;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class DetailView extends StackPane implements Subscriber {

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
        Canvas myCanvas = new Canvas(width, height);
        gc = myCanvas.getGraphicsContext2D();
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
    }

    /**
     * Draw the boxes in the model on the canvas
     */
    public void draw() {
        gc.clearRect(0, 0, width, height);
        model.getBoxes().forEach(box -> {
            gc.setFill(Color.INDIGO);
            gc.fillRect(box.getX(), box.getY(), box.getW(), box.getH());
        });
    }

    @Override
    public void modelChanged() {
        draw();
    }
}
