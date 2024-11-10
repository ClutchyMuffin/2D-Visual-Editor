package com.example.cmpt3812024a3;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class DetailView extends StackPane implements Subscriber {

    private Canvas myCanvas;
    private double width, height;
    private GraphicsContext gc;
    protected EntityModel model;
    protected InteractionModel imodel;

    /**
     * Constructor of the View Class
     */
    public DetailView() {

        this.width = 800;
        this.height = 800;
        myCanvas = new Canvas(width, height);
        gc = myCanvas.getGraphicsContext2D();

        // Fix the resizing
        this.widthProperty().addListener((observable, oldValue, newValue) -> {
            this.width = newValue.doubleValue();
            myCanvas.setWidth(width);
            imodel.setViewPortWidth(width);
            draw();
        });

        this.heightProperty().addListener((observable, oldValue, newValue) -> {
            this.height = newValue.doubleValue();
            myCanvas.setHeight(height);
            imodel.setViewPortHeight(height);
            draw();
        });

        this.getChildren().add(myCanvas);

    }

    // ----------------- GETTERS ----------------- //

    /**
     * Get the width of the view
     * @return width
     */
    public double getViewWidth() { return this.width; }

    /**
     * Get the height of the view
     * @return height
     */
    public double getViewHeight() { return this.height; }


    // ----------------- SETTERS ----------------- //

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


    // ----------------- DRAW ----------------- //

    /**
     * Draw the boxes in the model on the canvas
     */
    public void draw() {
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        gc.save();
        gc.translate(imodel.getViewPortLeft(), imodel.getViewPortTop());
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

            if (imodel.getSelectedBox() == entity) {
                gc.setFill(Color.WHITE);
                double radius = imodel.getHandleRadius();

                gc.strokeOval(entity.getX() - radius, entity.getY() - radius, 2 * radius, 2 * radius);
                gc.strokeOval(entity.getX() + entity.getW() - radius, entity.getY() - radius, 2 * radius, 2 * radius);
                gc.strokeOval(entity.getX() - radius, entity.getY() + entity.getH() - radius, 2 * radius, 2 * radius);
                gc.strokeOval(entity.getX() + entity.getW() - radius, entity.getY() + entity.getH() - radius, 2 * radius, 2 * radius);
                gc.fillOval(entity.getX() - radius, entity.getY() - radius, 2 * radius, 2 * radius);
                gc.fillOval(entity.getX() + entity.getW() - radius, entity.getY() - radius, 2 * radius, 2 * radius);
                gc.fillOval(entity.getX() - radius, entity.getY() + entity.getH() - radius, 2 * radius, 2 * radius);
                gc.fillOval(entity.getX() + entity.getW() - radius, entity.getY() + entity.getH() - radius, 2 * radius, 2 * radius);

            }
        });
        gc.restore();
    }

    @Override
    public void modelChanged() {
        draw();
    }
}
