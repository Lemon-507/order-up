package com.orderup.model;

import java.util.EnumSet;
import java.util.Set;

/**
 * 玩家位置、朝向、输入状态和手持物品。
 */
public class Player {
    public static final double WIDTH = 40;
    public static final double HEIGHT = 40;
    private static final double DEFAULT_SPEED = 220;

    private final Set<Direction> pressedDirections = EnumSet.noneOf(Direction.class);
    private double x;
    private double y;
    private double speed;
    private Direction facingDirection = Direction.DOWN;
    private GameItem heldItem;

    public Player(double x, double y) {
        this(x, y, DEFAULT_SPEED);
    }

    public Player(double x, double y, double speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public void press(Direction direction) {
        facingDirection = direction;
        pressedDirections.add(direction);
    }

    public void release(Direction direction) {
        pressedDirections.remove(direction);
    }

    public void clearInput() {
        pressedDirections.clear();
    }

    public boolean isMoving(Direction direction) {
        return pressedDirections.contains(direction);
    }

    public boolean pickUp(GameItem item) {
        if (item == null || heldItem != null) {
            return false;
        }
        heldItem = item;
        return true;
    }

    public GameItem releaseHeldItem() {
        GameItem released = heldItem;
        heldItem = null;
        return released;
    }

    public boolean hasHeldItem() {
        return heldItem != null;
    }

    public GameItem getHeldItem() {
        return heldItem;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Direction getFacingDirection() {
        return facingDirection;
    }
}
