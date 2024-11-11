package com.example.cmpt3812024a3;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;

public class MainUI extends StackPane {

    public MainUI() {

        AppController controller = new AppController();
        EntityModel model = new EntityModel();
        InteractionModel imodel = new InteractionModel();
        DetailView detailView = new DetailView();
        MiniView miniView = new MiniView();

        // Controller
        controller.setModel(model);
        controller.setIModel(imodel);

        // Model
        model.addSubscriber(detailView);
        model.addSubscriber(miniView);
        imodel.addSubscriber(detailView);
        imodel.addSubscriber(miniView);

        // DetailView
        detailView.setModel(model);
        detailView.setIModel(imodel);
        detailView.setupEvents(controller);

        // MiniView
        miniView.setModel(model);
        miniView.setIModel(imodel);
        miniView.setupEvents(controller);

        Platform.runLater(detailView::requestFocus);
        this.getChildren().addAll(detailView, miniView);

    }

}
