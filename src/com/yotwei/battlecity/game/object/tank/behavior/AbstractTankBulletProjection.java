package com.yotwei.battlecity.game.object.tank.behavior;

import com.yotwei.battlecity.game.object.bullet.AbstractBullet;
import com.yotwei.battlecity.game.object.tank.AbstractTank;
import com.yotwei.battlecity.game.object.tank.EnemyTank;
import com.yotwei.battlecity.game.object.tank.PlayerTank;

/**
 * Created by YotWei on 2019/3/6.
 */
public abstract class AbstractTankBulletProjection<_TankType extends AbstractTank> {

    private _TankType accessTank;

    protected AbstractTankBulletProjection(_TankType tank) {
        this.accessTank = tank;
    }

    protected _TankType getAccessTank() {
        return accessTank;
    }

    /**
     * to create an {@link AbstractBullet} instance to be projected
     * method will be called every frame
     *
     * @return an AbstractBullet instance,
     * null stands for do not project bullet
     */
    public abstract AbstractBullet getProjBullet();

    public static AbstractTankBulletProjection<PlayerTank> player(PlayerTank tank) {
        return new PlayerTankBulletProjection(tank);
    }

    public static <T extends AbstractTank> AbstractTankBulletProjection<T> none(T tank) {

        return new AbstractTankBulletProjection<T>(tank) {

            @Override
            public AbstractBullet getProjBullet() {
                return null;
            }
        };
    }
}
