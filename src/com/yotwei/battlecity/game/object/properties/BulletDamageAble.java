package com.yotwei.battlecity.game.object.properties;

/**
 * Created by YotWei on 2019/3/6.
 */
public interface BulletDamageAble {

    /**
     * try to damage a BulletDamageAble object with specify damage value
     *
     * @param damageValue the specify damage value
     * @return the read damage value on object
     */
    int tryDamage(int damageValue);
}
