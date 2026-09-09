package com.orderup.controller;

import com.orderup.Launcher;

public class ResultController implements SceneController {
    private Launcher application;

    @Override
    public void setApplication(Launcher application) {
        this.application = application;
    }

    public void restartGame() {
        application.showGameScene();
    }

    public void returnToMenu() {
        application.showStartScene();
    }
}
