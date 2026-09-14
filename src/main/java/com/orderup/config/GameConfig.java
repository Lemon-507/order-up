package com.orderup.config;

import com.orderup.model.DishType;
import com.orderup.model.Ingredient;
import com.orderup.model.IngredientStatus;
import com.orderup.model.IngredientType;
import com.orderup.model.Recipe;
import com.orderup.model.TileType;

import java.util.List;
import java.util.Set;

/**
 * 游戏运行参数的唯一来源。
 */
public final class GameConfig {
    public static final double WINDOW_WIDTH = 1280;
    public static final double WINDOW_HEIGHT = 720;
    public static final int MAP_ROWS = 9;
    public static final int MAP_COLUMNS = 13;
    public static final int TILE_SIZE = 80;
    public static final int DEFAULT_LEVEL = 1;
    public static final int GAME_SECONDS = 600;
    public static final double FIXED_STEP_SECONDS = 1.0 / 60.0;
    public static final double MAX_ACCUMULATED_SECONDS = 0.25;
    public static final double PLAYER_START_X = 200;
    public static final double PLAYER_START_Y = 200;
    public static final double PLAYER_SPEED = 220;
    public static final double DASH_DISTANCE = 160;
    public static final double DASH_DURATION_SECONDS = 0.15;
    public static final double DASH_COOLDOWN_SECONDS = 0.8;
    public static final double DASH_SPEED = DASH_DISTANCE / DASH_DURATION_SECONDS;
    public static final double CHOPPING_SECONDS = 2.0;
    public static final double RICE_COOKING_SECONDS = 5.0;
    public static final double PLATE_RESPAWN_SECONDS = 3.0;
    public static final int PLATE_COUNT = 3;
    public static final int INITIAL_ACTIVE_ORDER_COUNT = 1;
    public static final double ORDER_SPAWN_INTERVAL_SECONDS = 10.0;
    public static final int ORDER_COUNTER_ROW = 8;
    public static final int ORDER_COUNTER_COLUMN = 5;
    public static final int PLATE_RETURN_ROW = 8;
    public static final int PLATE_RETURN_START_COLUMN = 7;
    public static final int TRASH_CAN_ROW = 8;
    public static final int TRASH_CAN_COLUMN = 11;

    private static final Recipe SASHIMI_RECIPE = new Recipe(
            DishType.SASHIMI,
            100,
            30,
            Set.of(new Ingredient(IngredientType.FISH, IngredientStatus.CUT))
    );
    private static final Recipe ROLL_RECIPE = new Recipe(
            DishType.ROLL,
            150,
            45,
            Set.of(
                    new Ingredient(IngredientType.KELP, IngredientStatus.RAW),
                    new Ingredient(IngredientType.RICE, IngredientStatus.COOKED)
            )
    );
    public static final List<Recipe> RECIPES = List.of(SASHIMI_RECIPE, ROLL_RECIPE);

    private static final List<IngredientSourceConfig> LEVEL_1_SOURCES = List.of(
            new IngredientSourceConfig(0, 1, IngredientType.FISH),
            new IngredientSourceConfig(0, 2, IngredientType.RICE)
    );

    private static final List<IngredientSourceConfig> LEVEL_2_SOURCES = List.of(
            new IngredientSourceConfig(0, 1, IngredientType.FISH),
            new IngredientSourceConfig(0, 2, IngredientType.RICE),
            new IngredientSourceConfig(0, 3, IngredientType.KELP)
    );

    private static final List<FacilityConfig> KITCHEN_FACILITIES = List.of(
            new FacilityConfig(3, 6, TileType.CHOPPING_BOARD),
            new FacilityConfig(4, 6, TileType.RICE_COOKER),
            new FacilityConfig(ORDER_COUNTER_ROW, ORDER_COUNTER_COLUMN, TileType.ORDER_COUNTER),
            new FacilityConfig(PLATE_RETURN_ROW, PLATE_RETURN_START_COLUMN, TileType.PLATE_RETURN),
            new FacilityConfig(PLATE_RETURN_ROW, PLATE_RETURN_START_COLUMN + 1, TileType.PLATE_RETURN),
            new FacilityConfig(PLATE_RETURN_ROW, PLATE_RETURN_START_COLUMN + 2, TileType.PLATE_RETURN),
            new FacilityConfig(TRASH_CAN_ROW, TRASH_CAN_COLUMN, TileType.TRASH_CAN)
    );

    private GameConfig() {
    }

    /**
     * 返回指定关卡中所有食材源的位置和食材类型。
     *
     * @param level 关卡编号，当前只支持 1 和 2
     * @return 对应关卡的食材源配置
     */
    public static List<IngredientSourceConfig> getIngredientSources(int level) {
        return switch (level) {
            case 1 -> LEVEL_1_SOURCES;
            case 2 -> LEVEL_2_SOURCES;
            default -> throw new IllegalArgumentException("不存在的关卡：" + level);
        };
    }

    /**
     * 返回指定关卡允许生成的订单菜谱。
     */
    public static List<Recipe> getRecipes(int level) {
        return switch (level) {
            case 1 -> List.of(SASHIMI_RECIPE);
            case 2 -> RECIPES;
            default -> throw new IllegalArgumentException("不存在的关卡：" + level);
        };
    }

    /**
     * 返回指定关卡中的加工设施配置。
     *
     * @param level 关卡编号，当前只支持 1 和 2
     * @return 切菜板和电饭煲的位置及类型
     */
    public static List<FacilityConfig> getFacilities(int level) {
        return switch (level) {
            case 1, 2 -> KITCHEN_FACILITIES;
            default -> throw new IllegalArgumentException("不存在的关卡：" + level);
        };
    }

    /**
     * 一个食材源的静态地图配置。
     */
    public record IngredientSourceConfig(int row, int column, IngredientType ingredientType) {
    }

    /**
     * 一个加工设施的静态地图配置。
     */
    public record FacilityConfig(int row, int column, TileType tileType) {
    }
}
