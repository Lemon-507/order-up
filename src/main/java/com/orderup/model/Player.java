package com.orderup.model;


import lombok.Data;

import java.util.EnumSet;
import java.util.Set;

@Data
public class Player {
    public static final double WIDTH = 40;
    public static final double HEIGHT = 40;
    private static final double DEFAULT_SPEED = 220;
    public boolean isHolding = false;

    private Direction direction = Direction.DOWN;

    public Direction getDirection() {
        return direction;
    }

  public final Set<Direction> pressedDirections = EnumSet.noneOf(Direction.class);
    public double x;
    public double y;
    private double speed;

    public Player(double startX, double startY) {
        this(startX, startY, DEFAULT_SPEED);
    }

    public Player(double startX, double startY, double speed) {
        this.x = startX;
        this.y = startY;
        this.speed = speed;
    }

    public void press(Direction direction) {
        this.direction = direction; // 记录最后按下的方向
        pressedDirections.add(direction);
    }

    public void release(Direction direction) {
        pressedDirections.remove(direction);
    }

    public void clearMovement() {
        pressedDirections.clear();
    }
}
