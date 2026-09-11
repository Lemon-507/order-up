package com.orderup.service;

import com.orderup.model.Direction;
import com.orderup.model.GameMap;
import com.orderup.model.Ingredient;
import com.orderup.model.InteractionArea;
import com.orderup.model.Plate;
import com.orderup.model.Player;
import com.orderup.model.Table;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KitchenServiceTest {
    @Test
    void takesAndDropsAnIngredient() {
        GameMap map = new GameService().createMap();
        Player player = new Player(90, 80);
        player.press(Direction.UP);
        player.clearInput();
        InteractionArea area = new InteractionArea();
        area.updateFrom(player);
        KitchenService kitchen = new KitchenService();

        assertTrue(kitchen.interact(player, area, map).success());
        assertTrue(player.hasHeldItem());
        assertInstanceOf(Ingredient.class, player.getHeldItem());

        assertTrue(kitchen.interact(player, area, map).success());
        assertFalse(player.hasHeldItem());
    }

    @Test
    void placesAndTakesAnItemFromATable() {
        GameMap map = new GameService().createMap();
        Player player = new Player(360, 250);
        player.press(Direction.RIGHT);
        player.clearInput();
        InteractionArea area = new InteractionArea();
        area.updateFrom(player);
        KitchenService kitchen = new KitchenService();
        Plate item = map.addItem(new Plate(300, 250));
        player.pickUp(item);

        assertTrue(kitchen.interact(player, area, map).success());
        assertFalse(player.hasHeldItem());
        assertFalse(((Table) map.getTile(3, 5)).isEmpty());

        assertTrue(kitchen.interact(player, area, map).success());
        assertTrue(player.hasHeldItem());
    }
}
