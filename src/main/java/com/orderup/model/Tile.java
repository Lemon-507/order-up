package com.orderup.model;

import java.util.List;

public class Tile {
      int rownumber;//行
      int colnumber;//列
      TileType tileType;
      public boolean Interactable=false;//是否可交互

      public int TileSize=80;

      Tile(int x, int y){
          rownumber=y;
          colnumber=x;
          tileType= com.orderup.model.TileType.FLOOR;
      }
      public void Interact(InteractBlock ib,GameMap gameMap, List<GameItem> items,Player player){

      }

      public int getX() {
          return colnumber*TileSize;
      }
      public int getY() {
          return rownumber*TileSize;
      }

    public TileType getType() {
        return tileType;
    }
}
