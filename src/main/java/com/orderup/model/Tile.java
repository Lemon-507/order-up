package com.orderup.model;

import com.orderup.config.GameConfig;

/**
 * 地图中的一个不可拾取格子。
 */
public class Tile {
    private final int row;
    private final int column;
    private final TileType type;
    private final TileVisual visual;
    private boolean interactable;

    public Tile(int row, int column) {
        this(row, column, TileType.FLOOR, TileVisual.floorAt(row, column));
    }

    public Tile(int row, int column, TileType type) {
        this(row, column, type, TileVisual.defaultFor(type));
    }

    public Tile(int row, int column, TileType type, TileVisual visual) {
        this.row = row;
        this.column = column;
        this.type = type;
        this.visual = visual;
    }

    public int getX() {
        return column * GameConfig.TILE_SIZE;
    }

    public int getY() {
        return row * GameConfig.TILE_SIZE;
    }

    public int getSize() {
        return GameConfig.TILE_SIZE;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public TileType getType() {
        return type;
    }

    public TileVisual getVisual() {
        return visual;
    }

    public boolean isInteractable() {
        return interactable;
    }

    public void setInteractable(boolean interactable) {
        this.interactable = interactable;
    }
}
