package com.yotwei.battlecity.game.object.tank;

import com.yotwei.battlecity.game.engine.ResourcePackage;
import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.tank.behavior.ITankBehavior;

/**
 * Created by YotWei on 2019/3/3.
 */
public class EnemyTank extends AbstractTank {

    public EnemyTank(LevelContext lvlCtx, int enemyTypeId) {
        super(lvlCtx);

        behavior = new ITankBehavior<EnemyTank>(this) {
            @Override
            public Direction nextMoveDirection() {
                return Direction.RIGHT;
            }
        };

        image = ResourcePackage.getImage("enemy-" + enemyTypeId);
        direction = Direction.DOWN;

        behavior = ITankBehavior.enemy(this);
    }

    @Override
    protected int calcMoveSpeed() {
        return 240;
    }
}
