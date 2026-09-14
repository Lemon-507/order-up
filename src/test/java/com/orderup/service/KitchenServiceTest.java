package com.orderup.service;

import com.orderup.config.GameConfig;
import com.orderup.model.Direction;
import com.orderup.model.GameMap;
import com.orderup.model.Ingredient;
import com.orderup.model.IngredientStatus;
import com.orderup.model.IngredientType;
import com.orderup.model.InteractionArea;
import com.orderup.model.Plate;
import com.orderup.model.Player;
import com.orderup.model.Table;
import com.orderup.service.Impl.GameServiceImpl;
import com.orderup.service.Impl.KitchenServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KitchenServiceTest {
    @Test
    void takesAndDropsAnIngredient() {
        GameMap map = new GameServiceImpl().createMap();
        Player player = new Player(90, 80);
        player.press(Direction.UP);
        player.clearInput();
        InteractionArea area = new InteractionArea();
        area.updateFrom(player);
        com.orderup.service.KitchenService kitchen = new KitchenServiceImpl();

        assertTrue(kitchen.interact(player, area, map).success());
        assertTrue(player.hasHeldItem());
        Ingredient ingredient = assertInstanceOf(Ingredient.class, player.getHeldItem());
        assertEquals(IngredientType.FISH, ingredient.getType());

        assertTrue(kitchen.interact(player, area, map).success());
        assertFalse(player.hasHeldItem());
    }

    @Test
    void takesTheIngredientConfiguredForTheSource() {
        GameMap map = new GameServiceImpl().createMap(2);
        Player player = new Player(170, 80);
        player.press(Direction.UP);
        player.clearInput();
        InteractionArea area = new InteractionArea();
        area.updateFrom(player);
        KitchenService kitchen = new KitchenServiceImpl();

        assertTrue(kitchen.interact(player, area, map).success());
        Ingredient ingredient = assertInstanceOf(Ingredient.class, player.getHeldItem());
        assertEquals(IngredientType.RICE, ingredient.getType());
    }

    @Test
    void placesAndTakesAnItemFromATable() {
        GameMap map = new GameServiceImpl().createMap();
        Player player = new Player(360, 250);
        player.press(Direction.RIGHT);
        player.clearInput();
        InteractionArea area = new InteractionArea();
        area.updateFrom(player);
        com.orderup.service.KitchenService kitchen = new KitchenServiceImpl();
        Plate item = map.addItem(new Plate(300, 250));
        player.pickUp(item);

        assertTrue(kitchen.interact(player, area, map).success());
        assertFalse(player.hasHeldItem());
        assertFalse(((Table) map.getTile(3, 5)).isEmpty());

        assertTrue(kitchen.interact(player, area, map).success());
        assertTrue(player.hasHeldItem());
    }

    @Test
    void cutsRawFishAtTheChoppingBoard() {
        GameMap map = new GameServiceImpl().createMap();
        Player player = playerFacingLeftAt(250);
        Ingredient fish = map.addItem(new Ingredient(IngredientType.FISH, 0, 0));
        player.pickUp(fish);
        InteractionArea area = new InteractionArea();
        area.updateFrom(player);
        KitchenService kitchen = new KitchenServiceImpl();

        assertTrue(kitchen.interact(player, area, map).success());
        assertFalse(player.hasHeldItem());
        assertEquals(IngredientStatus.RAW, fish.getStatus());

        kitchen.updateProcessing(
                area,
                map,
                true,
                GameConfig.CHOPPING_SECONDS / 2
        );
        kitchen.updateProcessing(area, map, false, 0);
        kitchen.updateProcessing(
                area,
                map,
                true,
                GameConfig.CHOPPING_SECONDS / 2
        );
        assertEquals(IngredientStatus.RAW, fish.getStatus());

        kitchen.updateProcessing(
                area,
                map,
                true,
                GameConfig.CHOPPING_SECONDS / 2
        );
        assertEquals(IngredientStatus.CUT, fish.getStatus());

        assertTrue(kitchen.interact(player, area, map).success());
        assertTrue(player.hasHeldItem());
    }

    @Test
    void cooksRawRiceAtTheRiceCooker() {
        GameMap map = new GameServiceImpl().createMap();
        Player player = playerFacingLeftAt(330);
        Ingredient rice = map.addItem(new Ingredient(IngredientType.RICE, 0, 0));
        player.pickUp(rice);
        InteractionArea area = new InteractionArea();
        area.updateFrom(player);
        KitchenService kitchen = new KitchenServiceImpl();

        assertTrue(kitchen.interact(player, area, map).success());
        assertFalse(player.hasHeldItem());
        assertEquals(IngredientStatus.RAW, rice.getStatus());

        kitchen.updateProcessing(
                area,
                map,
                false,
                GameConfig.RICE_COOKING_SECONDS - 0.1
        );
        assertEquals(IngredientStatus.RAW, rice.getStatus());

        kitchen.updateProcessing(area, map, false, 0.2);
        assertEquals(IngredientStatus.COOKED, rice.getStatus());

        assertTrue(kitchen.interact(player, area, map).success());
        assertTrue(player.hasHeldItem());
    }

    private Player playerFacingLeftAt(double y) {
        Player player = new Player(560, y);
        player.press(Direction.LEFT);
        player.clearInput();
        return player;
    }
}
