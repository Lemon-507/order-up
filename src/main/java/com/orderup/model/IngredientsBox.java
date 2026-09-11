package com.orderup.model;

import java.util.List;

public class IngredientsBox extends Tile{
    TileType tileType = TileType.INGREDIENT_SOURCE;


    public IngredientsBox(int x, int y) {
        super(x, y);
    }

    //产生食材
    @Override
    public  void  Interact(InteractBlock ib, GameMap gameMap, List<GameItem> items,Player player) {
        if (!player.isHolding) {
            GameItem newItem=gameMap.AddItem(new GameItem((int) ib.getX(), (int) ib.getY()));
            player.isHolding = true;
        }
    }







}
