package com.yotwei.bc.battlecity.level.obj.property;

/**
 * Created by YotWei on 2018/6/17.
 */
public enum Direction {
    UP, LEFT, DOWN, RIGHT;

    public Direction opposite() {
        switch (this) {
            case UP:
                return DOWN;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            case DOWN:
                return UP;
            default:
                return null;
        }
    }

    public boolean isHorizontal() {
        return this == LEFT || this == RIGHT;
    }
}
