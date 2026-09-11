package com.orderup.model;

/**
 * 桌子：最多放置一个盘子或一份食材，二者不可并存、不可堆叠。
 * 玩家是否空手、是否朝向桌子由调用方判断；桌子只处理占用、拾取和装盘转发。
 */
public class Table extends Tile {

    private Plate plate;
    private Ingredient ingredient;

    public Table(int x, int y) {
        super(x, y);
        this.tileType = TileType.TABLE;
    }

    public boolean isVacant() {
        return plate == null && ingredient == null;
    }

    public boolean hasIngredient() {
        return ingredient != null;
    }

    public Ingredient peekIngredient() {
        return ingredient;
    }

    public boolean hasPlate() {
        return plate != null;
    }

    public Plate peekPlate() {
        return plate;
    }

    /**
     * 仅空桌可放盘子。桌上已有盘子或食材时失败，不改变状态。
     */
    public boolean placePlate(Plate plate) {
        if (plate == null || !isVacant()) {
            return false;
        }
        this.plate = plate;
        snapToTable(plate);
        return true;
    }

    public Plate takePlate() {
        Plate taken = this.plate;
        this.plate = null;
        return taken;
    }

    /**
     * 仅空桌可放食材，任意加工状态均可。桌上已有盘子或食材时失败。
     */
    public boolean placeIngredient(Ingredient ingredient) {
        if (ingredient == null || !isVacant()) {
            return false;
        }
        this.ingredient = ingredient;
        snapToTable(ingredient);
        return true;
    }

    public Ingredient takeIngredient() {
        Ingredient taken = this.ingredient;
        this.ingredient = null;
        return taken;
    }

    /**
     * 空手取走桌上物品：优先盘子，否则食材。空桌返回 {@code null}。
     */
    public GameItem take() {
        if (hasPlate()) {
            return takePlate();
        }
        if (hasIngredient()) {
            return takeIngredient();
        }
        return null;
    }

    /**
     * 手持食材与桌子交互。
     * 桌上有盘子：可装盘则放入桌上盘子，否则无响应。
     * 空桌：放下该食材。桌上已有食材：无响应。
     */
    public boolean interactWithIngredient(Ingredient heldIngredient) {
        if (heldIngredient == null || hasIngredient()) {
            return false;
        }
        if (hasPlate()) {
            return plate.addIngredient(heldIngredient);
        }
        return placeIngredient(heldIngredient);
    }

    /**
     * 手持盘子与桌子交互。
     * 桌上有食材：可装盘则装入玩家手中盘子并清空桌子，否则无响应。
     * 空桌：放下盘子。桌上已有盘子：无法放置。
     */
    public boolean interactWithPlate(Plate heldPlate) {
        if (heldPlate == null) {
            return false;
        }
        if (hasIngredient()) {
            return transferIngredientTo(heldPlate);
        }
        return placePlate(heldPlate);
    }

    private boolean transferIngredientTo(Plate heldPlate) {
        if (!heldPlate.addIngredient(ingredient)) {
            return false;
        }
        ingredient = null;
        return true;
    }

    private void snapToTable(GameItem item) {
        item.setX(getX());
        item.setY(getY());
        item.setWidth(TileSize);
        item.setHeight(TileSize);
    }
}
