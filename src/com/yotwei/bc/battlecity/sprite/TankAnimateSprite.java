package com.yotwei.bc.battlecity.sprite;

import com.yotwei.bc.battlecity.factory.GraphicsFactory;
import com.yotwei.bc.battlecity.level.obj.tank.AbstractTank;
import com.yotwei.bc.battlecity.level.obj.tank.EnemyTank;
import com.yotwei.bc.battlecity.level.obj.tank.PlayerTank;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by YotWei on 2018/6/18.
 */
public class TankAnimateSprite extends AnimateSprite {

    private final AbstractTank attachTank;
    private int counter = 0;

    public TankAnimateSprite(AbstractTank attachTank) {
        super(getTankResource(attachTank));
        this.attachTank = attachTank;
    }

    @Override
    public void drawNextFrame(Graphics2D g) {
        int offsetX = attachTank.getDirection().ordinal() * (attachTank.width << 1);
        offsetX += (counter++ / 8) % 2 * attachTank.width;
        g.drawImage(imgSrc.getSubimage(offsetX, 0, attachTank.width, attachTank.height),
                attachTank.x,
                attachTank.y,
                null);
    }

    private static BufferedImage getTankResource(AbstractTank tank) {
        if (tank instanceof PlayerTank)
            return GraphicsFactory.INSTANCE.getResourceById("player-1");
        if (tank instanceof EnemyTank)
            return GraphicsFactory.INSTANCE.getResourceById("enemy-" + tank.getModelId());
        return null;
    }
}
