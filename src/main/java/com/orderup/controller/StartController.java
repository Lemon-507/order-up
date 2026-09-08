package com.orderup.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class StartController {

    @FXML
    private void onStartButtonClick(ActionEvent event) {
        // TODO: Connect the game-start flow here. This action intentionally has no side effects yet.
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
