package com.orderup.view;

import com.orderup.model.InteractionArea;
import javafx.scene.canvas.GraphicsContext;

/**
 * 玩家面前的交互检测区域。正式游戏中保持透明，仅保留逻辑判定。
 */
public class InteractionAreaView {
    public void render(GraphicsContext graphics, InteractionArea area) {
        // 不绘制任何内容，避免调试用绿色方块遮挡地图和食物素材。
    }
}
