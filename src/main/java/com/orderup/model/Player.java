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
    // 玩家移动速度
    public double speed;

    public boolean isSpeedup;
    // GUI矩形图形对象，窗口上看到的方块
    public Rectangle view;
    // 玩家方块宽度常量
    public  final double WIDTH = 40;
    // 玩家方块高度常量
    public final double HEIGHT = 40;
    public Set<KeyCode> pressedKeys = new HashSet<>();

    public double getWIDTH() {
        return WIDTH;
    }

    public double getHEIGHT() {

        return HEIGHT;
    }

    public Player(double startX, double startY, Color color) {
        x = startX;
        y = startY;
        // 设置移动速度
        if(isSpeedup){
            speed = 300;
        }else{
            speed = 200;
        }
        speed = 220;
        // 创建矩形图形
        view = new Rectangle(x, y, WIDTH, HEIGHT);
        // 设置矩形填充颜色
        view.setFill(color);
    }
    // ————————————————————————————————————————————————————————玩家移动见playerService——————————————————————————————————————————————————————————————

public boolean isColliding(Tile tile) {
        if(x>tile.getX()&&x<tile.getX()+tile.TileSize){
            return true;
        } else if (y>tile.getY()&&y<tile.getY()+tile.TileSize) {
            return true;
        }
        return false;
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
