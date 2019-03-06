package com.yotwei.battlecity.game.object.tank.behavior;

import com.yotwei.battlecity.game.object.properties.Direction;
import com.yotwei.battlecity.game.object.tank.AbstractTank;
import com.yotwei.battlecity.game.object.tank.EnemyTank;
import com.yotwei.battlecity.game.object.tank.PlayerTank;

/**
 * Created by YotWei on 2019/3/1.
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractTankMovement<_TankType extends AbstractTank> {

    private final _TankType accessTank;

    protected AbstractTankMovement(_TankType tank) {
        this.accessTank = tank;
    }

    protected _TankType getAccessTank() {
        return accessTank;
    }

    /**
     * decide the next moving direction of access tank
     * method will be call every frame
     *
     * @return an enum value in {@link com.yotwei.battlecity.game.object.properties.Direction}
     * null value stand for no move
     */
    public abstract Direction nextMoveDirection();


    public static AbstractTankMovement<PlayerTank> player(PlayerTank tank) {
        return new PlayerTankMovement(tank);
    }

    public static AbstractTankMovement<EnemyTank> enemy(EnemyTank tank) {
        return new EnemyTankMovement(tank);
    }
}
