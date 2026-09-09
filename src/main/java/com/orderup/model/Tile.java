package com.orderup.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;

public class Tile {
      int rownumber;//行
      int colnumber;//列
      enum TileType{Floor,Box,Wall,Cooker};
      TileType type=TileType.Floor;
      int TileSize=80;

      Tile(int x, int y){
          rownumber=y;
          colnumber=x;
      }
      public int getX() {
          return colnumber*TileSize;
      }
      public int getY() {
          return rownumber*TileSize;
      }

    public void draw(GraphicsContext gc) {
        int x = getX();
        int y = getY();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeRect(x, y, TileSize, TileSize);
    }








}
