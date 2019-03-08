package com.yotwei.battlecity.game.stage.scene;

import com.yotwei.battlecity.game.engine.GameContext;

import java.awt.*;

/**
 * Created by YotWei on 2019/2/27.
 */
public class LevelClearer extends AbstractScene {

    private int ticker;

    public LevelClearer(GameContext context) {
        super(context);
    }

    @Override
    public void resetScene() {
        ticker = 120;
    }

    @Override
    public void updateScene() {
        ticker--;
    }

    @Override
    public void drawScene(Graphics2D g) {

    }

    @Override
    public boolean isSceneFinished() {
        return ticker == 0;
    }
}
