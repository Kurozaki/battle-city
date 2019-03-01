package com.yotwei.battlecity.game.stage.scene;

import com.yotwei.battlecity.game.engine.GameContext;

import java.awt.*;

/**
 * Created by YotWei on 2019/2/27.
 */
public class LevelSwitcher extends AbstractScene {

    public LevelSwitcher(GameContext context) {
        super(context);
    }

    @Override
    public void resetScene() {
        getGameContext().switchLevel();
    }

    @Override
    public void updateScene() {

    }

    @Override
    public void drawScene(Graphics2D g) {

    }

    @Override
    public boolean isSceneFinished() {
        return true;
    }
}
