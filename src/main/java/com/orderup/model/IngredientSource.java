package com.orderup.model;

import java.util.Objects;

/**
 * 固定在地图上的食材源，记录玩家交互时应生成的食材类型。
 */
public class IngredientSource extends Tile {
    private final IngredientType ingredientType;

    public IngredientSource(int row, int column, IngredientType ingredientType) {
        super(row, column, TileType.INGREDIENT_SOURCE, visualFor(ingredientType));
        this.ingredientType = Objects.requireNonNull(ingredientType);
    }

    private static TileVisual visualFor(IngredientType ingredientType) {
        return switch (Objects.requireNonNull(ingredientType)) {
            case FISH -> TileVisual.FISH_STORAGE;
            case RICE -> TileVisual.RICE_STORAGE;
            case KELP -> TileVisual.NORI_STORAGE;
        };
    }

    public IngredientType getIngredientType() {
        return ingredientType;
    }
}
