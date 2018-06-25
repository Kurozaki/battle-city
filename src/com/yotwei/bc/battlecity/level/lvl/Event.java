package com.yotwei.bc.battlecity.level.lvl;


import com.yotwei.bc.battlecity.level.obj.tank.AbstractTank;
import com.yotwei.bc.battlecity.util.HashMapBuilder;

import java.awt.*;
import java.util.Map;

/**
 * Created by YotWei on 2018/6/18.
 */
public class Event {

    // level initial
    public static final int TYPE_LEVEL_INIT = 1;

    // entities events
    public static final int TYPE_PLAYER_DEATH = 2;
    public static final int TYPE_ENEMY_DEATH = 3;
    public static final int TYPE_BULLET_PROJECT = 4;

    // player prepare and generate
    public static final int TYPE_NEW_ENEMY_PREPARE = 5;
    public static final int TYPE_NEW_ENEMY_CREATE = 6;

    // enemy prepare and generate
    public static final int TYPE_PLAYER_PREPARE = 7;
    public static final int TYPE_PLAYER_CREATE = 8;

    // level status
    public static final int TYPE_BATTLE_FINISHED = 9;
    public static final int TYPE_GAME_OVER = 10;
    public static final int TYPE_LEVEL_OVER = 11;

    public static final int TYPE_CREATE_EFFECT = 12;

    private final int type;
    private final Map<String, Object> datBundle;
    private int delay;

    private Event(int type, int delay, Map<String, Object> datBundle) {
        this.type = type;
        this.delay = delay;
        this.datBundle = datBundle;
    }

    public int getType() {
        return type;
    }

    public Map<String, Object> getDataBundle() {
        return datBundle;
    }

    public boolean isReady() {
        return delay-- <= 0;
    }

    public static Event playerDeath() {
        return new Event(TYPE_PLAYER_DEATH, 0, null);
    }

    public static Event enemyDeath() {
        return new Event(TYPE_ENEMY_DEATH, 0, null);
    }

    public static Event levelInit() {
        return new Event(TYPE_LEVEL_INIT, 0, null);
    }

    public static Event projectBullet(AbstractTank launcherTank) {
        Map<String, Object> bundle = new HashMapBuilder<String, Object>()
                .put("launcher", launcherTank)
                .build();
        return new Event(TYPE_BULLET_PROJECT, 0, bundle);
    }

    public static Event playerPrepare(int delay, int x, int y) {
        Map<String, Object> bundle = new HashMapBuilder<String, Object>()
                .put("point", new Point(x, y))
                .build();
        return new Event(TYPE_PLAYER_PREPARE, delay, bundle);
    }

    public static Event playerCreate(int x, int y) {
        Map<String, Object> bundle = new HashMapBuilder<String, Object>()
                .put("point", new Point(x, y))
                .build();
        return new Event(TYPE_PLAYER_CREATE, 0, bundle);
    }

    public static Event enemyPrepare(int delay, int modelId, int x, int y) {
        Map<String, Object> bundle = new HashMapBuilder<String, Object>()
                .put("point", new Point(x, y))
                .put("modelId", modelId)
                .build();
        return new Event(TYPE_NEW_ENEMY_PREPARE, delay, bundle);
    }

    public static Event enemyCreate(int modelId, int x, int y) {
        Map<String, Object> bundle = new HashMapBuilder<String, Object>()
                .put("point", new Point(x, y))
                .put("modelId", modelId)
                .build();
        return new Event(TYPE_NEW_ENEMY_CREATE, 0, bundle);
    }

    public static Event createEffect(int effectId, int x, int y, int aliveTime) {
        Map<String, Object> bundle = new HashMapBuilder<String, Object>()
                .put("effectId", effectId)
                .put("x", x)
                .put("y", y)
                .put("aliveTime", aliveTime).build();
        return new Event(TYPE_CREATE_EFFECT, 0, bundle);
    }

    public static Event battleFinished() {
        return new Event(TYPE_BATTLE_FINISHED, 0, null);
    }

    public static Event gameOver() {
        return new Event(TYPE_GAME_OVER, 0, null);
    }

    public static Event levelOver(int delay) {
        return new Event(TYPE_LEVEL_OVER, delay, null);
    }
}
