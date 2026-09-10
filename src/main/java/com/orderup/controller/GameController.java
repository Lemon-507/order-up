package com.orderup.controller;

import com.orderup.Launcher;
import com.orderup.model.GameMap;

import com.orderup.model.Player;
import com.orderup.model.StationType;
import com.orderup.service.GameService;
import com.orderup.service.PlayerService;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import com.orderup.util.GameTimer;


import static com.orderup.model.Mapname.map1;
import static java.nio.file.Files.move;

public class GameController implements SceneController, GameService {
    private Launcher application;
    private long lastTime = 0;
    @FXML
    private Canvas gameCanvas;
    private AnimationTimer gameLoop;
    Player player = new Player(200, 200, Color.RED);
    PlayerService playerService = new PlayerService();
    private GameTimer gameTimer;
    @FXML
    private Label timeLabel;

    // 地图作为成员变量，只实例一次，传入tileSize=50，保证13行全部放进720高度画布
    private GameMap gameMap;

    @Override
    public void setApplication(Launcher application) {
        this.application = application;
        gameTimer = new GameTimer(timeLabel, this::finishGame);
        gameTimer.startCountDown(60);
    }
    public void LoadMap(GameMap gameMap) {
        if(gameMap.getMapname()==map1) {
            for (int i = 0; i < 9; i++) {
                gameMap.setTiles(i, 0, StationType.WALL);
            }
            for (int i = 0; i < 9; i++) {
                gameMap.setTiles(i, 12, StationType.WALL);
            }
            for (int i = 0; i < 13; i++) {
                gameMap.setTiles(0, i, StationType.WALL);
            }
            for (int i = 0; i < 13; i++) {
                gameMap.setTiles(8, i, StationType.WALL);
            }
           for (int i = 3; i < 5; i++) {
                gameMap.setTiles(i, 5, StationType.WALL);
           }
        }
    }

    //todo:初始化游戏场景，食材，时间，分数等
    public void startGame() {
        // 在这里初始化地图
        gameMap = new GameMap(map1);
        LoadMap(gameMap);
    }

    @FXML
    public void initialize() {
        startGame();
        
        // 监听键盘事件
        gameCanvas.setOnKeyPressed((KeyEvent e) -> {
            player.pressedKeys.add(e.getCode());
        });
        gameCanvas.setOnKeyReleased((KeyEvent e) -> {
            player.pressedKeys.remove(e.getCode());
        });

        gameCanvas.requestFocus();

        // 游戏循环
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(0.016);
                // 每一帧都重新获取gc（正确做法）
                GraphicsContext gc = gameCanvas.getGraphicsContext2D();
                render(gc);
            }
        };

        gameCanvas.setFocusTraversable(true);
        gameLoop.start();
    }

    public void update(double dt) {
        playerService.move(player,dt,player.pressedKeys,gameMap);
    }

    //渲染：清空画布 → 画网格 → 画玩家
    private void render(GraphicsContext gc) {
        // 只清空画布一次！
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        // 第一层绘制网格
        if (gameMap != null) {
            gameMap.drawAllTiles(gc);
        }
        // 第二层绘制玩家，画在网格上面
        player.draw(gc);
    }

    //结束
    public void finishGame() {
        gameLoop.stop();
        gameTimer.stop();
        application.showResultScene();
    }
}
