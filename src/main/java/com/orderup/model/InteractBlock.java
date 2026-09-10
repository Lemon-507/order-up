package com.orderup.model;

public class InteractBlock {
    // 交互框坐标与尺寸
    private double x;
    private double y;
    public static final double WIDTH = 20;
    public static final double HEIGHT = 20;

    public void moveInteractBlock(Player player) {
        switch (player.getDirection()) {
            case UP -> {
                // 玩家上方，水平居中
                x = player.getX() + (Player.WIDTH - WIDTH) / 2;
                y = player.getY() - HEIGHT;
            }

            case DOWN -> {
                // 玩家下方，水平居中
                x = player.getX() + (Player.WIDTH - WIDTH) / 2;
                y = player.getY() + Player.HEIGHT;
            }

            case LEFT -> {
                // 玩家左边，垂直居中
                x = player.getX() - WIDTH;
                y = player.getY() + (Player.HEIGHT - HEIGHT) / 2;
            }

            case RIGHT -> {
                // 玩家右边，垂直居中
                x = player.getX() + Player.WIDTH;
                y = player.getY() + (Player.HEIGHT - HEIGHT) / 2;
            }
        }
    }



    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    // 判断本交互框 和目标矩形是否相交
    public boolean intersects(double targetX, double targetY, double targetW, double targetH){
        return  this.x < targetX + targetW
                && this.x + WIDTH > targetX
                && this.y < targetY + targetH
                && this.y + HEIGHT > targetY;
    }
}
