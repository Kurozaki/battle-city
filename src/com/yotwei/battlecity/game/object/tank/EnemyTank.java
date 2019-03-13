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

    protected int speedBase;

    public EnemyTank(LevelContext lvlCtx, int enemyTypeId) {
        super(lvlCtx);

        image = ResourcePackage.getImage("enemy-" + enemyTypeId);
        direction = Direction.DOWN;

        switch (enemyTypeId) {
            case 1:
                tankBulletProj = AbstractTankBulletProjection.enemy(this, "default");
                tankDurability = 100;
                speedBase =0x120;
                break;

            case 2:
                tankBulletProj = AbstractTankBulletProjection.enemy(this, "default");
                tankDurability = 100;
                speedBase = 0x140;
                break;

            case 3:
                tankBulletProj = AbstractTankBulletProjection.enemy(this, "ap");
                tankDurability = 600;
                speedBase = 0x110;
                break;

            case 4:
                tankBulletProj = AbstractTankBulletProjection.enemy(this, "freeze");
                tankDurability = 300;
                speedBase = 0x130;
                break;

            case 5:
                tankBulletProj = AbstractTankBulletProjection.enemy(this,"burst");
                tankDurability = 400;
                speedBase = 0x110;
                break;

            default:
                throw new RuntimeException("Illegal enemy id: " + enemyTypeId);
        }

        tankMovement = AbstractTankMovement.enemy(this);
    }

    @Override
    public void onActive() {
        // set tag
        setTag("Tank-Enemy");

        super.onActive();

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

        float factory = 1.0f;

        //
        // freeze check
        //
        int freezeTime = (int) extra.getOrDefault("freeze", 0);
        if (freezeTime > 0) {
            extra.put("freeze", freezeTime - 1);
            factory = 0.25f;
        }

        return (int) (speedBase * factory);
    }

}
