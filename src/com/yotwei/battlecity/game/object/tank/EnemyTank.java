package com.yotwei.battlecity.game.object.tank;

import com.yotwei.battlecity.game.engine.ResourcePackage;
import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.properties.Direction;
import com.yotwei.battlecity.game.object.tank.behavior.AbstractTankBulletProjection;
import com.yotwei.battlecity.game.object.tank.behavior.AbstractTankMovement;

/**
 * Created by YotWei on 2019/3/3.
 */
public class EnemyTank extends AbstractTank {

    public EnemyTank(LevelContext lvlCtx, int enemyTypeId) {
        super(lvlCtx);

        image = ResourcePackage.getImage("enemy-" + enemyTypeId);
        direction = Direction.DOWN;

        tankMovement = AbstractTankMovement.enemy(this);
        tankBulletProj = AbstractTankBulletProjection.none(this);
    }

    @Override
    public void onInactive() {
        super.onInactive();

        // trigger enemy death event
        LevelContext.Event ev = LevelContext.Event.wrap("enemyDeath");
        getLevelContext().triggerEvent(ev);
    }

    @Override
    protected int calcMoveSpeed() {
        return 240;
    }
}
