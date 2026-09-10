package com.orderup.view;

import com.orderup.model.InteractBlock;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * 交互框显示层，负责将交互区域绘制成绿色方块。
 */
public class InteractBlockView {
    public void render(GraphicsContext graphics, InteractBlock interactBlock) {
        graphics.setFill(Color.GREEN);
        graphics.fillRect(
                interactBlock.getX(),
                interactBlock.getY(),
                InteractBlock.WIDTH,
                InteractBlock.HEIGHT
        );
    }
}
