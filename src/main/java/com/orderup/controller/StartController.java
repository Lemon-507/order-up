package com.orderup.controller;

public class StartController {
    private Runnable startGame = () -> { };
    private Runnable quitGame = () -> { };

    public void configure(Runnable startGame, Runnable quitGame) {
        this.startGame = startGame;
        this.quitGame = quitGame;
    }

    public void onStartButtonClick() {
        startGame.run();
    }

    public void onQuitButtonClick() {
        quitGame.run();
    }
}
