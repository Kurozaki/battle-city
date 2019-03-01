package com.yotwei.battlecity.game.object;

import java.awt.*;

/**
 * Created by YotWei on 2019/2/25.
 */
public interface Physic {

    interface CollideAble<_HitboxType extends Shape> {

        _HitboxType getHitbox();

        void onCollide(CollideAble<? extends Shape> anotherObject);
    }

    interface Movable {

    }
}
