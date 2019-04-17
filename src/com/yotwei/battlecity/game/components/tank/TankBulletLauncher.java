package com.yotwei.battlecity.game.components.tank;

import com.yotwei.battlecity.game.components.GOComponent;
import com.yotwei.battlecity.game.objects.prefabs.bullet.Bullet;
import com.yotwei.battlecity.game.objects.prefabs.tanks.Tank;

import java.util.Set;

/**
 * Created by YotWei on 2019/3/31.
 */
public abstract class TankBulletLauncher implements GOComponent {

    protected final Tank tank;

    public TankBulletLauncher(Tank tank) {
        this.tank = tank;
    }

    @Override
    public String name() {
        return "BulletLauncher";
    }

    /**
     * 以后可能扩充到一次发射多颗子弹
     */
    public abstract Set<Bullet> getBullets();

    public Tank getTank() {
        return tank;
    }
}
