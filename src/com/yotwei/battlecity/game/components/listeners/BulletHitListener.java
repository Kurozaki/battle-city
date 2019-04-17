package com.yotwei.battlecity.game.components.listeners;

import com.yotwei.battlecity.game.components.GOListener;
import com.yotwei.battlecity.game.objects.prefabs.bullet.Bullet;

/**
 * Created by YotWei on 2019/4/3.
 */
public abstract class BulletHitListener<_ObjectType> implements GOListener {

    protected final _ObjectType gameObject;

    protected BulletHitListener(_ObjectType gameObject) {
        this.gameObject = gameObject;
    }

    @Override
    public String name() {
        return "BulletHitListener";
    }

    /**
     * 子弹撞击接口
     *
     * @param bullet 撞击过来的子弹
     * @return 子弹对当前物体所造成的伤害，子弹会根据此返回值扣除自身威力
     */
    public abstract int onBulletHit(Bullet bullet);
}
