package com.example.cmpt3812024a3;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class DetailView extends StackPane implements Subscriber {

    private final Canvas myCanvas;
    private final GraphicsContext gc;
    private double viewWidth, viewHeight;
    protected EntityModel model;
    protected InteractionModel iModel;

    /**
     * Constructor of the View Class
     */
    public DetailView() {

        viewWidth = 800;
        viewHeight = 800;
        myCanvas = new Canvas(viewWidth, viewHeight);
        gc = myCanvas.getGraphicsContext2D();

        this.setMinSize(0,0);
        this.getChildren().add(myCanvas);

        // Fix the resizing
        this.widthProperty().addListener((_, _, newValue) -> {
            this.viewWidth = newValue.doubleValue();
            myCanvas.setWidth(this.viewWidth);
            iModel.setViewPortWidth(this.viewWidth);
            draw();
        });

        this.heightProperty().addListener((_, _, newValue) -> {
            this.viewHeight = newValue.doubleValue();
            myCanvas.setHeight(this.viewHeight);
            iModel.setViewPortHeight(this.viewHeight);
            draw();
        });
    }

    // ----------------- GETTERS ----------------- //

    /**
     * Get the width of the view
     * @return width
     */
    public double getViewWidth() { return this.viewWidth; }

    /**
     * Get the height of the view
     * @return height
     */
    public double getViewHeight() { return this.viewHeight; }


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
    public void setIModel(InteractionModel im) { this.iModel = im; }

    /**
     * Set up the events of the view to be passed to controller
     * @param controller controller of the application
     */
    public void setupEvents(AppController controller) {
        myCanvas.setOnMousePressed(controller::handlePressed);
        myCanvas.setOnMouseDragged(controller::handleDragged);
        myCanvas.setOnMouseReleased(controller::handleReleased);
        setOnKeyPressed(controller::handleKeyPressed);
        setOnKeyReleased(controller::handleKeyReleased);
    }


    // ----------------- DRAW ----------------- //

    /**
     * Draw the boxes in the model on the canvas
     */
    public void draw() {
        // Clear the canvas
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        gc.strokeRect(0,0, iModel.getWorldSize(), iModel.getWorldSize());
        // Save current context and translate according to viewPort position
        gc.save();
        gc.translate(-iModel.getViewPortLeft(), -iModel.getViewPortTop());

        // Go through entity list and draw all
        model.getBoxes().forEach(box -> {
            if (box instanceof Portal portal) {
                drawPortal(gc, portal, 0); }
            else {
                drawElement(gc, box);
            }
        });
        gc.restore();
    }

    /**
     * Private method to draw the given box
     * @param box entity to draw
     */
    protected void drawElement(GraphicsContext gc, Box box) {

        // Choose fill color
        gc.setFill(iModel.getSelectedBox() == box ? Color.ORANGE : Color.BLUE);

        // Draw boxes
        gc.setLineWidth(2);
        gc.fillRect(box.getX(), box.getY(), box.getWidth(), box.getHeight());
        gc.strokeRect(box.getX(), box.getY(), box.getWidth(), box.getHeight());

        // Draw handles on the selected box
        if (iModel.getSelectedBox() == box) { drawHandles(gc, box); }
    }

    /**
     * Private method to draw the given portal
     * @param portal portal to draw
     */
    protected void drawPortal(GraphicsContext gc, Portal portal, int depth) {
        int MAX_DEPTH = 2;
        if (depth > MAX_DEPTH) return;

        // Save current context & reset the path
        gc.save();
        gc.beginPath();

        // Draw the Portal Window
        gc.setLineWidth(2);
        gc.setFill(Color.LIGHTGRAY);
        gc.strokeRect(portal.getX(), portal.getY(), portal.getWidth(), portal.getHeight());
        gc.fillRect(portal.getX(), portal.getY(), portal.getWidth(), portal.getHeight());
        if (iModel.getSelectedBox() == portal ) { drawHandles(gc, portal); }

        // Define Clipping region & clip it
        gc.rect(portal.getX(), portal.getY(), portal.getWidth(), portal.getHeight());
        gc.clip();

        // Transform the GC to the portal's properties & draw elements
        gc.translate(portal.getX() + portal.getPortalLeft(), portal.getY() + portal.getPortalTop());
        gc.scale(portal.getScaleFactor(), portal.getScaleFactor());

        // Draw border around the world, and two diagonals
        gc.strokeRect(0,0, iModel.getWorldSize(), iModel.getWorldSize());
        gc.setFill(Color.MAROON);
        gc.setLineDashes(10);
        gc.strokeLine(0,0, iModel.getWorldSize(), iModel.getWorldSize());
        gc.setLineDashes(0);

        // Draw inner boxes
        model.getBoxes().forEach(innerElement -> {
            if (innerElement instanceof Portal innerPortal) {
                drawPortal(gc, innerPortal, depth + 1);  // Recursive call with incremented depth
            } else if (depth < 2) {
                drawElement(gc, innerElement);
            }
        });
        gc.restore();
    }

    /**
     * Private method to draw handles around the given box
     * @param box entity to draw handles around
     */
    protected void drawHandles(GraphicsContext gc, Box box) {
        gc.setFill(Color.WHITE);
        double radius = iModel.getHandleRadius();

        // Top Left
        gc.strokeOval(box.getX() - radius, box.getY() - radius, 2 * radius, 2 * radius);
        gc.fillOval(box.getX() - radius, box.getY() - radius, 2 * radius, 2 * radius);

        // Top Right
        gc.strokeOval(box.getX() + box.getWidth() - radius, box.getY() - radius, 2 * radius, 2 * radius);
        gc.fillOval(box.getX() + box.getWidth() - radius, box.getY() - radius, 2 * radius, 2 * radius);

        // Bottom Left
        gc.strokeOval(box.getX() - radius, box.getY() + box.getHeight() - radius, 2 * radius, 2 * radius);
        gc.fillOval(box.getX() - radius, box.getY() + box.getHeight() - radius, 2 * radius, 2 * radius);

        // Bottom Right
        gc.strokeOval(box.getX() + box.getWidth() - radius, box.getY() + box.getHeight() - radius, 2 * radius, 2 * radius);
        gc.fillOval(box.getX() + box.getWidth() - radius, box.getY() + box.getHeight() - radius, 2 * radius, 2 * radius);
    }

    @Override
    public void modelChanged() {
        draw();
    }
}
