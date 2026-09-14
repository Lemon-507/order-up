package com.orderup.model;

public enum TileType {
    FLOOR(false),
    TABLE(true),
    INGREDIENT_SOURCE(true),
    CHOPPING_BOARD(true),
    RICE_COOKER(true);

    private final boolean blocking;

    TileType(boolean blocking) {
        this.blocking = blocking;
    }

    public boolean isBlocking() {
        return blocking;
    }

}
