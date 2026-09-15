package com.orderup.model;

/**
 * 瓦片的美术外观。逻辑类型由 {@link TileType} 管理，外观单独记录，
 * 以便同为阻挡物的桌面、花盆和柜台使用不同图片。
 */
public enum TileVisual {
    FLOOR_LIGHT("floor_light.png"),
    FLOOR_DARK("floor_dark.png"),
    EMPTY_TABLE("empty_table.png"),
    SQUARE_PLANTER("square_planter.png"),
    ROUND_PLANTER("round_planter.png"),
    RICE_COOKER("rice_cooker.png"),
    BOWL_DISPENSER("bowl_dispenser.png"),
    CUTTING_BOARD("cutting_board.png"),
    FISH_STORAGE("fish_storage.png"),
    RICE_STORAGE("rice_storage.png"),
    NORI_STORAGE("nori_storage.png"),
    TRASH_BIN("trash_bin.png"),
    CASHIER_COUNTER_LEFT("cashier_counter_left.png"),
    CASHIER_COUNTER_RIGHT("cashier_counter_right.png"),
    SERVING_COUNTER("serving_counter.png");

    private final String fileName;

    TileVisual(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public static TileVisual floorAt(int row, int column) {
        return (row + column) % 2 == 0 ? FLOOR_LIGHT : FLOOR_DARK;
    }

    public static TileVisual defaultFor(TileType type) {
        return switch (type) {
            case FLOOR -> FLOOR_LIGHT;
            case TABLE -> EMPTY_TABLE;
            case INGREDIENT_SOURCE -> FISH_STORAGE;
            case CHOPPING_BOARD -> CUTTING_BOARD;
            case RICE_COOKER -> RICE_COOKER;
            case ORDER_COUNTER -> SERVING_COUNTER;
            case PLATE_RETURN -> BOWL_DISPENSER;
            case TRASH_CAN -> TRASH_BIN;
        };
    }
}
