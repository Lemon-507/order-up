package com.orderup.service;

import com.orderup.config.GameConfig;
import com.orderup.model.GameMap;
import com.orderup.model.IngredientSource;
import com.orderup.model.IngredientType;
import com.orderup.model.Plate;
import com.orderup.model.ProcessingStation;
import com.orderup.model.Table;
import com.orderup.model.TileType;
import com.orderup.service.Impl.GameServiceImpl;
import org.junit.jupiter.api.Test;

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
                map.getTile(0, 1)
        );
        assertEquals(IngredientType.FISH, fishSource.getIngredientType());
        IngredientSource riceSource = assertInstanceOf(
                IngredientSource.class,
                map.getTile(0, 2)
        );
        assertEquals(IngredientType.RICE, riceSource.getIngredientType());
        assertInstanceOf(Table.class, map.getTile(4, 5));
        ProcessingStation choppingBoard = assertInstanceOf(
                ProcessingStation.class,
                map.getTile(3, 6)
        );
        ProcessingStation riceCooker = assertInstanceOf(
                ProcessingStation.class,
                map.getTile(4, 6)
        );
        assertEquals(TileType.CHOPPING_BOARD, choppingBoard.getType());
        assertEquals(TileType.RICE_COOKER, riceCooker.getType());
        assertEquals(
                TileType.ORDER_COUNTER,
                map.getTile(GameConfig.ORDER_COUNTER_ROW, GameConfig.ORDER_COUNTER_COLUMN).getType()
        );
        assertEquals(
                TileType.PLATE_RETURN,
                map.getTile(GameConfig.PLATE_RETURN_ROW, GameConfig.PLATE_RETURN_COLUMN).getType()
        );
        Plate plate = assertInstanceOf(Plate.class, map.getItems().get(0));
        assertEquals(
                GameConfig.PLATE_RETURN_COLUMN * GameConfig.TILE_SIZE
                        + (GameConfig.TILE_SIZE - plate.getWidth()) / 2.0,
                plate.getX()
        );
    }

    @Test
    void createsTheSecondMapWithAKelpSource() {
        GameMap map = new GameServiceImpl().createMap(2);

        IngredientSource kelpSource = assertInstanceOf(
                IngredientSource.class,
                map.getTile(0, 3)
        );
        assertEquals(IngredientType.KELP, kelpSource.getIngredientType());
    }

    @Test
    void rejectsAnUnknownLevel() {
        GameService service = new GameServiceImpl();

        assertThrows(IllegalArgumentException.class, () -> service.createMap(3));
    }
}
