package com.orderup.model;



import javafx.scene.canvas.GraphicsContext;
import lombok.Data;


@Data
public class GameMap {
    Mapname mapname;
    int mapid=1;
    private static int ROWS=9;
    private static int COLS=13;
    public Tile[][] tiles;
    //初始化所有瓷砖
    public GameMap(Mapname mapname1){
        tiles=new Tile[ROWS][COLS];
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                tiles[i][j]=new Tile(j,i);
            }
        }
        mapname=mapname1;
    }

    public Mapname getMapname() {
        return mapname;
    }

    //瓷砖Type
    public void setTiles(int x, int y, StationType typeI){
        tiles[x][y].TileType=typeI;
    }

    public  Tile getTiles(int x, int y){
       return tiles[x][y];
    }

    public  void drawAllTiles(GraphicsContext gc) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                tiles[r][c].draw(gc);
            }
        }
    }
}





