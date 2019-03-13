package com.yotwei.battlecity.game.object.item;

import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.tank.AbstractTank;

/**
 * Created by YotWei on 2019/3/11.
 */
public class ClearEnemyItem extends AbstractItem {

    public ClearEnemyItem(LevelContext lvlCtx, int itemId) {
        super(lvlCtx, itemId);
    }

    @Override
    protected boolean onTankPickUp(AbstractTank tank) {
        if (tank.getTag().equals("Tank-Player")) {
            getLevelContext()
                    .triggerEvent(LevelContext.Event.wrap("clearEnemyOnScreen"));
            return true;
        }
        return false;
    }
}
