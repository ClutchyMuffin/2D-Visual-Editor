package com.example.cmpt3812024a3;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class MiniView extends DetailView {

    private Canvas miniCanvas;
    private GraphicsContext gc;
    private double side=200;
    private double scale;

    public MiniView() {
        super();

        // Limit the size of this Stack Pane
        this.setMaxSize(side,side);
        this.setMinSize(0,0);

        miniCanvas = new Canvas(side, side);
        gc = miniCanvas.getGraphicsContext2D();

        StackPane.setAlignment(this, Pos.TOP_LEFT); // Position this at the top left
        this.getChildren().add(miniCanvas);
        this.setOpacity(0.5);
    }


    // ----------------- DRAW ----------------- //

    public void draw() {
        scale = side / iModel.getWorldSize();

        gc.clearRect(0, 0, miniCanvas.getWidth(), miniCanvas.getHeight());
        gc.save();

        // Create border around the miniView
        gc.setLineWidth(2);
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, miniCanvas.getWidth(), miniCanvas.getHeight());
        gc.strokeRect(0, 0, miniCanvas.getWidth(), miniCanvas.getHeight());

        // Scale to fit the whole world
        gc.scale(scale, scale);

        // Draw the detailView rectangle
        gc.setFill(Color.YELLOW);
        gc.fillRect(iModel.getViewPortLeft(), iModel.getViewPortTop(),
                    iModel.getViewPortWidth(), iModel.getViewPortHeight());

        // Go through entity list and draw all
        model.getBoxes().forEach(box -> {

            if (iModel.getSelectedBox() == box) {
                gc.setFill(Color.ORANGE);
            } else {
                gc.setFill(Color.BLUE);
            }

            // Draw boxes
            gc.setLineWidth(2);
            gc.fillRect(box.getX(), box.getY(), box.getWidth(), box.getHeight());
            gc.strokeRect(box.getX(), box.getY(), box.getWidth(), box.getHeight());

            // Draw handles on the selected box
            if (iModel.getSelectedBox() == box) {
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
        });

        gc.restore();
    }

    // ----------------- SETTERS ----------------- //

    public void setupEvents(MiniViewController controller) {
        miniCanvas.setOnMousePressed(e -> {
            controller.setScale(scale);
            controller.handlePressed(e);
        });
        miniCanvas.setOnMouseReleased(e -> {
            controller.setScale(scale);
            controller.handleReleased(e);
        });
        miniCanvas.setOnMouseDragged(e -> {
            controller.setScale(scale);
            controller.handleDragged(e);
        });
        setOnKeyPressed(controller::handleKeyPressed);
        setOnKeyReleased(controller::handleKeyReleased);
    }

}
