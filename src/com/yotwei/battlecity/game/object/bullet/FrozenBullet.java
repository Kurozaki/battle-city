package com.yotwei.battlecity.game.object.bullet;

import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.properties.Physic;
import com.yotwei.battlecity.game.object.tank.AbstractTank;

import java.awt.*;

/**
 * Created by YotWei on 2019/3/11.
 */
public class FrozenBullet extends AbstractBullet {

    protected FrozenBullet(LevelContext lvlCtx, int bulletId, AbstractTank associateTank) {
        super(lvlCtx, bulletId, associateTank);
    }

    @Override
    public void onActive() {

        // it has lower atk value
        bulletATK = 50;

        super.onActive();
    }

    @Override
    public void onCollide(Physic<? extends Shape> anotherObject) {

        // record origin bullet atk
        int bullATKOrigin = bulletATK;

        super.onCollide(anotherObject);

        //
        // if bulletATK decrease, it means bullet hit object
        //
        if (bulletATK < bullATKOrigin) {
            if (anotherObject instanceof AbstractTank) {
                ((AbstractTank) anotherObject)
                        .getExtra().put("freeze", 60 * 2 /* freeze 2s */);
            }
        }
    }
}
