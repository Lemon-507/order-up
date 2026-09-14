package com.orderup.service;

import com.orderup.config.GameConfig;
import com.orderup.model.*;

public interface GameService {
    /**
     * 按默认关卡创建并初始化地图。
     *
     * @return 包含地板、设施和初始物品的地图
     */
    default GameMap createMap() {
        return createMap(GameConfig.DEFAULT_LEVEL);
    }

    /**
     * 按指定关卡创建并初始化地图。
     *
     * @param level 关卡编号
     * @return 包含该关卡食材源和设施的地图
     */
    GameMap createMap(int level);

    /**
     * 在地图上放置边界桌面、中央桌面、食材源、加工设施、垃圾桶和初始盘子。
     *
     * @param map 要配置的空白地图
     * @param level 关卡编号
     */
    default void configureMap(GameMap map, int level) {
        for (int row = 0; row < GameConfig.MAP_ROWS; row++) {
            placeTable(map, row, 0);
            placeTable(map, row, GameConfig.MAP_COLUMNS - 1);
        }
        for (int column = 0; column < GameConfig.MAP_COLUMNS; column++) {
            placeTable(map, 0, column);
            placeTable(map, GameConfig.MAP_ROWS - 1, column);
        }
        placeTable(map, 3, 3);
        placeTable(map, 3, 4);
        placeTable(map, 3, 8);
        placeTable(map, 3, 9);

        // 装饰台仍阻挡移动，但不作为可放置物品的普通桌面。
        placeDecoration(map, 0, 1, TileVisual.SQUARE_PLANTER);
        placeDecoration(map, 0, 2, TileVisual.ROUND_PLANTER);
        placeDecoration(map, 0, 10, TileVisual.ROUND_PLANTER);
        placeDecoration(map, 0, 11, TileVisual.SQUARE_PLANTER);
        placeDecoration(map, 8, 3, TileVisual.SQUARE_PLANTER);
        placeDecoration(map, 8, 6, TileVisual.SQUARE_PLANTER);
        placeTable(map, 8, 4, TileVisual.CASHIER_COUNTER_LEFT);
        placeTable(map, 8, 5, TileVisual.CASHIER_COUNTER_RIGHT);

        for (GameConfig.IngredientSourceConfig source : GameConfig.getIngredientSources(level)) {
            map.setTile(
                    source.row(),
                    source.column(),
                    new IngredientSource(source.row(), source.column(), source.ingredientType())
            );
        }
        for (GameConfig.FacilityConfig facility : GameConfig.getFacilities(level)) {
            map.setTile(
                    facility.row(),
                    facility.column(),
                    createFacility(facility)
            );
        }
        for (int count = 0; count < GameConfig.PLATE_COUNT; count++) {
            addEmptyPlate(map);
        }
    }

    /**
     * 将地图的指定格子替换为桌面。
     *
     * @param map 目标地图
     * @param row 格子行号
     * @param column 格子列号
     */
    default void placeTable(GameMap map, int row, int column) {
        map.setTile(row, column, new Table(row, column));
    }

    default void placeTable(GameMap map, int row, int column, TileVisual visual) {
        map.setTile(row, column, new Table(row, column, visual));
    }

    default void placeDecoration(GameMap map, int row, int column, TileVisual visual) {
        map.setTile(row, column, new Tile(row, column, TileType.TABLE, visual));
    }

    /** 创建配置指定的普通设施或加工设施。 */
    default Tile createFacility(GameConfig.FacilityConfig facility) {
        return switch (facility.tileType()) {
            case CHOPPING_BOARD, RICE_COOKER -> new ProcessingStation(
                    facility.row(),
                    facility.column(),
                    facility.tileType()
            );
            default -> new Tile(
                    facility.row(),
                    facility.column(),
                    facility.tileType(),
                    facility.tileVisual()
            );
        };
    }

    /** 在固定盘子区的第一个空位生成一个空盘子。 */
    default Plate addEmptyPlate(GameMap map) {
        for (int slot = 0; slot < GameConfig.PLATE_COUNT; slot++) {
            Plate plate = new Plate();
            double x = GameConfig.PLATE_RETURN_START_COLUMN * GameConfig.TILE_SIZE
                    + (GameConfig.TILE_SIZE - plate.getWidth()) / 2.0
                    + (slot - 1) * 5;
            double y = GameConfig.PLATE_RETURN_ROW * GameConfig.TILE_SIZE
                    + (GameConfig.TILE_SIZE - plate.getHeight()) / 2.0
                    + slot * 2;
            boolean occupied = map.getItems().stream()
                    .filter(Plate.class::isInstance)
                    .anyMatch(item -> item.getX() == x && item.getY() == y);
            if (!occupied) {
                plate.setX(x);
                plate.setY(y);
                return map.addItem(plate);
            }
        }
        throw new IllegalStateException("盘子区已没有空位");
    }
}
