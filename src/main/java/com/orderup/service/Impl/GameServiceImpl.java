package com.orderup.service.Impl;

import com.orderup.model.GameMap;
import com.orderup.model.MapName;
import com.orderup.model.TileType;
import com.orderup.service.GameService;

public class GameServiceImpl implements GameService {

    @Override
    public void LoadMap(GameMap map) {
        if (map.getMapname() != MapName.map1) {
            return;
        }

        for (int i = 0; i < 9; i++) {
            map.setTiles(i, 0, TileType.WALL);
            map.setTiles(i, 12, TileType.WALL);
        }
        for (int i = 0; i < 13; i++) {
            map.setTiles(0, i, TileType.WALL);
            map.setTiles(8, i, TileType.WALL);
        }
        for (int i = 3; i < 5; i++) {
            map.setTiles(i, 5, TileType.WALL);
        }
    }
}
