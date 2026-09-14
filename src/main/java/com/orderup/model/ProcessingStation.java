package com.orderup.model;

/**
 * 可以暂存一个食材并记录加工进度的设施格子。
 */
public class ProcessingStation extends Tile {
    private Ingredient ingredient;
    private double progressSeconds;

    public ProcessingStation(int row, int column, TileType type) {
        super(row, column, type);
        if (type != TileType.CHOPPING_BOARD && type != TileType.RICE_COOKER) {
            throw new IllegalArgumentException("加工设施类型不正确：" + type);
        }
    }

    public boolean place(Ingredient ingredient) {
        if (ingredient == null || this.ingredient != null) {
            return false;
        }
        this.ingredient = ingredient;
        progressSeconds = 0;
        ingredient.setX(getX() + (getSize() - ingredient.getWidth()) / 2.0);
        ingredient.setY(getY() + (getSize() - ingredient.getHeight()) / 2.0);
        return true;
    }

    public Ingredient take() {
        Ingredient taken = ingredient;
        ingredient = null;
        progressSeconds = 0;
        return taken;
    }

    public void advance(double deltaSeconds) {
        progressSeconds += deltaSeconds;
    }

    public boolean isEmpty() {
        return ingredient == null;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public double getProgressSeconds() {
        return progressSeconds;
    }
}
