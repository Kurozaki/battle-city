package com.yotwei.battlecity.game.components.item;

import com.yotwei.battlecity.game.components.listeners.CollisionListener;
import com.yotwei.battlecity.game.objects.GameObject;
import com.yotwei.battlecity.game.objects.prefabs.Item;
import com.yotwei.battlecity.game.objects.prefabs.tanks.PlayerTank;
import com.yotwei.battlecity.game.objects.prefabs.tanks.Tank;

import java.awt.*;
import java.util.Set;

/**
 * Created by YotWei on 2019/4/10.
 */
public class ItemAddLiveCollisionListener extends CollisionListener<Item> {

    private final Item item;

    public ItemAddLiveCollisionListener(Item item) {
        super(item);
        this.item = item;
    }

    @Override
    public void onTouchBound(Rectangle boundary) {

    }

    @Override
    public void onCollide(GameObject tank) {
        if (tank.getTag().equals("PlayerTank")) {
            item.getLevelHandler().incPlayerLive((PlayerTank) tank);
            item.setActive(false);
        }
    }
}
