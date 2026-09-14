package com.orderup.view;

import com.orderup.config.GameConfig;
import com.orderup.model.GameMap;
import com.orderup.model.ProcessingStation;
import com.orderup.model.Tile;
import com.orderup.model.TileType;
import com.orderup.model.TileVisual;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

/**
 * 绘制地图格子。
 */
public class GameMapView {
    private static final String TILE_ROOT = "/com/orderup/images/map/tiles/";
    private final Map<TileVisual, Image> tileImages = loadTileImages();

    public void render(GraphicsContext graphics, GameMap map) {
        graphics.save();
        graphics.setImageSmoothing(false);
        for (Tile[] row : map.getTiles()) {
            for (Tile tile : row) {
                renderTile(graphics, tile);
            }
        }
        graphics.restore();
    }

    private void renderTile(GraphicsContext graphics, Tile tile) {
        graphics.drawImage(
                tileImages.get(tile.getVisual()),
                tile.getX(),
                tile.getY(),
                tile.getSize(),
                tile.getSize()
        );

        if (tile instanceof ProcessingStation station) {
            renderProcessingProgress(graphics, station);
        }
    }

    private void renderProcessingProgress(
            GraphicsContext graphics,
            ProcessingStation station
    ) {
        if (station.isEmpty()) {
            return;
        }
        double requiredSeconds = station.getType() == TileType.CHOPPING_BOARD
                ? GameConfig.CHOPPING_SECONDS
                : GameConfig.RICE_COOKING_SECONDS;
        double progress = Math.min(1, station.getProgressSeconds() / requiredSeconds);
        double barX = station.getX() + 8;
        double barY = station.getY() + station.getSize() - 11;
        double barWidth = station.getSize() - 16;

        graphics.setFill(Color.web("#263238"));
        graphics.fillRoundRect(barX, barY, barWidth, 6, 4, 4);
        graphics.setFill(Color.web("#7ED957"));
        graphics.fillRoundRect(barX, barY, barWidth * progress, 6, 4, 4);
    }

    private Map<TileVisual, Image> loadTileImages() {
        Map<TileVisual, Image> images = new EnumMap<>(TileVisual.class);
        for (TileVisual visual : TileVisual.values()) {
            String path = TILE_ROOT + visual.getFileName();
            URL resource = GameMapView.class.getResource(path);
            if (resource == null) {
                throw new IllegalStateException("找不到地图瓦片资源：" + path);
            }
            images.put(visual, new Image(resource.toExternalForm()));
        }
        return images;
    }
}
