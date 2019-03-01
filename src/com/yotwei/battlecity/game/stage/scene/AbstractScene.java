package com.yotwei.battlecity.game.stage.scene;

import com.yotwei.battlecity.game.engine.GameContext;

import java.awt.*;

/**
 * Created by YotWei on 2019/2/26.
 */
public abstract class AbstractScene {

    private GameContext context;

    AbstractScene(GameContext context) {
        this.context = context;
    }

    protected GameContext getGameContext() {
        return context;
    }

    public abstract void resetScene();

    public abstract void updateScene();

    public abstract void drawScene(Graphics2D g);

    public abstract boolean isSceneFinished();
}
