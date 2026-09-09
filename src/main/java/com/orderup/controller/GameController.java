package com.orderup.controller;

import com.orderup.Launcher;
import com.orderup.model.Player;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class GameController implements SceneController {
    private Launcher application;
    private long lastTime = 0;
    @FXML
    private Canvas gameCanvas;
    private AnimationTimer gameLoop;
    Player player=new Player(200, 200, Color.RED);

    @Override
    public void setApplication(Launcher application) {
        this.application = application;
    }
//todo:初始化游戏场景，食材，时间，分数等
    public void startGame() {

    }

    @FXML
    public void initialize() {
        startGame();


        // 监听键盘事件
        // 键盘按下
        gameCanvas.setOnKeyPressed((KeyEvent e)->{
            player.pressedKeys.add(e.getCode());
        });
        // 键盘松开
        gameCanvas.setOnKeyReleased((KeyEvent e)->{
            player.pressedKeys.remove(e.getCode());
        });


        gameCanvas.requestFocus();
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();


        // 游戏循环
        gameLoop = new AnimationTimer() {


            @Override
            public void handle(long now) {
                update(0.016);
                render(gc);
            }
        };





        gameCanvas.setFocusTraversable(true);
        gameLoop.start();
    }


    public void update(double dt) {
        player.P_update(dt);
    }

    //渲染
    private void render(GraphicsContext gc) {
        gc.clearRect(0,0,1280,720);
        player.draw(gc);
    }
   //结束
    public void finishGame() {
        gameLoop.stop();
        application.showResultScene();
    }
}
