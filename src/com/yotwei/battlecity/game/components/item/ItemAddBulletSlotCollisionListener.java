package com.yotwei.battlecity.game.components.item;

import com.yotwei.battlecity.game.components.listeners.CollisionListener;
import com.yotwei.battlecity.game.components.tank.TankBuffContainer;
import com.yotwei.battlecity.game.objects.GameObject;
import com.yotwei.battlecity.game.objects.prefabs.Item;
import com.yotwei.battlecity.game.objects.properties.buff.Buff;

import java.awt.*;

/**
 * Created by YotWei on 2019/4/12.
 */
public class ItemAddBulletSlotCollisionListener extends CollisionListener<Item> {

    private final Item item;

    public ItemAddBulletSlotCollisionListener(Item gameObject) {
        super(gameObject);
        this.item = gameObject;
    }

    @Override
    public void onTouchBound(Rectangle boundary) {

    }

    @Override
    public void onCollide(GameObject tank) {
        if (tank.getTag().equals("PlayerTank")) {

            TankBuffContainer buffs = tank.getComponent("BuffContainer");

            Buff<Integer> oldBuff = buffs.getBuff(Buff.TYPE_ADD_BULLET_SLOT);
            Buff<Integer> newBuff;

            if (oldBuff == null) {
                newBuff = new Buff<>(Buff.TYPE_ADD_BULLET_SLOT, Integer.MAX_VALUE, 1);
            } else {
                newBuff = new Buff<>(Buff.TYPE_ADD_BULLET_SLOT, Integer.MAX_VALUE, oldBuff.getValue() + 1);
            }
            buffs.addBuff(newBuff);

            item.setActive(false);
        }
    }
}
