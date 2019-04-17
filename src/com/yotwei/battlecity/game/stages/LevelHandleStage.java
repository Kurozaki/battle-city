package com.yotwei.battlecity.game.stages;

import com.yotwei.battlecity.framework.IStageHandleContext;
import com.yotwei.battlecity.framework.stage.IStage;
import com.yotwei.battlecity.game.LevelHandler;
import com.yotwei.battlecity.game.beans.LevelBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * Created by YotWei on 2019/3/16.
 */
public class LevelHandleStage extends LevelHandler
        implements IStage {

    private static final Logger logger = LoggerFactory.getLogger(LevelHandleStage.class);

    /**
     * 关卡完成后的延时跳转
     */
    private int handleFinishedTicker = 120;

    @Override
    public void onInit(IStageHandleContext ctx) {
        LevelBean levelBean = ctx.getVar("_LevelBeanCurrent");
        initLevel(levelBean);
    }

    @Override
    public void update(IStageHandleContext ctx) {
        updateFrameTicker();

        updateGameObjects();
        handleObjectCollision();
        clearObjectBufferQueue();

        checkEagle();

        // 检查关卡状态
        if (levelState != LState.NORMAL) {
            handleFinishedTicker--;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        drawBackground(g);
        drawGameObjects(g);

        drawUIs(g);
    }

    @Override
    public void onFinished(IStageHandleContext ctx) {

    }

    @Override
    public IStage next() {
        //
        // 关卡处于非 NORMAL 状态，
        // 并且延时跳转计数器倒数至 0
        // 那么跳转成立
        //
        boolean isSwitch = levelState != LState.NORMAL && handleFinishedTicker <= 0;

        return isSwitch ? new LevelPreviewStage() : this;
    }
}
