package com.orderup.service;

import com.orderup.model.GameMap;
import com.orderup.model.Player;
import com.orderup.model.StationType;
import com.orderup.model.Tile;
import javafx.scene.input.KeyCode;

import java.util.Set;

public class PlayerService {

    // 玩家移动方法
    public void move(Player player, double dt, Set<KeyCode> pressedKeys, GameMap gameMap) {
        // x方向移动增量
        double dx = 0;
        double dy = 0;
        // y方向移动增量
        if (pressedKeys.contains(KeyCode.W)) dy -= 1;
        if (pressedKeys.contains(KeyCode.S)) dy += 1;
        if (pressedKeys.contains(KeyCode.A)) dx -= 1;
        if (pressedKeys.contains(KeyCode.D)) dx += 1;

        player.isSpeedup = pressedKeys.contains(KeyCode.SPACE);
        if(player.isSpeedup){
            player.speed = 400; // 建议放大数值，dt是0.016，40太慢
        }else{
            player.speed = 200;
        }



        // 根据速度和帧间隔更新玩家逻辑坐标
        double moveX = dx * player.speed * dt;
        double moveY = dy * player.speed * dt;

        player.x += moveX;
        player.y += moveY;


        for (int i = 0; i < 9; i++) {
            for(int j = 0; j < 13; j++){
                Tile tile = gameMap.getTiles(i,j);

                if(tile.getType() != StationType.WALL) continue;

                double tx = tile.getX();
                double ty = tile.getY();
                double tw = tile.TileSize;
                double th = tile.TileSize;
                double pw = player.getWIDTH();
                double ph = player.getHEIGHT();


                if (player.y + ph > ty && player.y < ty + th) {
                    // 玩家右侧碰到墙左边缘（缓冲10像素）
                    if (player.x + pw >= tx && player.x + pw <= tx + tile.TileSize/4) {
                        player.x = tx - pw; // 贴到墙左边外侧
                    }
                    // 玩家左侧碰到墙右边缘
                    else if (player.x <= tx + tw && player.x >= tx + tw - tile.TileSize/4) {
                        player.x = tx + tw; // 贴到墙右边外侧
                    }
                }


                if (player.x + pw > tx && player.x < tx + tw) {
                    // 玩家底部撞到墙顶部
                    if (player.y + ph >= ty && player.y + ph <= ty + tile.TileSize/4) {
                        player.y = ty - ph;
                    }
                    // 玩家顶部撞到墙底部
                    if (player.y <= ty + th && player.y >= ty + th - tile.TileSize/4) {
                        player.y = ty + th;
                    }
                }
            }
        }

        // 窗口边界限制（保留）
        if(player.x + player.getWIDTH() > 1280){
            player.x = 1280 - player.getWIDTH();
        }
        if(player.x < 0){
            player.x = 0;
        }
        if(player.y + player.getHEIGHT() > 720){
            player.y = 720 - player.getHEIGHT();
        }
        if(player.y < 0){
            player.y = 0;
        }
    }
}
