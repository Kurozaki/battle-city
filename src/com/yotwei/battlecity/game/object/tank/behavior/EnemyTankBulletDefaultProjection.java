package com.yotwei.battlecity.game.object.tank.behavior;

import com.yotwei.battlecity.game.object.GameObjectFactory;
import com.yotwei.battlecity.game.object.bullet.AbstractBullet;
import com.yotwei.battlecity.game.object.tank.EnemyTank;

import java.util.Random;

/**
 * Created by YotWei on 2019/3/10.
 */
public class EnemyTankBulletDefaultProjection
        extends AbstractTankBulletProjection<EnemyTank> {


    private Random rand;
    private int ticker;

    private int projectedBulletId;

    protected EnemyTankBulletDefaultProjection(EnemyTank tank) {
        super(tank);

        rand = new Random();
        ticker = 30 + rand.nextInt(30);
        projectedBulletId = 1;
    }

    public void setProjectedBulletId(int projectedBulletId) {
        this.projectedBulletId = projectedBulletId;
    }

    @Override
    public AbstractBullet getProjBullet() {

        if (ticker-- <= 0) {

            EnemyTank tank = getAccessTank();

            // reset ticker value
            ticker = rand.nextInt(120) + 60;
            AbstractBullet bullet = GameObjectFactory.createBullet(tank, projectedBulletId);

            // set bullet init location
            bullet.getHitbox().setLocation(
                    tank.getHitbox().x + (tank.getHitbox().width - bullet.getHitbox().width) / 2,
                    tank.getHitbox().y + (tank.getHitbox().height - bullet.getHitbox().height) / 2
            );

            return bullet;
        }

        return null;
    }
}
