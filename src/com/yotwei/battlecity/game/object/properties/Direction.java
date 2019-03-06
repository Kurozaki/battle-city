package com.yotwei.battlecity.game.object.properties;

/**
 * Created by YotWei on 2019/3/6.
 * <p>
 * direction is a enum
 * to tell which direction the tank will move to in the next step
 */
public enum Direction {

    LEFT(0), UP(1), RIGHT(2), DOWN(3);

    public final int index;

    Direction(int index) {
        this.index = index;
    }

    public static boolean isOpposite(Direction d1, Direction d2) {
        return d1 != null && d2 != null &&
                Math.abs(d1.index - d2.index) == 2;
    }
}