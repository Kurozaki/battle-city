package com.yotwei.battlecity.game.objects.properties.buff;

/**
 * Created by YotWei on 2019/4/10.
 */
public class Buff<T> {

    public static final int TYPE_SLOW_DOWN = 1;
    public static final int TYPE_MOVE_SPEED_UP = 2;
    public static final int TYPE_GUARD = 3;
    public static final int TYPE_ADD_BULLET_SLOT = 4;

    /**
     * buff 的类型（或者效果）
     */
    private final int type;

    /**
     * buff 作用时间计数器
     */
    private int ticker;

    private final T value;

    public Buff(int type, int tick, T value) {
        this.type = type;
        this.ticker = tick;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public int updateTicker() {
        return --ticker;
    }

    public T getValue() {
        return value;
    }
}
