package com.orderup.view;

import com.orderup.controller.ResultController;
import com.orderup.controller.SceneNavigator;
import javafx.fxml.FXML;

/**
 * 结算页面的 JavaFX 事件适配层。
 */
public class ResultView implements SceneView {
    private final ResultController controller = new ResultController();

    @Override
    public void setNavigator(SceneNavigator navigator) {
        controller.setNavigator(navigator);
    }

    @FXML
    private void restartGame() {
        controller.restartGame();
    }

    @FXML
    private void returnToMenu() {
        controller.returnToMenu();
    }
}
