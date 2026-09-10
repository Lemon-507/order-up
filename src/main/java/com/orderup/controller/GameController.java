package com.orderup.controller;

import com.orderup.model.Direction;
import com.orderup.model.GameMap;
import com.orderup.model.Mapname;
import com.orderup.model.Player;
import com.orderup.service.Impl.GameServiceImpl;
import com.orderup.service.Impl.PlayerServiceImpl;
import com.orderup.util.GameTimer;

/**
 * 游戏页面的状态协调器，只负责输入、更新和结束条件。
 */
public class GameController {
    private final Player player;
    private final GameMap gameMap;
    private final GameServiceImpl gameService;
    private final PlayerServiceImpl playerService;
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
        this.gameMap = new GameMap(Mapname.map1);
        this.gameService = new GameServiceImpl();
        this.playerService = new PlayerServiceImpl();
        this.gameTimer = new GameTimer(this::finishGame);
        gameService.LoadMap(gameMap);
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
        playerService.move(player, deltaSeconds, worldWidth, worldHeight, gameMap);
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

    public GameMap getGameMap() {
        return gameMap;
    }

    public int getRemainingSeconds() {
        return gameTimer.getSecondsCount();
    }
}
