package com.orderup.view;

import com.orderup.model.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * 玩家在画布上的显示方式。
 */
public class PlayerView {
    public void render(GraphicsContext graphics, Player player) {
        graphics.setFill(Color.RED);
        graphics.fillRect(player.getX(), player.getY(), Player.WIDTH, Player.HEIGHT);
    }
}
