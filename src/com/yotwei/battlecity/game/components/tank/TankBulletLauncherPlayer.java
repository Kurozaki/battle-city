package com.yotwei.battlecity.game.components.tank;

import com.yotwei.battlecity.framework.KeyInput;
import com.yotwei.battlecity.game.components.GOBoundingBox;
import com.yotwei.battlecity.game.objects.prefabs.bullet.Bullet;
import com.yotwei.battlecity.game.objects.prefabs.tanks.Tank;
import com.yotwei.battlecity.game.objects.properties.buff.Buff;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by YotWei on 2019/4/1.
 */
public class TankBulletLauncherPlayer extends TankBulletLauncher {

    private final Set<Bullet> bulletSet;
    private final int bulletSlotBase = 2;

    private boolean flagA = false, flagB = false;

    public TankBulletLauncherPlayer(Tank tank) {
        super(tank);

        bulletSet = new HashSet<>();
    }

    private int getBulletSlot() {
        int bulletSlot = bulletSlotBase;

        // 检查是否存在增加子弹发射上限的 buff
        TankBuffContainer buffs = tank.getComponent("BuffContainer");
        Buff<Integer> buff = buffs.getBuff(Buff.TYPE_ADD_BULLET_SLOT);
        if (buff != null) {
            bulletSlot += buff.getValue();
        }

        return bulletSlot;
    }

    @Override
    public Set<Bullet> getBullets() {
        Set<Bullet> bullets = new HashSet<>();

        boolean isPressedA = KeyInput.isKeyPressed(KeyEvent.VK_K);
//        boolean isPressedB = KeyInput.isKeyPressed(KeyEvent.VK_L);

        if (isPressedA && !flagA) {
            flagA = true;

            bulletSet.removeIf(bullet -> !bullet.isActive());
            if (bulletSet.size() < getBulletSlot()) {

                // 发射普通炮弹
                Bullet bullet = Bullet.createInstance(this, Bullet.TYPE_ID_COMMON);
                setBulletCoordinate(bullet);
                bullets.add(bullet);
                bulletSet.add(bullet);
            }

        } else if (!isPressedA) {
            flagA = false;
        }

        return bullets;
    }

    private void setBulletCoordinate(Bullet bullet) {
        final GOBoundingBox tankBox = tank.getComponent("BoundingBox");
        final GOBoundingBox bulletBox = bullet.getComponent("BoundingBox");
        bulletBox.x = tankBox.x + (tankBox.width - bulletBox.width >> 1);
        bulletBox.y = tankBox.y + (tankBox.height - bulletBox.height >> 1);
    }
}