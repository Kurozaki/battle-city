package com.yotwei.battlecity.game.components.item;

import com.yotwei.battlecity.game.LevelHandler;
import com.yotwei.battlecity.game.components.listeners.CollisionListener;
import com.yotwei.battlecity.game.components.tank.TankBuffContainer;
import com.yotwei.battlecity.game.objects.GameObject;
import com.yotwei.battlecity.game.objects.prefabs.Item;
import com.yotwei.battlecity.game.objects.properties.buff.Buff;

import java.awt.*;

/**
 * Created by YotWei on 2019/4/11.
 */
public class ItemFreezeCollisionListener extends CollisionListener<Item> {

    public ItemFreezeCollisionListener(Item gameObject) {
        super(gameObject);
    }

    @Override
    public void onTouchBound(Rectangle boundary) {

    }

    @Override
    public void onCollide(GameObject tank) {
        if (tank.getTag().equals("PlayerTank")) {
            final LevelHandler lh = tank.getLevelHandler();
            // 遍历关卡内的所有坦克
            for (GameObject enemyTank : lh.retrieve("tanks", lh.getBoundary())) {
                if (enemyTank.getTag().equals("EnemyTank")) {

                    // 如果是敌方坦克，添加一个 80% 的减速 buff
                    TankBuffContainer buffs = enemyTank.getComponent("BuffContainer");
                    buffs.addBuff(new Buff<>(
                            Buff.TYPE_SLOW_DOWN,
                            6 * 60,
                            0.8f));
                }
            }
            gameObject.setActive(false);
        }
    }
}
