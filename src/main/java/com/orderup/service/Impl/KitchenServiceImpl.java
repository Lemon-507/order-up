package com.orderup.service.Impl;

import com.orderup.config.GameConfig;
import com.orderup.model.GameItem;
import com.orderup.model.GameMap;
import com.orderup.model.Ingredient;
import com.orderup.model.IngredientSource;
import com.orderup.model.IngredientStatus;
import com.orderup.model.IngredientType;
import com.orderup.model.InteractionArea;
import com.orderup.model.InteractionResult;
import com.orderup.model.Plate;
import com.orderup.model.Player;
import com.orderup.model.ProcessingStation;
import com.orderup.model.Table;
import com.orderup.model.Tile;
import com.orderup.model.TileType;

/**
 * 处理拾取、放下、食材来源、加工设施和桌面装盘。
 */
public class KitchenServiceImpl implements com.orderup.service.KitchenService {
    /** {@inheritDoc} */
    @Override
    public InteractionResult interact(Player player, InteractionArea area, GameMap map) {
        Tile tile = findTile(area, map);

        if (tile instanceof ProcessingStation station) {
            return interactWithStation(player, station, map);
        }
        if (tile != null && tile.getType() == TileType.TRASH_CAN) {
            return interactWithTrashCan(player);
        }
        if (player.getHeldItem() instanceof Ingredient ingredient && !(tile instanceof Table)) {
            Plate plate = findPlate(area, map);
            if (plate != null) {
                InteractionResult result = addIngredientToPlate(plate, ingredient, map);
                if (result.success()) {
                    player.releaseHeldItem();
                }
                return result;
            }
        }
        if (player.getHeldItem() instanceof Plate plate && !(tile instanceof Table)) {
            if (tile instanceof IngredientSource source) {
                Ingredient ingredient = new Ingredient(
                        source.getIngredientType(),
                        area.getX(),
                        area.getY()
                );
                return addIngredientToPlate(plate, ingredient, map);
            }
            Ingredient ingredient = findIngredient(area, map);
            if (ingredient != null) {
                return addIngredientToPlate(plate, ingredient, map);
            }
        }
        if (player.hasHeldItem()) {
            return placeOrDrop(player, area, map, tile);
        }
        if (tile instanceof Table table && !table.isEmpty()) {
            player.pickUp(table.take());
            return InteractionResult.ok("拿起物品");
        }
        for (GameItem item : map.getItems()) {
            if (item != player.getHeldItem()
                    && area.intersects(item.getX(), item.getY(), item.getWidth(), item.getHeight())) {
                player.pickUp(item);
                return InteractionResult.ok("拿起物品");
            }
        }
        if (tile instanceof IngredientSource source) {
            return takeIngredientFromSource(player, area, map, source);
        }
        return InteractionResult.failed("附近没有可交互物品");
    }

    private InteractionResult interactWithTrashCan(Player player) {
        if (!(player.getHeldItem() instanceof Plate plate)) {
            return InteractionResult.failed("请手持有食材的盘子使用垃圾桶");
        }
        if (plate.isEmpty()) {
            return InteractionResult.failed("盘子已经是空的");
        }
        plate.clear();
        return InteractionResult.ok("已倒掉盘中食材");
    }

    /**
     * 向加工设施放入原料，或取回已经加工完成的食材。
     */
    private InteractionResult interactWithStation(
            Player player,
            ProcessingStation station,
            GameMap map
    ) {
        if (player.getHeldItem() instanceof Plate plate) {
            Ingredient ingredient = station.getIngredient();
            if (ingredient == null) {
                return InteractionResult.failed("设施中没有食材");
            }
            InteractionResult result = addIngredientToPlate(plate, ingredient, map);
            if (result.success()) {
                station.take();
            }
            return result;
        }
        if (player.hasHeldItem()) {
            return placeIngredientOnStation(player, station);
        }

        Ingredient ingredient = station.getIngredient();
        if (ingredient == null) {
            return InteractionResult.failed("设施中没有食材");
        }
        if (ingredient.getStatus() == IngredientStatus.RAW) {
            return station.getType() == TileType.CHOPPING_BOARD
                    ? InteractionResult.ok("请持续按住 E 切鱼")
                    : InteractionResult.failed("米饭还在烹煮");
        }

        player.pickUp(station.take());
        return InteractionResult.ok("取出加工完成的食材");
    }

    private InteractionResult placeIngredientOnStation(
            Player player,
            ProcessingStation station
    ) {
        if (!station.isEmpty()) {
            return InteractionResult.failed("该设施正在使用");
        }
        if (!(player.getHeldItem() instanceof Ingredient ingredient)) {
            return InteractionResult.failed("该设施只能加工食材");
        }

        boolean acceptsIngredient = station.getType() == TileType.CHOPPING_BOARD
                ? ingredient.getType() == IngredientType.FISH
                : ingredient.getType() == IngredientType.RICE;
        if (!acceptsIngredient || ingredient.getStatus() != IngredientStatus.RAW) {
            return InteractionResult.failed(station.getType() == TileType.CHOPPING_BOARD
                    ? "切菜板只能放置生鱼"
                    : "电饭煲只能放置生米");
        }

        station.place(ingredient);
        player.releaseHeldItem();

        return InteractionResult.ok(station.getType() == TileType.CHOPPING_BOARD
                ? "生鱼已放上切菜板，请持续按住 E"
                : "生米已放入电饭煲");
    }

    /** {@inheritDoc} */
    @Override
    public void updateProcessing(
            InteractionArea area,
            GameMap map,
            boolean interacting,
            double deltaSeconds
    ) {
        if (deltaSeconds < 0) {
            throw new IllegalArgumentException("Delta seconds cannot be negative.");
        }

        Tile activeTile = interacting ? findTile(area, map) : null;
        for (Tile[] row : map.getTiles()) {
            for (Tile tile : row) {
                if (tile instanceof ProcessingStation station) {
                    updateStation(station, activeTile == station, deltaSeconds);
                }
            }
        }
    }

    private void updateStation(
            ProcessingStation station,
            boolean activelyInteracting,
            double deltaSeconds
    ) {
        Ingredient ingredient = station.getIngredient();
        if (ingredient == null || ingredient.getStatus() != IngredientStatus.RAW) {
            return;
        }

        if (station.getType() == TileType.RICE_COOKER) {
            station.advance(deltaSeconds);
            if (station.getProgressSeconds() >= GameConfig.RICE_COOKING_SECONDS) {
                ingredient.setStatus(IngredientStatus.COOKED);
                ingredient.setSight(true);
            }
            return;
        }

        if (!activelyInteracting) {
            return;
        }
        station.advance(deltaSeconds);
        if (station.getProgressSeconds() >= GameConfig.CHOPPING_SECONDS) {
            ingredient.setStatus(IngredientStatus.CUT);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void updateHeldItem(Player player, InteractionArea area) {
        GameItem heldItem = player.getHeldItem();
        if (heldItem != null) {
            heldItem.setX(area.getX());
            heldItem.setY(area.getY());
        }
    }

    /** {@inheritDoc} */
    @Override
    public InteractionResult placeOrDrop(
            Player player,
            InteractionArea area,
            GameMap map,
            Tile tile
    ) {
        if (tile instanceof Table table) {
            return interactWithTable(player, table, map);
        }

        GameItem droppedItem = player.releaseHeldItem();
        droppedItem.setX(area.getX());
        droppedItem.setY(area.getY());
        return InteractionResult.ok("放下物品");
    }

    /** {@inheritDoc} */
    @Override
    public InteractionResult interactWithTable(Player player, Table table, GameMap map) {
        GameItem heldItem = player.getHeldItem();
        GameItem tableItem = table.getItem();

        if (heldItem instanceof Ingredient ingredient && tableItem instanceof Plate plate) {
            InteractionResult result = addIngredientToPlate(plate, ingredient, map);
            if (!result.success()) {
                return result;
            }
            player.releaseHeldItem();
            return result;
        }

        if (heldItem instanceof Plate plate && tableItem instanceof Ingredient ingredient) {
            InteractionResult result = addIngredientToPlate(plate, ingredient, map);
            if (!result.success()) {
                return result;
            }
            table.take();
            return result;
        }

        if (table.place(heldItem)) {
            player.releaseHeldItem();
            return InteractionResult.ok("物品已放到桌上");
        }
        return InteractionResult.failed("桌面已被占用");
    }

    /** {@inheritDoc} */
    @Override
    public InteractionResult takeIngredientFromSource(
            Player player,
            InteractionArea area,
            GameMap map,
            IngredientSource source
    ) {
        Ingredient ingredient = map.addItem(
                new Ingredient(source.getIngredientType(), area.getX(), area.getY())
        );
        player.pickUp(ingredient);
        return InteractionResult.ok("取得食材");
    }

    private InteractionResult addIngredientToPlate(
            Plate plate,
            Ingredient ingredient,
            GameMap map
    ) {
        if (!plate.addIngredient(ingredient)) {
            return InteractionResult.failed("该食材尚不能装盘");
        }
        map.removeItem(ingredient);
        return InteractionResult.ok("食材已装盘");
    }

    private Ingredient findIngredient(InteractionArea area, GameMap map) {
        for (GameItem item : map.getItems()) {
            if (item instanceof Ingredient ingredient
                    && area.intersects(
                    ingredient.getX(),
                    ingredient.getY(),
                    ingredient.getWidth(),
                    ingredient.getHeight()
            )) {
                return ingredient;
            }
        }
        return null;
    }

    private Plate findPlate(InteractionArea area, GameMap map) {
        for (GameItem item : map.getItems()) {
            if (item instanceof Plate plate
                    && area.intersects(
                    plate.getX(),
                    plate.getY(),
                    plate.getWidth(),
                    plate.getHeight()
            )) {
                return plate;
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Tile findTile(InteractionArea area, GameMap map) {
        double centerX = area.getX() + InteractionArea.WIDTH / 2;
        double centerY = area.getY() + InteractionArea.HEIGHT / 2;

        int column = (int) (centerX / GameConfig.TILE_SIZE);
        int row = (int) (centerY / GameConfig.TILE_SIZE);

        if (row < 0 || row >= GameConfig.MAP_ROWS
                || column < 0 || column >= GameConfig.MAP_COLUMNS) {
            return null;
        }

        return map.getTile(row, column);
    }
}
