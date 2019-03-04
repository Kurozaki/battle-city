package com.yotwei.battlecity.game.object.properties;

import java.awt.*;

/**
 * Created by YotWei on 2019/2/25.
 */
public interface Physic<_HitboxType extends Shape> {

    _HitboxType getHitbox();

    void onCollide(Physic<? extends Shape> anotherObject);

    void onTouchBound(final Rectangle bound);
}
