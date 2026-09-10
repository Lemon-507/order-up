package com.orderup.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Player {

    // 玩家逻辑坐标x（水平方向）
    public double x;
    // 玩家逻辑坐标y（垂直方向）
    public double y;
    // 移动速度：像素/秒
    public double speed;
    // GUI矩形图形对象，窗口上看到的方块
    public Rectangle view;
    // 玩家方块宽度常量
    public static final double WIDTH = 40;
    // 玩家方块高度常量
    public static final double HEIGHT = 40;
    public Set<KeyCode> pressedKeys = new HashSet<>();


    public Player(double startX, double startY, Color color) {
        x = startX;
        y = startY;
        // 设置移动速度
        speed = 220;
        // 创建矩形图形
        view = new Rectangle(x, y, WIDTH, HEIGHT);
        // 设置矩形填充颜色
        view.setFill(color);
    }
    public void P_update(double dt){
        // x方向移动增量
        double dx = 0;
        // y方向移动增量
        double dy = 0;
        // W按下，y向上移动
        if (pressedKeys.contains(KeyCode.W)) dy -= 1;
        // S按下，y向下移动
        if (pressedKeys.contains(KeyCode.S)) dy += 1;
        // A按下，x向左移动
        if (pressedKeys.contains(KeyCode.A)) dx -= 1;
        // D按下，x向右移动
        if (pressedKeys.contains(KeyCode.D)) dx += 1;

//        // 斜向移动归一化，防止斜着走速度变快
//        if (dx != 0 || dy != 0) {
//            double len = Math.sqrt(dx * dx + dy * dy);
//            dx /= len;
//            dy /= len;
//        }
        // 根据速度和帧间隔更新玩家逻辑坐标

        x += dx * speed * dt;
        y += dy * speed * dt;
        // 边界检测
        if(x+40>1280){
            x=1280-40;
        }
        if(x<0){
            x=0;
        }
        if(y+40>720){
            y=720-40;
        }
        if(y<0){
            y=0;
        }

    }
    void render() {
        view.setLayoutX(x);
        view.setLayoutY(y);
    }
    public void draw(GraphicsContext gc){
        gc.setFill(Color.RED);
        gc.fillRect(x,y,30,30);
    }

}
