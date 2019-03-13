package com.yotwei.battlecity.game.object.bullet;

import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.tank.AbstractTank;

/**
 * Created by YotWei on 2019/3/11.
 */
public class ArmourPiercingBullet extends AbstractBullet {

    protected ArmourPiercingBullet(LevelContext lvlCtx, int bulletId, AbstractTank associateTank) {
        super(lvlCtx, bulletId, associateTank);

        // AP-Bullet has high atk value
        bulletATK = 800;
    }
}
