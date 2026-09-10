package com.orderup.model;

import lombok.Data;

/**
 * 表示食材类型、当前加工状态和加工进度。
 */
@Data
public class Ingredient extends GameItem {
    private IngredientType ingredientType;
    private IngredientStatus processStatus;
    private double processProgress;

}
