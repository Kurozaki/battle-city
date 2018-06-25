package com.yotwei.bc.battlecity.level.obj.tank;

import com.yotwei.bc.battlecity.level.lvl.Event;
import com.yotwei.bc.battlecity.level.lvl.Level;
import com.yotwei.bc.battlecity.level.obj.bullet.BulletProjectStrategy;
import com.yotwei.bc.battlecity.level.obj.property.Direction;
import com.yotwei.bc.battlecity.level.obj.property.Type;
import com.yotwei.bc.battlecity.others.PlayerKeys;

/**
 * Created by YotWei on 2018/6/17.
 */
public class PlayerTank extends AbstractTank {

    /*
     * player's control keys
     */
    private final PlayerKeys _playerKeys = PlayerKeys.P1_KEYS;

    /*
     * player's bullet project strategy
     *
     * project bullet when the PROJECT key is pressed
     */
    private final BulletProjectStrategy _bulletProjector = new BulletProjectStrategy() {

        private boolean _projectFlag = false;

        @Override
        public boolean isAbleProjectBullet() {
            if (_playerKeys.isProjectActivate()) {
                if (_projectFlag) {
                    _projectFlag = false;
                    return true;
                }
            } else {
                _projectFlag = true;
            }
            return false;

        }
    };

    private PlayerTank(Level level) {
        super(0, level);

        // set type and initial moving speed
        this.setType(Type.PLAYER);
        this.movsp.setSpeed(0x160);
    }

    public static PlayerTank create(int initX, int initY, Level level) {
        PlayerTank tank = new PlayerTank(level);
        tank.setLocation(initX, initY);
        return tank;
    }

    /**
     * get next moving direction according to the player keys
     *
     * @return direction, stands for the next moving direction
     */
    @Override
    protected Direction nextDirection() {
        if (_playerKeys.isDownActivate())
            return Direction.DOWN;
        if (_playerKeys.isUpActivate())
            return Direction.UP;
        if (_playerKeys.isLeftActivate())
            return Direction.LEFT;
        if (_playerKeys.isRightActivate())
            return Direction.RIGHT;
        return null;
    }

    @Override
    protected boolean isAbleProjectBullet() {
        return _bulletProjector.isAbleProjectBullet();
    }

    /**
     * send a PlayerDeath event when player tank is destroyed
     */
    @Override
    protected void destroySelf() {
        super.destroySelf();
        getLevel().addEvent(Event.playerDeath());
    }
}
