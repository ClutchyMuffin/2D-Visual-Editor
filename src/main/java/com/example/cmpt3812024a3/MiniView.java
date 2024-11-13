/**
 * Name = Sayed Farzaan Rafi Bhat
 * NSID = kfn036
 * Stu# = 11356043
 */

package com.example.cmpt3812024a3;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class MiniView extends DetailView {

    private final Canvas miniCanvas;
    private final GraphicsContext gc;
    private final double side=300;
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
        this.setOpacity(0.65);
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
    }


    // ----------------- DRAW ----------------- //

    public void draw() {
        scale = side / iModel.getWorldSize(); // Scale to use for drawing

        // Clear the canvas & save context
        gc.clearRect(0, 0, miniCanvas.getWidth(), miniCanvas.getHeight());

        // Create border around the miniView
        gc.setFill(Color.BLACK);
        gc.setLineWidth(3);
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, miniCanvas.getWidth(), miniCanvas.getHeight());
        gc.strokeRect(0, 0, miniCanvas.getWidth(), miniCanvas.getHeight());

        // Save current context and scale
        gc.save();
        gc.scale(scale, scale);

        // Draw the detailView rectangle
        gc.setFill(Color.YELLOWGREEN);
        gc.fillRect(iModel.getViewPortLeft(), iModel.getViewPortTop(),
                    iModel.getViewPortWidth(), iModel.getViewPortHeight());

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
}
