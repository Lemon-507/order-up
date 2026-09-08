package com.orderup.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StartController {

    @FXML
    public void onStartButtonClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onSettingsButtonClick(ActionEvent actionEvent) {
    }

    @FXML
    public void onQuitButtonClick(ActionEvent actionEvent) {
        Platform.exit();
    }


}
