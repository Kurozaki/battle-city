package com.yotwei.battlecity.game.components.item;

import com.yotwei.battlecity.game.components.listeners.CollisionListener;
import com.yotwei.battlecity.game.components.tank.TankBuffContainer;
import com.yotwei.battlecity.game.objects.GameObject;
import com.yotwei.battlecity.game.objects.prefabs.Item;
import com.yotwei.battlecity.game.objects.prefabs.tanks.Tank;
import com.yotwei.battlecity.game.objects.properties.buff.Buff;
import sun.java2d.pipe.BufferedContext;

import java.awt.*;

/**
 * Created by YotWei on 2019/4/11.
 */
public class ItemGuardCollisionListener extends CollisionListener<Item> {

    public ItemGuardCollisionListener(Item gameObject) {
        super(gameObject);
    }

    @Override
    public void onTouchBound(Rectangle boundary) {

    }

    @Override
    public void onCollide(GameObject tank) {
        if (tank.getTag().equals("EnemyTank") ||
                tank.getTag().equals("PlayerTank")) {

            TankBuffContainer buffs = tank.getComponent("BuffContainer");
            // 给坦克添加一个 12 秒的无敌 buff
            buffs.addBuff(new Buff<>(Buff.TYPE_GUARD, 12 * 60, 0));

            // 道具失效
            gameObject.setActive(false);
        }
    }
}
