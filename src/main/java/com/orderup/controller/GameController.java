package com.orderup.controller;

import com.orderup.Launcher;

public class GameController implements SceneController {
    private Launcher application;

    @Override
    public void setApplication(Launcher application) {
        this.application = application;
    }

    /**
     * 游戏倒计时结束后调用，进入结算页。
     */
    public void finishGame() {
        application.showResultScene();
    }
}
