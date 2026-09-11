package com.orderup.model;

public class Tile {
      int rowNumber;//行
      int colNumber;//列
      TileType TileType;
      public boolean Interactable=false;//是否可交互

      public int TileSize=80;

      Tile(int x, int y){
          rowNumber =y;
          colNumber =x;
          TileType= com.orderup.model.TileType.FLOOR;
      }
      public int getX() {
          return colNumber *TileSize;
      }
      public int getY() {
          return rowNumber *TileSize;
      }

    public TileType getType() {
        return TileType;
    }
}
