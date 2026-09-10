package com.orderup.controller;

import com.orderup.model.Direction;
import com.orderup.model.Player;
import com.orderup.util.GameTimer;

/**
 * 游戏页面的状态协调器，只负责输入、更新和结束条件。
 */
public class GameController {
    private final Player player;
    private final GameTimer gameTimer;
    private final double worldWidth;
    private final double worldHeight;
    private final int gameSeconds;
    private final Runnable onGameFinished;
    private boolean finished;

    public GameController(
            double worldWidth,
            double worldHeight,
            int gameSeconds,
            Runnable onGameFinished
    ) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.gameSeconds = gameSeconds;
        this.onGameFinished = onGameFinished;
        this.player = new Player(200, 200);
        this.gameTimer = new GameTimer(this::finishGame);
    }

    public void startGame() {
        finished = false;
        gameTimer.startCountDown(gameSeconds);
    }

    public void press(Direction direction) {
        player.press(direction);
    }

    public void release(Direction direction) {
        player.release(direction);
    }

    public void clearInput() {
        player.clearMovement();
    }

    public void update(double deltaSeconds) {
        if (finished) {
            return;
        }
        player.update(deltaSeconds, worldWidth, worldHeight);
        gameTimer.update(deltaSeconds);
    }

    public void finishGame() {
        if (finished) {
            return;
        }

        finished = true;
        gameTimer.stop();
        player.clearMovement();
        onGameFinished.run();
    }

    public void stopGame() {
        finished = true;
        gameTimer.stop();
        player.clearMovement();
    }

    public Player getPlayer() {
        return player;
    }

    public int getRemainingSeconds() {
        return gameTimer.getSecondsCount();
    }
}
