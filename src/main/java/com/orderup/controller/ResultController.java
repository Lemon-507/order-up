package com.orderup.controller;

public class ResultController {
    private Runnable restartGame = () -> { };
    private Runnable returnToMenu = () -> { };

    public void configure(Runnable restartGame, Runnable returnToMenu) {
        this.restartGame = restartGame;
        this.returnToMenu = returnToMenu;
    }

    public void restartGame() {
        restartGame.run();
    }

    public void returnToMenu() {
        returnToMenu.run();
    }
}
