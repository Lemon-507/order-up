package com.orderup.service.Impl;

import com.orderup.model.Direction;
import com.orderup.model.GameMap;
import com.orderup.model.Player;
import com.orderup.model.TileType;
import com.orderup.model.Tile;
import com.orderup.service.PlayerService;

public class PlayerServiceImpl implements PlayerService {
    @Override
    public void move(
            Player player,
            double deltaSeconds,
            double worldWidth,
            double worldHeight,
            GameMap gameMap
    ) {
        double dx = 0;
        double dy = 0;

        if (player.pressedDirections.contains(Direction.UP)) {
            dy -= 1;
        }
        if (player.pressedDirections.contains(Direction.DOWN)) {
            dy += 1;
        }
        if (player.pressedDirections.contains(Direction.LEFT)) {
            dx -= 1;
        }
        if (player.pressedDirections.contains(Direction.RIGHT)) {
            dx += 1;
        }

        player.x += dx * player.getSpeed() * deltaSeconds;
        player.y += dy * player.getSpeed() * deltaSeconds;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 13; j++) {
                Tile tile = gameMap.getTiles(i, j);
                if (tile.getType() != TileType.WALL) {
                    continue;
                }

                double tileX = tile.getX();
                double tileY = tile.getY();
                double tileSize = tile.TileSize;

                if (player.y + Player.HEIGHT > tileY && player.y < tileY + tileSize) {
                    if (player.x + Player.WIDTH >= tileX
                            && player.x + Player.WIDTH <= tileX + tileSize / 4) {
                        player.x = tileX - Player.WIDTH;
                    } else if (player.x <= tileX + tileSize
                            && player.x >= tileX + tileSize - tileSize / 4) {
                        player.x = tileX + tileSize;
                    }
                }

                if (player.x + Player.WIDTH > tileX && player.x < tileX + tileSize) {
                    if (player.y + Player.HEIGHT >= tileY
                            && player.y + Player.HEIGHT <= tileY + tileSize / 4) {
                        player.y = tileY - Player.HEIGHT;
                    } else if (player.y <= tileY + tileSize
                            && player.y >= tileY + tileSize - tileSize / 4) {
                        player.y = tileY + tileSize;
                    }
                }
            }
        }

        player.x = clamp(player.x, 0, worldWidth - Player.WIDTH);
        player.y = clamp(player.y, 0, worldHeight - Player.HEIGHT);
    }

    private double clamp(double value, double minimum, double maximum) {
        return Math.max(minimum, Math.min(value, maximum));
    }
}
