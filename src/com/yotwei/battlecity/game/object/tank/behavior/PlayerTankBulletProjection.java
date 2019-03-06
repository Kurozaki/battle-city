package com.yotwei.battlecity.game.object.tank.behavior;

import com.yotwei.battlecity.game.object.GameObjectFactory;
import com.yotwei.battlecity.game.object.bullet.AbstractBullet;
import com.yotwei.battlecity.game.object.tank.PlayerTank;

import java.awt.*;

/**
 * Created by YotWei on 2019/3/6.
 */
@SuppressWarnings("WeakerAccess")
public class PlayerTankBulletProjection
        extends AbstractTankBulletProjection<PlayerTank> {

    private boolean projAFlag;

    protected PlayerTankBulletProjection(PlayerTank tank) {
        super(tank);
    }

    @Override
    public AbstractBullet getProjBullet() {

        PlayerTank tank = getAccessTank();

        if (!projAFlag && tank.getControlKeys().isProjA()) {

            projAFlag = true;

            //
            // create a bullet
            // set tank's direction as bullet's direction
            // set tank center as initialize coordinate
            //
            AbstractBullet bullet =
                    GameObjectFactory.createBullet(tank.getLevelContext(), 1);

            bullet.setDirection(tank.getDirection());

            Rectangle tankHitbox = tank.getHitbox();
            Rectangle bulletHitbox = bullet.getHitbox();
            bulletHitbox.setLocation(
                    tankHitbox.x + (tankHitbox.width - bulletHitbox.width >> 1),
                    tankHitbox.y + (tankHitbox.height - bulletHitbox.height >> 1)
            );

            return bullet;

        } else {
            projAFlag = tank.getControlKeys().isProjA();
            return null;
        }
    }
}
