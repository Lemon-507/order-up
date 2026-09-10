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

    public void onSettingsButtonClick() {
        // TODO: Open the options screen here. This action intentionally has no side effects yet.
    }

    public void onQuitButtonClick() {
        quitGame.run();
    }
}
