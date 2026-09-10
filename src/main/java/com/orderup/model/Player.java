package com.orderup.model;


import java.util.EnumSet;
import java.util.Set;


public class Player {
    public static final double WIDTH = 40;
    public static final double HEIGHT = 40;
    private static final double DEFAULT_SPEED = 220;

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
        pressedDirections.add(direction);
    }

    public void release(Direction direction) {
        pressedDirections.remove(direction);
    }

    public void clearMovement() {
        pressedDirections.clear();
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
}
