package com.orderup.model;

public class Tile {
      int rownumber;//行
      int colnumber;//列
      TileType TileType;
      public boolean Interactable=false;//是否可交互

      public int TileSize=80;

      Tile(int x, int y){
          rownumber=y;
          colnumber=x;
          TileType= com.orderup.model.TileType.FLOOR;
      }
      public int getX() {
          return colnumber*TileSize;
      }
      public int getY() {
          return rownumber*TileSize;
      }

    public TileType getType() {
        return TileType;
    }
}
