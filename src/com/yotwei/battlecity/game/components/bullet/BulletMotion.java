package com.yotwei.battlecity.game.components.bullet;

import com.yotwei.battlecity.game.components.GOComponent;
import com.yotwei.battlecity.game.objects.properties.Direction;

/**
 * Created by YotWei on 2019/3/31.
 */
public class BulletMotion implements GOComponent {

    private final Direction direction;
    private final int speed;

    private int speedSub;

    public BulletMotion(Direction direction) {
        this(direction, 0x240);
    }

    public BulletMotion(Direction direction, int speed) {
        this.direction = direction;
        this.speed = speed;
    }

    @Override
    public String name() {
        return "BulletMotion";
    }

    public int getMoveDist() {
        speedSub += speed;
        int md = speedSub >> 8;
        speedSub &= 0xff;
        return md;
    }

    public Direction direction() {
        return direction;
    }
}
