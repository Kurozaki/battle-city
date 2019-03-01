package com.yotwei.battlecity.game.stage;

import com.yotwei.battlecity.framework.IStageHandleContext;
import com.yotwei.battlecity.framework.KeyInput;
import com.yotwei.battlecity.framework.stage.IStage;
import com.yotwei.battlecity.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by YotWei on 2019/2/26.
 */
public class HeadlineStage
        implements IStage {

    private static final Logger logger = LoggerFactory.getLogger("Headline");

    private boolean confirm = false;

    @Override
    public void onInit(IStageHandleContext ctx) {

        if (logger.isDebugEnabled()) {
            logger.debug("{}.onInit()", getClass().getSimpleName());
        }
    }

    @Override
    public void update(IStageHandleContext ctx) {
        if (!confirm && KeyInput.isKeyPressed(KeyEvent.VK_ENTER)) {
            ctx.putVar("levelPackageName", "default");
            confirm = true;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Constant.WND_SIZE.width, Constant.WND_SIZE.height);
    }

    @Override
    public void onFinished(IStageHandleContext ctx) {
        if (logger.isDebugEnabled()) {
            logger.debug("onFinished()");
        }
    }

    @Override
    public IStage next() {
        if (!confirm)
            return this;
        return new GameStage();
    }
}
