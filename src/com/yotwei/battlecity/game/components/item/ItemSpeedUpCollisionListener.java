package com.yotwei.battlecity.game.components.item;

import com.yotwei.battlecity.game.components.GOComponent;
import com.yotwei.battlecity.game.components.listeners.CollisionListener;
import com.yotwei.battlecity.game.components.tank.TankBuffContainer;
import com.yotwei.battlecity.game.objects.GameObject;
import com.yotwei.battlecity.game.objects.prefabs.Item;
import com.yotwei.battlecity.game.objects.properties.buff.Buff;

import java.awt.*;

/**
 * Created by YotWei on 2019/4/11.
 */
public class ItemSpeedUpCollisionListener extends CollisionListener<Item> {

    public ItemSpeedUpCollisionListener(Item gameObject) {
        super(gameObject);
    }

    @Override
    public void onTouchBound(Rectangle boundary) {

    }

    @Override
    public void onCollide(GameObject tank) {
        if (tank.getTag().equals("PlayerTank") || tank.getTag().equals("EnemyTank")) {
            TankBuffContainer buffs = tank.getComponent("BuffContainer");

            // 检查是否已经有加速 buff
            Buff<Float> oldBuff = buffs.getBuff(Buff.TYPE_MOVE_SPEED_UP);

            float speedUpRate = oldBuff == null ? 0.1f : oldBuff.getValue() + 0.1f;
            if (speedUpRate > 0.5f) {   // 限制最高提速 40%
                speedUpRate = 0.5f;
            }

            // 加入新 buff
            buffs.addBuff(new Buff<>(
                    Buff.TYPE_MOVE_SPEED_UP,
                    Integer.MAX_VALUE,
                    speedUpRate
            ));

            gameObject.setActive(false);
        }
    }
}
