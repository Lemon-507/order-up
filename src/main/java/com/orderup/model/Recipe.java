package com.orderup.model;

import java.util.Objects;

/**
 * 不可变菜谱配置。
 */
public class Recipe {
    private final DishType dishType;
    private final int baseScore;
    private final double timeLimitSeconds;

    public Recipe(DishType dishType, int baseScore, double timeLimitSeconds) {
        this.dishType = Objects.requireNonNull(dishType);
        this.baseScore = baseScore;
        this.timeLimitSeconds = timeLimitSeconds;
    }

    public DishType getDishType() {
        return dishType;
    }

    public String getDishName() {
        return dishType.getDisplayName();
    }

    public int getBaseScore() {
        return baseScore;
    }

    public double getTimeLimitSeconds() {
        return timeLimitSeconds;
    }
}
