package com.yotwei.battlecity.game.components.tank;

import com.yotwei.battlecity.game.components.GOBoundingBox;
import com.yotwei.battlecity.game.objects.prefabs.bullet.Bullet;
import com.yotwei.battlecity.game.objects.prefabs.tanks.Tank;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by YotWei on 2019/4/1.
 */
public class TankBulletLauncherEnemy extends TankBulletLauncher {

    private static final int MIN_TICK = 100;

    private final int bulletTypeA;
    private final int bulletTypeB;

    private int ticker;
    private Random rand;

    public TankBulletLauncherEnemy(Tank tank, int bulletTypeA, int bulletTypeB) {
        super(tank);

        this.bulletTypeA = bulletTypeA;
        this.bulletTypeB = bulletTypeB;

        this.rand = new Random();
    }

    @Override
    public Set<Bullet> getBullets() {
        Set<Bullet> set = new HashSet<>();

        if (ticker-- <= 0) {
            // 触发炮弹发射
            int bulletType;
            if (Math.random() > 0.5) {  // 有 50% 的概率发射 A 类子弹，50% 概率发射 B 类子弹
                bulletType = bulletTypeA;
            } else {
                bulletType = bulletTypeB;
            }

            Bullet bullet = Bullet.createInstance(this, bulletType);
            setBulletCoordinate(bullet);

            set.add(bullet);

            // 重置计数器
            ticker = MIN_TICK + rand.nextInt(MIN_TICK);
        }

        return set;
    }

    private void setBulletCoordinate(Bullet bullet) {
        final GOBoundingBox tankBox = tank.getComponent("BoundingBox");
        final GOBoundingBox bulletBox = bullet.getComponent("BoundingBox");
        bulletBox.x = tankBox.x + (tankBox.width - bulletBox.width >> 1);
        bulletBox.y = tankBox.y + (tankBox.height - bulletBox.height >> 1);
    }
}
