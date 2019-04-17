package com.yotwei.battlecity.game.components.item;

import com.yotwei.battlecity.game.LevelHandler;
import com.yotwei.battlecity.game.components.listeners.CollisionListener;
import com.yotwei.battlecity.game.objects.GameObject;
import com.yotwei.battlecity.game.objects.prefabs.Item;

import java.awt.*;

/**
 * Created by YotWei on 2019/4/10.
 */
public class ItemClearEnemyCollisionListener extends CollisionListener<Item> {

    private final Item item;

    public ItemClearEnemyCollisionListener(Item gameObject) {
        super(gameObject);
        this.item = gameObject;
    }

    @Override
    public void onTouchBound(Rectangle boundary) {

    }

    @Override
    public void onCollide(GameObject tank) {
        if (tank.getTag().equals("PlayerTank")) {
            //
            // 清除屏幕上的所有敌人
            //
            final LevelHandler lh = tank.getLevelHandler();
            for (GameObject obj :
                    lh.retrieve("tanks", lh.getBoundary())) {
                if (obj.getTag().equals("EnemyTank")) {
                    obj.setActive(false);
                }
            }
            // 道具被拾起后消失
            item.setActive(false);
        }
    }
}
