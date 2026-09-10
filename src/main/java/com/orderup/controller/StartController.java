package com.orderup.controller;

public class StartController implements SceneController {
    private SceneNavigator navigator;

    @Override
    public void setNavigator(SceneNavigator navigator) {
        this.navigator = navigator;
    }

    public void startGame() {
        requireNavigator().showGameScene();
    }

    public void openSettings() {
        // TODO: Open the options screen here. This action intentionally has no side effects yet.
    }

    public void quitGame() {
        requireNavigator().exitApplication();
    }

    private SceneNavigator requireNavigator() {
        if (navigator == null) {
            throw new IllegalStateException("Scene navigator has not been configured.");
        }
        return navigator;
    }
}
