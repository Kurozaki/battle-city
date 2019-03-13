package com.yotwei.battlecity.game.object.item;

import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.tank.AbstractTank;

import java.util.Map;

/**
 * Created by YotWei on 2019/3/11.
 */
public class TankSpeedUpItem extends AbstractItem {

    public TankSpeedUpItem(LevelContext lvlCtx, int itemId) {
        super(lvlCtx, itemId);
    }

    @Override
    protected boolean onTankPickUp(AbstractTank tank) {

        float speedUp = (float) tank.getExtra().getOrDefault("speedUp", 0.0f);

        if (speedUp < 0.6f) {   // speed up limited
            speedUp += 0.2f;
            tank.getExtra().put("speedUp", speedUp);
        }

        return true;
    }
}
