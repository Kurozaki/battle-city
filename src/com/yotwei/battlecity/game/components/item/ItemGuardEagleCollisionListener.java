package com.yotwei.battlecity.game.components.item;

import com.yotwei.battlecity.game.LevelHandler;
import com.yotwei.battlecity.game.components.listeners.CollisionListener;
import com.yotwei.battlecity.game.objects.GameObject;
import com.yotwei.battlecity.game.objects.prefabs.Item;

import java.awt.*;

/**
 * Created by YotWei on 2019/4/12.
 */
public class ItemGuardEagleCollisionListener extends CollisionListener<Item> {

    private final Item item;

    public ItemGuardEagleCollisionListener(Item gameObject) {
        super(gameObject);
        this.item = gameObject;
    }

    @Override
    public void onTouchBound(Rectangle boundary) {

    }

    @Override
    public void onCollide(GameObject tank) {
        if (tank.getTag().equals("PlayerTank")) {
            tank.getLevelHandler().getEagle().strengthenAround();
            item.setActive(false);
        }
    }
}
