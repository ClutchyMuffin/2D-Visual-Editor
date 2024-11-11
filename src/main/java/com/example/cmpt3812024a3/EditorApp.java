package com.example.cmpt3812024a3;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EditorApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        MainUI mainUI = new MainUI();
        Scene scene = new Scene(mainUI);

        stage.setTitle("Editor App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
