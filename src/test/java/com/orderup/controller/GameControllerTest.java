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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
        Order submittedOrder = controller.getCurrentOrder();
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
        assertTrue(controller.getGameMap().getItems().stream().noneMatch(Plate.class::isInstance));
        assertTrue(controller.getScore() > 0);
        assertNotEquals(submittedOrder.getId(), controller.getCurrentOrder().getId());

        controller.update(GameConfig.PLATE_RESPAWN_SECONDS - 0.1);
        assertTrue(controller.getGameMap().getItems().stream().noneMatch(Plate.class::isInstance));

        controller.update(0.2);
        Plate returnedPlate = controller.getGameMap().getItems().stream()
                .filter(Plate.class::isInstance)
                .map(Plate.class::cast)
                .findFirst()
                .orElseThrow();
        assertTrue(returnedPlate.isEmpty());
        assertEquals(
                GameConfig.PLATE_RETURN_COLUMN * GameConfig.TILE_SIZE
                        + (GameConfig.TILE_SIZE - returnedPlate.getWidth()) / 2.0,
                returnedPlate.getX()
        );
        assertEquals(
                GameConfig.PLATE_RETURN_ROW * GameConfig.TILE_SIZE
                        + (GameConfig.TILE_SIZE - returnedPlate.getHeight()) / 2.0,
                returnedPlate.getY()
        );
    }
}
