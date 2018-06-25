package com.yotwei.bc.battlecity.level.obj.tank;

import com.yotwei.bc.battlecity.level.lvl.Event;
import com.yotwei.bc.battlecity.level.lvl.Level;
import com.yotwei.bc.battlecity.level.obj.bullet.BulletProjectStrategy;
import com.yotwei.bc.battlecity.level.obj.property.Direction;
import com.yotwei.bc.battlecity.level.obj.property.Type;

import java.awt.*;
import java.util.Random;

/**
 * Created by YotWei on 2018/6/18.
 */
public class EnemyTank extends AbstractTank {

    private final BulletProjectStrategy _bulletProjector = new BulletProjectStrategy() {

        private final int PROJECT_INTERVAL = 60;

        private int _counter = 0;

        @Override
        public boolean isAbleProjectBullet() {
            if (_counter++ > PROJECT_INTERVAL) {
                _counter = 0;
                return true;
            }
            return false;
        }
    };

    private EnemyTank(int modelId, Level level) {
        super(modelId, level);

        // set enemy type and initial moving speed
        this.setType(Type.ENEMY);
        this.movsp.setSpeed(0x100);
    }

    public static AbstractTank create(int modelId, int initX, int initY, Level level) {
        EnemyTank tank = new EnemyTank(modelId, level);
        tank.setLocation(initX, initY);
        return tank;
    }

    @Override
    protected Direction nextDirection() {

        // get next moving box
        Rectangle nextMoveBox = getNextMoveBox(direction, false);

        if (isMovBoxAbleToBePlace(nextMoveBox)) {
            // keep moving straightly
            return direction;
        }

        return Direction.values()[new Random().nextInt(Direction.values().length)];
    }

    @Override
    protected boolean isAbleProjectBullet() {
        return _bulletProjector.isAbleProjectBullet();
    }

    @Override
    protected void destroySelf() {
        super.destroySelf();
        getLevel().addEvent(Event.enemyDeath());
    }
}
