package com.orderup.model;



import javafx.scene.canvas.GraphicsContext;
import lombok.Data;

enum Mapname{map1,map2;}



@Data
public class GameMap {
    int mapid=1;
    private static int ROWS=9;
    private static int COLS=13;
    static Tile[][] tiles;
    //初始化所有瓷砖
    public GameMap(){
        tiles=new Tile[ROWS][COLS];
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                tiles[i][j]=new Tile(j,i);
            }
        }
    }
   //瓷砖Type
    public void setTiles(int x, int y, Tile.TileType typeI){
        tiles[x][y].type=typeI;
    }

    public static void drawAllTiles(GraphicsContext gc) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                tiles[r][c].draw(gc);
            }
        }
    }
}





