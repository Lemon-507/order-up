package com.orderup.model;

/**
 * 可加工的食材。
 */
public class Ingredient extends GameItem {
    private final IngredientType type;
    private IngredientStatus status = IngredientStatus.RAW;
    private double processProgress;

    public Ingredient(IngredientType type, double x, double y) {
        super(x, y);
        this.type = type;
    }

    public IngredientType getType() {
        return type;
    }

    public IngredientStatus getStatus() {
        return status;
    }

    public void setStatus(IngredientStatus status) {
        this.status = status;
    }

    public double getProcessProgress() {
        return processProgress;
    }

    public void setProcessProgress(double processProgress) {
        this.processProgress = processProgress;
    }
}
