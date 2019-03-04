package com.yotwei.battlecity.game.object.tank.behavior;

import com.yotwei.battlecity.game.object.tank.AbstractTank;
import com.yotwei.battlecity.game.object.tank.PlayerTank;


/**
 * Created by YotWei on 2019/3/1.
 */
@SuppressWarnings("WeakerAccess")
public class PlayerTankBehavior extends ITankBehavior<PlayerTank> {

    protected PlayerTankBehavior(PlayerTank tank) {
        super(tank);
    }

    @Override
    public AbstractTank.Direction nextMoveDirection() {

        PlayerTank.ControlKeys ck = getAccessTank().getControlKeys();

        if (ck.isRight()) {
            return AbstractTank.Direction.RIGHT;
        } else if (ck.isLeft()) {
            return AbstractTank.Direction.LEFT;
        } else if (ck.isUp()) {
            return AbstractTank.Direction.UP;
        } else if (ck.isDown()) {
            return AbstractTank.Direction.DOWN;
        } else {
            return null;
        }
    }
}
