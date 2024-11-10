package com.example.cmpt3812024a3;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MiniView extends DetailView {

    private Canvas miniCanvas;
    private GraphicsContext gc;
    private double side=200, scale;

    public MiniView() {
        super();
        miniCanvas = new Canvas(side, side);
        gc = miniCanvas.getGraphicsContext2D();

        this.setAlignment(javafx.geometry.Pos.TOP_LEFT); // Position this at the top left
        this.getChildren().add(miniCanvas);
    }

    public void draw() {
        scale = side / imodel.getWorldSize();

        gc.clearRect(0, 0, miniCanvas.getWidth(), miniCanvas.getHeight());
        gc.save();

        // Create border around the miniView
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, miniCanvas.getWidth(), miniCanvas.getHeight());
        gc.strokeRect(0, 0, miniCanvas.getWidth(), miniCanvas.getHeight());

        // Scale to fit the whole world
        gc.scale(scale, scale);

        // Draw the detailView rectangle
        gc.setFill(Color.YELLOW);
        gc.fillRect(-imodel.getViewPortLeft(), -imodel.getViewPortTop(), getViewWidth(), getViewHeight());

        // Draw the boxes
        model.getBoxes().forEach(entity -> {
            if (imodel.getSelectedBox() == entity) {
                gc.setFill(Color.ORANGE);
            } else {
                gc.setFill(Color.BLUE);
            }
            gc.fillRect(entity.getX(), entity.getY(), entity.getW(), entity.getH());
            gc.strokeRect(entity.getX(), entity.getY(), entity.getW(), entity.getH());
        });

        gc.restore();
    }
}
