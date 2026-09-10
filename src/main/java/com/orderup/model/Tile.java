package com.orderup.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;



public class Tile {
      int rownumber;//行
      int colnumber;//列
      StationType TileType;

      public int TileSize=80;

      Tile(int x, int y){
          rownumber=y;
          colnumber=x;
          TileType=StationType.FLOOR;
      }
      public int getX() {
          return colnumber*TileSize;
      }
      public int getY() {
          return rownumber*TileSize;
      }

    public StationType getType() {
        return TileType;
    }

    public void draw(GraphicsContext gc) {
        int x = getX();
        int y = getY();

        if (TileType == StationType.WALL) {
            //墙体填充黑色
            gc.setFill(Color.BLACK);
            gc.fillRect(x, y, TileSize, TileSize);
            //墙体边框（深灰色）
            gc.setStroke(Color.DARKGRAY);
            gc.setLineWidth(1);
            gc.strokeRect(x, y, TileSize, TileSize);
        } else {
            //地板：浅灰色填充 + 黑色网格线
            gc.setFill(Color.LIGHTGRAY);
            gc.fillRect(x, y, TileSize, TileSize);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            gc.strokeRect(x, y, TileSize, TileSize);
        }
    }








}
