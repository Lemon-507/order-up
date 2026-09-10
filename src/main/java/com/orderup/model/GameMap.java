package com.orderup.model;

import lombok.Data;


@Data
public class GameMap {
    MapName mapname;
    int mapid=1;
    private static int ROWS=9;
    private static int COLS=13;
    public Tile[][] tiles;
    //初始化所有瓷砖
    public GameMap(MapName mapName1){
        tiles=new Tile[ROWS][COLS];
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                tiles[i][j]=new Tile(j,i);
            }
        }
        mapname= mapName1;
    }

    public MapName getMapname() {
        return mapname;
    }

    //瓷砖Type
    public void setTiles(int x, int y, TileType typeI){
        tiles[x][y].TileType=typeI;
    }

    public  Tile getTiles(int x, int y){
       return tiles[x][y];
    }

}





