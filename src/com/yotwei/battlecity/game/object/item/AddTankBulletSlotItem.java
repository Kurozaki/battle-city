package com.yotwei.battlecity.game.object.item;

import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.tank.AbstractTank;

/**
 * Created by YotWei on 2019/3/11.
 */
public class AddTankBulletSlotItem extends AbstractItem {

    public AddTankBulletSlotItem(LevelContext lvlCtx, int itemId) {
        super(lvlCtx, itemId);
    }

    @Override
    protected boolean onTankPickUp(AbstractTank tank) {

        int addBulletSlot = (int) tank.getExtra().getOrDefault("addBulletSlot", 0);

        // add 1 bullet slot
        addBulletSlot += 1;
        tank.getExtra().put("addBulletSlot", addBulletSlot);

        return true;
    }
}
