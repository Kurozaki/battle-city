package com.yotwei.battlecity.game.object.tank.behavior;

import com.yotwei.battlecity.game.object.properties.Direction;
import com.yotwei.battlecity.game.object.tank.AbstractTank;
import com.yotwei.battlecity.game.object.tank.PlayerTank;


/**
 * Created by YotWei on 2019/3/1.
 */
@SuppressWarnings("WeakerAccess")
public class PlayerTankMovement extends AbstractTankMovement<PlayerTank> {

    protected PlayerTankMovement(PlayerTank tank) {
        super(tank);
    }

    @Override
    public Direction nextMoveDirection() {

        PlayerTank.ControlKeys ck = getAccessTank().getControlKeys();

        if (ck.isRight()) {
            return Direction.RIGHT;
        } else if (ck.isLeft()) {
            return Direction.LEFT;
        } else if (ck.isUp()) {
            return Direction.UP;
        } else if (ck.isDown()) {
            return Direction.DOWN;
        } else {
            return null;
        }
    }
}
