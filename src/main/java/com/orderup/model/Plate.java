package com.orderup.model;

import com.orderup.util.CalculateScore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存已装入盘中的食材组合，用于形成菜肴并提交订单。
 * <p>
 * 盘子只提供领域方法：装盘校验、菜肴识别、送餐判定与计分、以及桌子占用约定。
 * 玩家拾取/手持移动、朝向检测和订单列表查找由调用方完成。
 */
@Data
public class Plate extends GameItem{
    /**
     * 盘内食材。使用 List 以允许同种食材重复放入。
     */
    private List<Ingredient> contents = new ArrayList<>();

    /**
     * 非 {@link IngredientStatus#RAW} 的食材可以装盘。
     */
    private boolean canAccept(Ingredient ingredient) {
        return ingredient != null
                && ingredient.getProcessStatus() != null
                && ingredient.getProcessStatus() != IngredientStatus.RAW;
    }

    /**
     * 将已处理食材放入盘中。未处理或不合法的食材不改变盘子状态。
     *
     * @return 是否成功装入
     */
    public boolean addIngredient(Ingredient ingredient) {
        if (!canAccept(ingredient)) {
            return false;
        }
        if (contents == null) {
            contents = new ArrayList<>();
        }
        return contents.add(ingredient);
    }

    private boolean isEmpty() {
        return contents == null || contents.isEmpty();
    }

    /**
     * 空盘不能与送餐口交互；不完整或错误组合可以交互。
     */
    private boolean canSubmitToServingCounter() {
        return !isEmpty();
    }

    /**
     * 当前盘内是否构成合法菜肴。错误组合可以存在，但不算菜肴。
     */
    public boolean isValidDish() {
        return getDishType() != null;
    }

    /**
     * 按既定菜谱识别菜肴。无法识别时返回 {@code null}。
     */
    private DishType getDishType() {
        if (isEmpty()) {
            return null;
        }
        int fishCut = count(IngredientType.FISH, IngredientStatus.CUT);
        int riceCooked = count(IngredientType.RICE, IngredientStatus.COOKED);
        int kelpCut = count(IngredientType.KELP, IngredientStatus.CUT);

        if (contents.size() == 1 && fishCut == 1) {
            return DishType.SASHIMI;
        }
        if (contents.size() == 2 && riceCooked == 1 && kelpCut == 1) {
            return DishType.ROLL;
        }
        return null;
    }

    /**
     * 盘内菜肴名称，供与订单 {@link Recipe#getDishName()} 匹配。
     */
    private String getDishName() {
        DishType dishType = getDishType();
        if (dishType == null) {
            return null;
        }
        return switch (dishType) {
            case SASHIMI -> "生鱼片";
            case ROLL -> "饭团";
        };
    }

    /**
     * 按盘内菜肴名称匹配。同时接受中文名与 {@link DishType} 枚举名。
     */
    public boolean matchesDishName(String dishName) {
        DishType dishType = getDishType();
        if (dishType == null || dishName == null || dishName.isBlank()) {
            return false;
        }
        return dishName.equals(getDishName()) || dishName.equals(dishType.name());
    }

    public boolean matches(Recipe recipe) {
        return recipe != null && matchesDishName(recipe.getDishName());
    }

    /**
     * 与送餐口交互：空盘无响应；否则清空盘子。
     * 菜肴名称与订单相符时，将 {@link Recipe#getBaseScore()} 累加到 {@link CalculateScore#score}。
     *
     * @return 是否发生了交互（空盘为 {@code false}）
     */
    public boolean submitToServingCounter(Recipe requiredDish, CalculateScore calculateScore) {
        if (!canSubmitToServingCounter()) {
            return false;
        }
        if (matches(requiredDish) && calculateScore != null) {
            calculateScore.setScore(calculateScore.getScore() + requiredDish.getBaseScore());
        }
        clear();
        return true;
    }

    /**
     * 送餐完成后清空盘子，盘子仍由玩家持有。
     */
    private void clear() {
        if (contents == null) {
            contents = new ArrayList<>();
        } else {
            contents.clear();
        }
    }

    /**
     * 预留给尚未定义的桌子：桌上没有盘子时才可放置。
     * 盘子不可堆叠，一张桌子最多一张盘子。
     */
    public boolean canPlaceOn(TableSurface table) {
        return table != null && !table.hasPlate();
    }

    /**
     * 将本盘子放到桌子上。桌上已有盘子时失败，不改变任何一方状态。
     */
    public boolean placeOn(TableSurface table) {
        if (!canPlaceOn(table)) {
            return false;
        }
        return table.placePlate(this);
    }

    /**
     * 从桌子上取走盘子。桌上无盘时返回 {@code null}。
     * 调用方负责确认玩家空手。
     */
    public static Plate takeFrom(TableSurface table) {
        if (table == null || !table.hasPlate()) {
            return null;
        }
        return table.takePlate();
    }

    private int count(IngredientType type, IngredientStatus status) {
        int total = 0;
        for (Ingredient ingredient : contents) {
            if (ingredient != null
                    && ingredient.getIngredientType() == type
                    && ingredient.getProcessStatus() == status) {
                total++;
            }
        }
        return total;
    }

    /**
     * 桌子尚未实现时的占用约定。未来的桌子应实现该接口：
     * 盘子不可堆叠，{@link #placePlate(Plate)} 在已占用时应返回 {@code false}。
     */
    public interface TableSurface {
        boolean hasPlate();

        Plate peekPlate();

        /**
         * @return 放置成功为 {@code true}；已有盘子（不可堆叠）为 {@code false}
         */
        boolean placePlate(Plate plate);

        /**
         * 取走桌上盘子；无盘时返回 {@code null}。
         */
        Plate takePlate();
    }
}
