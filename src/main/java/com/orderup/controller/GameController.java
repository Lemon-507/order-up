package com.orderup.controller;

import com.orderup.model.Direction;
import com.orderup.model.GameItem;
import com.orderup.model.GameMap;
import com.orderup.model.InteractBlock;
import com.orderup.model.MapName;
import com.orderup.model.Player;
import com.orderup.model.Tile;
import com.orderup.service.Impl.GameServiceImpl;
import com.orderup.service.Impl.PlayerServiceImpl;
import com.orderup.util.GameTimer;

import java.util.List;

/**
 * 游戏页面的状态协调器，只负责输入、更新和结束条件。
 */
public class GameController {
    private final Player player;
    private final InteractBlock interactBlock;
    private final GameMap gameMap;
    private final GameServiceImpl gameService;
    private final PlayerServiceImpl playerService;
    private final GameTimer gameTimer;
    private final double worldWidth;
    private final double worldHeight;
    private final int gameSeconds;
    private final Runnable onGameFinished;
    private GameItem holdingItem;
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
        this.interactBlock = new InteractBlock();
        this.gameMap = new GameMap(MapName.map1);
        this.gameService = new GameServiceImpl();
        this.playerService = new PlayerServiceImpl();
        this.gameTimer = new GameTimer(this::finishGame);
        gameService.LoadMap(gameMap);
        interactBlock.moveInteractBlock(player);
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
        interactBlock.moveInteractBlock(player);
        refreshItem();
        changeTileState();
        gameTimer.update(deltaSeconds);
    }
//交互方块（Tile）
    public void InteractTile(Tile tile,InteractBlock ib,GameMap gameMap, List<GameItem> items,Player player ){
        if(ib.intersects(tile.getX(),tile.getY(),tile.TileSize,tile.TileSize)){
            tile.Interact(ib,gameMap,items,player);
        }
    }


  //  //交互Item
    public void InteractItem() {
        if (player.isHolding) {
            dropItem();
            return;
        }
        for (Tile[] row : gameMap.getTiles()) {
            for (Tile tile : row) {
                InteractTile(tile, interactBlock, gameMap, gameMap.getItems(), player);
            }
        }

        for (GameItem item : gameMap.getItems()) {
            if (item.isPicked) {
                continue;
            }
            if (interactBlock.intersects(
                    item.getX(),
                    item.getY(),
                    item.getWidth(),
                    item.getHeight()
            )) {
                holdingItem = item;
                player.isHolding = true;
                holdingItem.isPicked = true;
                refreshItem();
                return;
            }
        }

        return;
    }
//丢物品
    private void dropItem() {
        if (holdingItem != null) {
            holdingItem.setX((int) interactBlock.getX());
            holdingItem.setY((int) interactBlock.getY());
            holdingItem.isPicked = false;
        }
        holdingItem = null;
        player.isHolding = false;
    }
//刷新物品

    private void refreshItem() {
        if (holdingItem != null && holdingItem.isPicked) {
            holdingItem.setX((int) interactBlock.getX());
            holdingItem.setY((int) interactBlock.getY());
        }
    }

    private void changeTileState() {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 13; column++) {
                Tile tile = gameMap.tiles[row][column];
                tile.Interactable = interactBlock.intersects(
                        tile.getX(),
                        tile.getY(),
                        tile.TileSize,
                        tile.TileSize
                );
            }
        }
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

    public InteractBlock getInteractBlock() {
        return interactBlock;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public List<GameItem> getItems() {
        return gameMap.getItems();
    }

    public int getRemainingSeconds() {
        return gameTimer.getSecondsCount();
    }

    public void AddItem(GameItem item) {
        gameMap.AddItem(item);
    }

}
