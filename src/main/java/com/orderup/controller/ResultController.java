package com.orderup.controller;

public class ResultController implements SceneController {
    private SceneNavigator navigator;

    @Override
    public void setNavigator(SceneNavigator navigator) {
        this.navigator = navigator;
    }

    public void restartGame() {
        requireNavigator().showGameScene();
    }

    public void returnToMenu() {
        requireNavigator().showStartScene();
    }

    private SceneNavigator requireNavigator() {
        if (navigator == null) {
            throw new IllegalStateException("Scene navigator has not been configured.");
        }
        return navigator;
    }
}
