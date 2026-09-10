package com.orderup.view;

import com.orderup.model.GameMap;
import com.orderup.model.Tile;
import com.orderup.model.TileType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * 地图显示层，负责绘制地板和墙体格子。
 */
public class GameMapView {
    public void render(GraphicsContext graphics, GameMap gameMap) {
        for (Tile[] row : gameMap.tiles) {
            for (Tile tile : row) {
                renderTile(graphics, tile);
            }
        }
    }

    private void renderTile(GraphicsContext graphics, Tile tile) {
        int x = tile.getX();
        int y = tile.getY();

        if (tile.getType() == TileType.WALL) {
            graphics.setFill(Color.BLACK);
            graphics.fillRect(x, y, tile.TileSize, tile.TileSize);
            graphics.setStroke(Color.DARKGRAY);
        } else {
            graphics.setFill(Color.LIGHTGRAY);
            graphics.fillRect(x, y, tile.TileSize, tile.TileSize);
            graphics.setStroke(Color.BLACK);
        }

        graphics.setLineWidth(1);
        graphics.strokeRect(x, y, tile.TileSize, tile.TileSize);
    }
}
