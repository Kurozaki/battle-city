package com.yotwei.battlecity.game.object.tank.behavior;

import com.yotwei.battlecity.game.object.GameObjectFactory;
import com.yotwei.battlecity.game.object.bullet.AbstractBullet;
import com.yotwei.battlecity.game.object.tank.PlayerTank;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by YotWei on 2019/3/6.
 */
@SuppressWarnings("WeakerAccess")
public class PlayerTankBulletProjection
        extends AbstractTankBulletProjection<PlayerTank> {

    private final Set<AbstractBullet> bulletSlots;

    private boolean projAFlag;

    protected PlayerTankBulletProjection(PlayerTank tank) {
        super(tank);

        bulletSlots = new HashSet<>();
    }

    @Override
    public AbstractBullet getProjBullet() {

        PlayerTank tank = getAccessTank();

        if (!projAFlag && tank.getControlKeys().isProjA()) {

            projAFlag = true;

            //
            // project condition check
            //
            bulletSlots.removeIf(bullet -> !bullet.isActive());
            if (bulletSlots.size() >= getAccessTank().getBulletSlotsCount()) {
                return null;
            }

            //
            // create a bullet
            // set tank center as initialize coordinate
            //
            AbstractBullet bullet =
                    GameObjectFactory.createBullet(tank, 1);

            Rectangle tankHitbox = tank.getHitbox();
            Rectangle bulletHitbox = bullet.getHitbox();
            bulletHitbox.setLocation(
                    tankHitbox.x + (tankHitbox.width - bulletHitbox.width >> 1),
                    tankHitbox.y + (tankHitbox.height - bulletHitbox.height >> 1)
            );

            bulletSlots.add(bullet);

            return bullet;

        } else {
            projAFlag = tank.getControlKeys().isProjA();
            return null;
        }
    }
}
