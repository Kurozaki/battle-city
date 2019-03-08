package com.yotwei.battlecity.game.stage;

import com.yotwei.battlecity.framework.IStageHandleContext;
import com.yotwei.battlecity.framework.stage.IStage;
import com.yotwei.battlecity.util.GraphicUtil;

import java.awt.*;

/**
 * Created by YotWei on 2019/3/8.
 */
public class GameOverStage implements IStage {

    private int ticker = 120;

    private Font font = new Font("Consolas", Font.BOLD, 36);

    @Override
    public void onInit(IStageHandleContext ctx) {

    }

    @Override
    public void update(IStageHandleContext ctx) {
        ticker--;
    }

    @Override
    public void draw(Graphics2D g) {
        GraphicUtil.clearScreen(g, Color.BLACK);
        GraphicUtil.drawCenterText(g, font, Color.WHITE, "Game Over");
    }

    @Override
    public void onFinished(IStageHandleContext ctx) {

    }

    @Override
    public IStage next() {
        return ticker > 0 ? this : new HeadlineStage();
    }
}
