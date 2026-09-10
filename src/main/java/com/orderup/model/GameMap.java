package com.orderup.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class GameMap {
    MapName mapname;
    int mapid=1;
    private static int ROWS=9;
    private static int COLS=13;
    public Tile[][] tiles;
    private final List<GameItem> items = new ArrayList<>();
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
    public void AddItem(int x, int y) {
        items.add(new GameItem(x, y));
    }

    public void AddItem(GameItem item) {
        items.add(item);
    }

    public List<GameItem> getItems() {
        return items;
    }

    public  Tile getTiles(int x, int y){
       return tiles[x][y];
    }

}





