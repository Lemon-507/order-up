package com.orderup.controller;

import com.orderup.Launcher;
import com.orderup.util.GameTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameController implements SceneController {
    private Launcher application;
    private GameTimer gameTimer;

    @FXML
    private Label timeLabel;

    @Override
    public void setApplication(Launcher application) {
        this.application = application;

        gameTimer = new GameTimer(timeLabel, this::finishGame);
        gameTimer.startCountDown(60);
    }

    /**
     * 倒计时结束后进入结算页。
     */
    public void finishGame() {
        gameTimer.stop();
        application.showResultScene();
    }
}