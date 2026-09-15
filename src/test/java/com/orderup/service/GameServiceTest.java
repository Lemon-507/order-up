package com.orderup.service;

import com.orderup.config.GameConfig;
import com.orderup.model.GameMap;
import com.orderup.model.IngredientSource;
import com.orderup.model.IngredientType;
import com.orderup.model.Plate;
import com.orderup.model.ProcessingStation;
import com.orderup.model.Table;
import com.orderup.model.TileType;
import com.orderup.model.TileVisual;
import com.orderup.service.Impl.GameServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameServiceTest {
    @Test
    void createsTheFirstMapWithTablesSourceAndItem() {
        GameMap map = new GameServiceImpl().createMap();

        assertInstanceOf(Table.class, map.getTile(0, 0));
        IngredientSource fishSource = assertInstanceOf(
                IngredientSource.class,
                map.getTile(4, 0)
        );
        assertEquals(IngredientType.FISH, fishSource.getIngredientType());
        IngredientSource riceSource = assertInstanceOf(
                IngredientSource.class,
                map.getTile(4, 12)
        );
        assertEquals(IngredientType.RICE, riceSource.getIngredientType());
        assertInstanceOf(Table.class, map.getTile(3, 4));
        ProcessingStation choppingBoard = assertInstanceOf(
                ProcessingStation.class,
                map.getTile(8, 1)
        );
        ProcessingStation riceCooker = assertInstanceOf(
                ProcessingStation.class,
                map.getTile(0, 4)
        );
        assertEquals(TileType.CHOPPING_BOARD, choppingBoard.getType());
        assertEquals(TileType.RICE_COOKER, riceCooker.getType());
        assertEquals(TileVisual.FLOOR_LIGHT, map.getTile(1, 1).getVisual());
        assertEquals(TileVisual.FLOOR_DARK, map.getTile(1, 2).getVisual());
        assertEquals(TileVisual.ROUND_PLANTER, map.getTile(0, 2).getVisual());
        assertEquals(TileVisual.SERVING_COUNTER, map.getTile(2, 12).getVisual());
        assertEquals(TileType.ORDER_COUNTER, map.getTile(2, 12).getType());
        assertEquals(TileType.ORDER_COUNTER, map.getTile(3, 12).getType());
        assertInstanceOf(Table.class, map.getTile(8, 4));
        assertInstanceOf(Table.class, map.getTile(8, 5));
        assertEquals(TileVisual.CASHIER_COUNTER_LEFT, map.getTile(8, 4).getVisual());
        assertEquals(TileVisual.CASHIER_COUNTER_RIGHT, map.getTile(8, 5).getVisual());
        assertEquals(
                TileType.ORDER_COUNTER,
                map.getTile(GameConfig.ORDER_COUNTER_ROW, GameConfig.ORDER_COUNTER_COLUMN).getType()
        );
        assertEquals(
                TileType.PLATE_RETURN,
                map.getTile(GameConfig.PLATE_RETURN_ROW, GameConfig.PLATE_RETURN_START_COLUMN).getType()
        );
        assertEquals(
                TileType.TRASH_CAN,
                map.getTile(GameConfig.TRASH_CAN_ROW, GameConfig.TRASH_CAN_COLUMN).getType()
        );
        List<Plate> plates = map.getItems().stream()
                .filter(Plate.class::isInstance)
                .map(Plate.class::cast)
                .toList();
        assertEquals(GameConfig.PLATE_COUNT, plates.size());
        for (int slot = 0; slot < GameConfig.PLATE_COUNT; slot++) {
            Plate plate = plates.get(slot);
            assertEquals(
                    GameConfig.PLATE_RETURN_START_COLUMN * GameConfig.TILE_SIZE
                            + (GameConfig.TILE_SIZE - plate.getWidth()) / 2.0
                            + (slot - 1) * 5,
                    plate.getX()
            );
            assertEquals(
                    GameConfig.PLATE_RETURN_ROW * GameConfig.TILE_SIZE
                            + (GameConfig.TILE_SIZE - plate.getHeight()) / 2.0
                            + slot * 2,
                    plate.getY()
            );
        }
    }

    @Test
    void createsTheSecondMapWithAKelpSource() {
        GameMap map = new GameServiceImpl().createMap(2);

        IngredientSource kelpSource = assertInstanceOf(
                IngredientSource.class,
                map.getTile(7, 12)
        );
        assertEquals(IngredientType.KELP, kelpSource.getIngredientType());
    }

    @Test
    void rejectsAnUnknownLevel() {
        GameService service = new GameServiceImpl();

        assertThrows(IllegalArgumentException.class, () -> service.createMap(3));
    }
}
