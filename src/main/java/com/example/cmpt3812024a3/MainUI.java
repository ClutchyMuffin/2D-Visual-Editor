package com.example.cmpt3812024a3;

import javafx.scene.layout.StackPane;

public class MainUI extends StackPane {

    public MainUI() {

        EntityModel model = new EntityModel();
        InteractionModel imodel = new InteractionModel();
        DetailView view = new DetailView();
        AppController controller = new AppController();

        model.addSubscriber(view);
        imodel.addSubscriber(view);
        view.setModel(model);
        view.setIModel(imodel);
        view.setupEvents(controller);
        controller.setModel(model);
        controller.setIModel(imodel);

        this.getChildren().add(view);

    }

}
