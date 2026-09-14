package com.orderup.view;

import com.orderup.config.GameConfig;
import com.orderup.model.GameMap;
import com.orderup.model.IngredientSource;
import com.orderup.model.ProcessingStation;
import com.orderup.model.Tile;
import com.orderup.model.TileType;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * 绘制地图格子。
 */
public class GameMapView {
    public void render(GraphicsContext graphics, GameMap map) {
        for (Tile[] row : map.getTiles()) {
            for (Tile tile : row) {
                renderTile(graphics, tile);
            }
        }
    }

    private void renderTile(GraphicsContext graphics, Tile tile) {
        graphics.setFill(fillColor(tile));
        graphics.fillRect(tile.getX(), tile.getY(), tile.getSize(), tile.getSize());

        graphics.setStroke(Color.BLACK);
        graphics.setLineWidth(1);
        graphics.strokeRect(tile.getX(), tile.getY(), tile.getSize(), tile.getSize());

        if (tile instanceof IngredientSource source) {
            renderSourceLabel(graphics, source);
        } else if (tile instanceof ProcessingStation station) {
            renderFacilityLabel(graphics, station);
            renderProcessingProgress(graphics, station);
        } else if (tile.getType() == TileType.ORDER_COUNTER
                || tile.getType() == TileType.PLATE_RETURN) {
            renderCounterLabel(graphics, tile);
        }
    }

    private Color fillColor(Tile tile) {
        if (tile instanceof IngredientSource source) {
            return IngredientPalette.sourceColor(source.getIngredientType());
        }
        if (tile.getType() == TileType.TABLE) {
            return tile.isInteractable() ? Color.GRAY : Color.BLACK;
        }
        return switch (tile.getType()) {
            case CHOPPING_BOARD -> Color.web("#B87945");
            case RICE_COOKER -> Color.web("#AEB9C2");
            case ORDER_COUNTER -> Color.web("#2D6F73");
            case PLATE_RETURN -> Color.web("#6B5B86");
            default -> Color.LIGHTGRAY;
        };
    }

    private void renderSourceLabel(GraphicsContext graphics, IngredientSource source) {
        graphics.setFill(IngredientPalette.textColor(source.getIngredientType()));
        graphics.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        graphics.setTextAlign(TextAlignment.CENTER);
        graphics.setTextBaseline(VPos.CENTER);
        graphics.fillText(
                IngredientPalette.label(source.getIngredientType()),
                source.getX() + source.getSize() / 2,
                source.getY() + source.getSize() / 2
        );
    }

    private void renderFacilityLabel(GraphicsContext graphics, Tile tile) {
        graphics.setFill(tile.getType() == TileType.CHOPPING_BOARD
                ? Color.web("#FFF2DA")
                : Color.web("#263238"));
        graphics.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        graphics.setTextAlign(TextAlignment.CENTER);
        graphics.setTextBaseline(VPos.CENTER);
        graphics.fillText(
                tile.getType() == TileType.CHOPPING_BOARD ? "切菜板" : "电饭煲",
                tile.getX() + tile.getSize() / 2,
                tile.getY() + tile.getSize() / 2
        );
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

    private void renderCounterLabel(GraphicsContext graphics, Tile tile) {
        graphics.setFill(Color.WHITE);
        graphics.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        graphics.setTextAlign(TextAlignment.CENTER);
        graphics.setTextBaseline(VPos.CENTER);
        graphics.fillText(
                tile.getType() == TileType.ORDER_COUNTER ? "出餐口" : "盘子区",
                tile.getX() + tile.getSize() / 2,
                tile.getY() + tile.getSize() / 2
        );
    }
}
