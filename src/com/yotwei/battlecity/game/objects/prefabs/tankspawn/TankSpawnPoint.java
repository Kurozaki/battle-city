package com.yotwei.battlecity.game.objects.prefabs.tankspawn;

import com.yotwei.battlecity.game.LevelHandler;
import com.yotwei.battlecity.game.components.GOBoundingBox;
import com.yotwei.battlecity.game.components.GOSprite;
import com.yotwei.battlecity.game.objects.GameObject;
import com.yotwei.battlecity.game.objects.prefabs.tanks.Tank;
import com.yotwei.battlecity.util.Constant;
import com.yotwei.battlecity.util.GameObjects;

import java.awt.*;

/**
 * Created by YotWei on 2019/4/4.
 */
public class TankSpawnPoint<_TankType extends Tank> extends GameObject {

    private static final int SPAWN_TICK = 100;

    private int spawnTicker;

    private final _TankType tank;
    private final GOBoundingBox boundBox;
    private final GOSprite sprite;

    private TankSpawnPoint(LevelHandler levelHandler, _TankType tank) {
        super(levelHandler, "TankSpawnPoint");

        this.tank = tank;

        Dimension size = Constant.UNIT_SIZE_2X;

        boundBox = new GOBoundingBox();
        boundBox.setBounds(GameObjects.boundingBox(tank));
        addComponent(boundBox);

        sprite = new GOSprite();
        sprite.setImageResourceId("special_tank_spawn");
        sprite.setFrameSize(size.width, size.height);
        sprite.setDrawPriority(10);
        addComponent(sprite);
    }

    @Override
    public void onActive() {

    }

    @Override
    public void onUpdate() {
        spawnTicker++;
        if (spawnTicker > SPAWN_TICK) {
            if (levelHandler.retrieve("tanks", boundBox).isEmpty()) {
                levelHandler.addObject("tanks", tank);
                setActive(false);
            }
        }
    }

    @Override
    public void onInactive() {

    }

    @Override
    public void onDraw(Graphics2D g) {

        if (spawnTicker > 30) {
            sprite.setFrameOffset(spawnTicker >> 2, 0);

            final Rectangle dr = boundBox;
            final Rectangle sr = sprite.getSrcRect();

            g.drawImage(
                    sprite.getImage(),

                    dr.x,
                    dr.y,
                    dr.x + dr.width,
                    dr.y + dr.height,

                    sr.x,
                    sr.y,
                    sr.x + sr.width,
                    sr.y + sr.height,

                    null
            );
        }
    }

    public static <_TankType extends Tank> TankSpawnPoint<_TankType> createInstance(
            _TankType tank) {

        TankSpawnPoint<_TankType> spawnPoint = new TankSpawnPoint<>(tank.getLevelHandler(), tank);
        spawnPoint.setActive(true);
        return spawnPoint;
    }
}
