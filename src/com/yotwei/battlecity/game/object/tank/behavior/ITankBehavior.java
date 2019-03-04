package com.yotwei.battlecity.game.object.tank.behavior;

import com.yotwei.battlecity.game.object.tank.AbstractTank;
import com.yotwei.battlecity.game.object.tank.EnemyTank;
import com.yotwei.battlecity.game.object.tank.PlayerTank;

/**
 * Created by YotWei on 2019/3/1.
 */
@SuppressWarnings("WeakerAccess")
public abstract class ITankBehavior<_TankType extends AbstractTank> {

    private final _TankType accessTank;

    protected ITankBehavior(_TankType tank) {
        this.accessTank = tank;
    }

    public _TankType getAccessTank() {
        return accessTank;
    }

    /**
     * decide the next moving direction of access tank
     *
     * @return an enum value in {@link com.yotwei.battlecity.game.object.tank.AbstractTank.Direction}
     * null value stand for no move
     */
    public abstract AbstractTank.Direction nextMoveDirection();


    public static ITankBehavior<PlayerTank> player(PlayerTank tank) {
        return new PlayerTankBehavior(tank);
    }

    public static ITankBehavior<EnemyTank> enemy(EnemyTank tank) {
        return new EnemyTankBehavior(tank);
    }
}
