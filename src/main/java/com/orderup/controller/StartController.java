package com.orderup.controller;

import com.orderup.Launcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class StartController implements SceneController {
    private Launcher application;

    @Override
    public void setApplication(Launcher application) {
        this.application = application;
    }

    @FXML
    private void onStartButtonClick(ActionEvent event) {

        application.showGameScene();
    }

    @FXML
    private void onSettingsButtonClick(ActionEvent event) {
        // TODO: Open the options screen here. This action intentionally has no side effects yet.

    }

    @FXML
    private void onQuitButtonClick(ActionEvent event) {
        Platform.exit();
    }
}
