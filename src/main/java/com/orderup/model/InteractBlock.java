package com.orderup.model;

import lombok.Data;

@Data
public class InteractBlock {
    // 交互框坐标与尺寸
    private double x;
    private double y;
    public static final double WIDTH = 10;
    public static final double HEIGHT = 10;

    // 判断本交互框 和目标矩形是否相交
    public boolean intersects(double targetX, double targetY, double targetW, double targetH){
        return  this.x < targetX + targetW
                && this.x + WIDTH > targetX
                && this.y < targetY + targetH
                && this.y + HEIGHT > targetY;
    }
}
