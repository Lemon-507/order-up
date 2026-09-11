package com.orderup.model;

import lombok.Data;

/**
 * 食材、盘子及成品等可拿取物品的抽象父类。
 */
@Data
public class GameItem {
    private String itemId;
    private String itemType;
    private int x;
    private int y;
    private int width = 20;
    private int height = 20;
    public boolean isPicked=false;

    public GameItem() {
    }

    public GameItem(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }

}
