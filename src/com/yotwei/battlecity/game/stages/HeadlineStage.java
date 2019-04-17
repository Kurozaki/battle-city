package com.yotwei.battlecity.game.stages;

import com.yotwei.battlecity.framework.IStageHandleContext;
import com.yotwei.battlecity.framework.KeyInput;
import com.yotwei.battlecity.framework.stage.IStage;
import com.yotwei.battlecity.game.global.ResourcePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * Created by YotWei on 2019/3/15.
 */
public class HeadlineStage implements IStage {

    private static final int FADE_OUT_TICK = 30;
    private static final Logger logger = LoggerFactory.getLogger(HeadlineStage.class);

    private BufferedImage headlineImage;
    private AlphaComposite alphaComposite = AlphaComposite.SrcIn;

    // 渐出场景的计数器
    private int fadeOutTicker;
    private boolean isFadingOut;    //按下回车会触发

    @Override
    public void onInit(IStageHandleContext ctx) {
        headlineImage = ResourcePool.getImage("headline");
        if (logger.isDebugEnabled()) {
            logger.debug("HeadlineStage onInit()");
        }
    }

    @Override
    public void update(IStageHandleContext ctx) {
        if (!isFadingOut && KeyInput.isKeyPressed(KeyEvent.VK_ENTER)) {
            isFadingOut = true;
            fadeOutTicker = FADE_OUT_TICK;
        }

        if (isFadingOut) {
            fadeOutTicker--;
        }
    }

    @Override
    public void draw(Graphics2D g) {

        // 计算并设置背景的 alpha
        float alphaValue = isFadingOut ?
                (float) fadeOutTicker / FADE_OUT_TICK :
                1.0f;
        alphaComposite = alphaComposite.derive(alphaValue);
        g.setComposite(alphaComposite);

        // 绘制背景
        g.drawImage(headlineImage, 0, 0, null);
    }

    @Override
    public void onFinished(IStageHandleContext ctx) {
        if (logger.isDebugEnabled()) {
            logger.debug("HeadlineStage onFinished()");
        }
        ctx.putVar("_LevelDir", "default");
    }

    @Override
    public IStage next() {
        boolean isFadeOutFinished = isFadingOut && fadeOutTicker <= 0;
        return isFadeOutFinished ? new GamePrepareStage() : this;
    }
}
