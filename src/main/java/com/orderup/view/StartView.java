package com.orderup.view;

import com.orderup.controller.SceneNavigator;
import com.orderup.controller.StartController;
import javafx.fxml.FXML;

/**
 * 开始页面的 JavaFX 事件适配层。
 */
public class StartView implements SceneView {
    private final StartController controller = new StartController();

    @Override
    public void setNavigator(SceneNavigator navigator) {
        controller.setNavigator(navigator);
    }

    @FXML
    private void onStartButtonClick() {
        controller.startGame();
    }

    @FXML
    private void onSettingsButtonClick() {
        controller.openSettings();
    }

    @FXML
    private void onQuitButtonClick() {
        controller.quitGame();
    }
}
