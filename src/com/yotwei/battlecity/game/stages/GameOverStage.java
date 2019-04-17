package com.yotwei.battlecity.game.stages;

import com.yotwei.battlecity.framework.IStageHandleContext;
import com.yotwei.battlecity.framework.stage.IStage;
import com.yotwei.battlecity.game.global.ResourcePool;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by YotWei on 2019/3/16.
 */
public class GameOverStage implements IStage {

    private int ticker;
    private BufferedImage image;

    @Override
    public void onInit(IStageHandleContext ctx) {
        image = ResourcePool.getImage("gameover");
        ticker = 120;
    }

    @Override
    public void update(IStageHandleContext ctx) {
        ticker--;
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(image, 0, 0, null);
    }

    @Override
    public void onFinished(IStageHandleContext ctx) {

    }

    @Override
    public IStage next() {
        return ticker == 0 ? new HeadlineStage() : this;
    }
}
