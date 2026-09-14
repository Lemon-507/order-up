package com.orderup.controller;

import com.orderup.config.GameConfig;
import com.orderup.model.Direction;
import com.orderup.model.GameState;
import com.orderup.model.Ingredient;
import com.orderup.model.IngredientStatus;
import com.orderup.model.IngredientType;
import com.orderup.model.InteractionResult;
import com.orderup.model.Order;
import com.orderup.model.Plate;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameControllerTest {
    @Test
    void pauseStopsUpdatesUntilGameResumes() {
        GameController controller = new GameController(1, () -> { });
        controller.startGame();
        int initialSeconds = controller.getRemainingSeconds();

        controller.pauseGame();
        controller.update(2);

        assertEquals(GameState.PAUSED, controller.getState());
        assertEquals(initialSeconds, controller.getRemainingSeconds());

        controller.resumeGame();
        controller.update(1);

        assertEquals(GameState.RUNNING, controller.getState());
        assertEquals(initialSeconds - 1, controller.getRemainingSeconds());
    }

    @Test
    void submittedPlateRespawnsAtThePlateReturnAfterDelay() {
        GameController controller = new GameController(1, () -> { });
        controller.startGame();
        assertEquals(GameConfig.ACTIVE_ORDER_COUNT, controller.getActiveOrders().size());
        Order submittedOrder = controller.getActiveOrders().get(0);
        Plate plate = controller.getGameMap().getItems().stream()
                .filter(Plate.class::isInstance)
                .map(Plate.class::cast)
                .findFirst()
                .orElseThrow();
        Ingredient fish = new Ingredient(IngredientType.FISH, 0, 0);
        fish.setStatus(IngredientStatus.CUT);
        plate.addIngredient(fish);

        controller.getPlayer().setPosition(410, 580);
        controller.getPlayer().press(Direction.DOWN);
        controller.getPlayer().clearInput();
        controller.update(0);
        controller.getPlayer().pickUp(plate);

        InteractionResult result = controller.interact();

        assertTrue(result.success());
        assertFalse(controller.getPlayer().hasHeldItem());
        assertEquals(
                GameConfig.PLATE_COUNT - 1,
                countPlates(controller)
        );
        assertTrue(controller.getScore() > 0);
        assertEquals(GameConfig.ACTIVE_ORDER_COUNT, controller.getActiveOrders().size());
        assertTrue(controller.getActiveOrders().stream()
                .noneMatch(order -> order.getId().equals(submittedOrder.getId())));

        controller.update(GameConfig.PLATE_RESPAWN_SECONDS - 0.1);
        assertEquals(GameConfig.PLATE_COUNT - 1, countPlates(controller));

        controller.update(0.2);
        Plate returnedPlate = controller.getGameMap().getItems().stream()
                .filter(Plate.class::isInstance)
                .map(Plate.class::cast)
                .filter(candidate -> candidate.getX()
                        == GameConfig.PLATE_RETURN_START_COLUMN * GameConfig.TILE_SIZE
                        + (GameConfig.TILE_SIZE - candidate.getWidth()) / 2.0)
                .findFirst()
                .orElseThrow();
        assertEquals(GameConfig.PLATE_COUNT, countPlates(controller));
        assertTrue(returnedPlate.isEmpty());
        assertEquals(
                GameConfig.PLATE_RETURN_START_COLUMN * GameConfig.TILE_SIZE
                        + (GameConfig.TILE_SIZE - returnedPlate.getWidth()) / 2.0,
                returnedPlate.getX()
        );
        assertEquals(
                GameConfig.PLATE_RETURN_ROW * GameConfig.TILE_SIZE
                        + (GameConfig.TILE_SIZE - returnedPlate.getHeight()) / 2.0,
                returnedPlate.getY()
        );
    }

    @Test
    void submittedPlatesRespawnOnIndependentTimers() {
        GameController controller = new GameController(1, () -> { });
        controller.startGame();
        controller.getPlayer().setPosition(410, 580);
        controller.getPlayer().press(Direction.DOWN);
        controller.getPlayer().clearInput();
        controller.update(0);
        List<Plate> plates = controller.getGameMap().getItems().stream()
                .filter(Plate.class::isInstance)
                .map(Plate.class::cast)
                .toList();

        submitSashimi(controller, plates.get(0));
        controller.update(1);
        submitSashimi(controller, plates.get(1));
        assertEquals(1, countPlates(controller));

        controller.update(GameConfig.PLATE_RESPAWN_SECONDS - 0.9);
        assertEquals(2, countPlates(controller));

        controller.update(1);
        assertEquals(GameConfig.PLATE_COUNT, countPlates(controller));
    }

    @Test
    void expiredOrdersCanMakeTheTotalScoreNegative() {
        GameController controller = new GameController(1, () -> { });
        controller.startGame();
        double orderLifetime = controller.getActiveOrders().get(0).getRemainingSeconds();

        controller.update(orderLifetime);

        assertEquals(-20 * GameConfig.ACTIVE_ORDER_COUNT, controller.getScore());
        assertEquals(GameConfig.ACTIVE_ORDER_COUNT, controller.getActiveOrders().size());
    }

    private void submitSashimi(GameController controller, Plate plate) {
        Ingredient fish = new Ingredient(IngredientType.FISH, 0, 0);
        fish.setStatus(IngredientStatus.CUT);
        plate.addIngredient(fish);
        controller.getPlayer().pickUp(plate);
        assertTrue(controller.interact().success());
    }

    private long countPlates(GameController controller) {
        return controller.getGameMap().getItems().stream()
                .filter(Plate.class::isInstance)
                .count();
    }
}
