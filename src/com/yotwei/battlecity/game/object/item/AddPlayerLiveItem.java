package com.yotwei.battlecity.game.object.item;

import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.tank.AbstractTank;
import com.yotwei.battlecity.game.object.tank.PlayerTank;

/**
 * Created by YotWei on 2019/3/11.
 */
public class AddPlayerLiveItem extends AbstractItem {

    public AddPlayerLiveItem(LevelContext lvlCtx, int itemId) {
        super(lvlCtx, itemId);
    }

    @Override
    protected boolean onTankPickUp(AbstractTank tank) {
        if (tank.getTag().equals("Tank-Enemy"))
            return false;

        tank.getExtra().put("addLiveCount", true);
        return true;
    }
}
