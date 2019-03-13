package com.yotwei.battlecity.game.object.bullet;

import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.tank.AbstractTank;

/**
 * Created by YotWei on 2019/3/9.
 */
public class GenericBullet extends AbstractBullet {

    protected GenericBullet(LevelContext lvlCtx, int bulletId, AbstractTank associateTank) {
        super(lvlCtx, bulletId, associateTank);
    }
}
