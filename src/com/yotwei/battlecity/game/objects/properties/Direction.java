package com.yotwei.battlecity.game.objects.properties;

/**
 * Created by YotWei on 2019/3/20.
 */
public enum Direction {

    LEFT(0), UP(1), RIGHT(2), DOWN(3);

    public final int index;

    Direction(int index) {
        this.index = index;
    }

    public boolean isVertical() {
        return this == UP || this == DOWN;
    }

    public boolean isHorizontal() {
        return this == LEFT || this == RIGHT;
    }

    public static boolean isOpposite(Direction d1, Direction d2) {
        return d1 != null && d2 != null &&
                Math.abs(d1.index - d2.index) == 2;
    }
}
