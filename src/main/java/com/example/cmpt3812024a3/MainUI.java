/**
 * Name = Sayed Farzaan Rafi Bhat
 * NSID = kfn036
 * Stu# = 11356043
 */

package com.example.cmpt3812024a3;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;

public class MainUI extends StackPane {

    public MainUI() {

        AppController controller = new AppController();
        MiniViewController mController = new MiniViewController();
        EntityModel model = new EntityModel();
        InteractionModel iModel = new InteractionModel();
        DetailView detailView = new DetailView();
        MiniView miniView = new MiniView();

        // Controller
        controller.setModel(model);
        controller.setIModel(iModel);
        mController.setModel(model);
        mController.setiModel(iModel);

        // Model
        model.addSubscriber(detailView);
        model.addSubscriber(miniView);
        iModel.addSubscriber(detailView);
        iModel.addSubscriber(miniView);

        // DetailView
        detailView.setModel(model);
        detailView.setIModel(iModel);
        detailView.setupEvents(controller);

        // MiniView
        miniView.setModel(model);
        miniView.setIModel(iModel);
        miniView.setupEvents(mController);

        Platform.runLater(() -> {
            detailView.requestFocus();
            iModel.setViewPortWidth(detailView.getViewWidth());
            iModel.setViewPortHeight(detailView.getViewHeight());
        });
        this.getChildren().addAll(detailView, miniView);

    }

}
