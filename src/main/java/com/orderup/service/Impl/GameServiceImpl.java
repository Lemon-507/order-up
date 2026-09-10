package com.orderup.service.Impl;

import com.orderup.model.GameMap;
import com.orderup.model.Mapname;
import com.orderup.model.StationType;
import com.orderup.service.GameService;

public class GameServiceImpl implements GameService {

    @Override
    public void LoadMap(GameMap map) {
        if (map.getMapname() != Mapname.map1) {
            return;
        }

        for (int i = 0; i < 9; i++) {
            map.setTiles(i, 0, StationType.WALL);
            map.setTiles(i, 12, StationType.WALL);
        }
        for (int i = 0; i < 13; i++) {
            map.setTiles(0, i, StationType.WALL);
            map.setTiles(8, i, StationType.WALL);
        }
        for (int i = 3; i < 5; i++) {
            map.setTiles(i, 5, StationType.WALL);
        }
    }
}
