package com.yotwei.battlecity.framework.stage;

import com.yotwei.battlecity.framework.IStageHandleContext;
import com.yotwei.battlecity.util.GraphicUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * Created by YotWei on 2018/10/30.
 * <p>
 * 这是一个引导启动类，onInit() 方法不会被调用
 */
public abstract class EntryStage implements IStage {

    private static final Logger logger = LoggerFactory.getLogger(EntryStage.class);

    private Dimension winSize;

    @Override
    public void onInit(IStageHandleContext ctx) {
        if (logger.isDebugEnabled()) {
            logger.debug("EntryStage onInit()");
        }
        winSize = ctx.getVar("_WinSize");
    }

    @Override
    public void update(IStageHandleContext ctx) {
        if (logger.isDebugEnabled()) {
            logger.debug("EntryStage update()");
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (logger.isDebugEnabled()) {
            logger.debug("EntryStage gfx()");
        }

        if (winSize != null) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, winSize.width, winSize.height);
        }
    }

    @Override
    public void onFinished(IStageHandleContext ctx) {
        if (logger.isDebugEnabled()) {
            logger.debug("EntryStage onFinished()");
        }
    }
}
