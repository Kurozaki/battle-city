package com.yotwei.battlecity.game.stage;

import com.yotwei.battlecity.framework.IStageHandleContext;
import com.yotwei.battlecity.framework.stage.IStage;
import com.yotwei.battlecity.game.engine.ResourcePackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;

/**
 * Created by YotWei on 2019/2/28.
 * <p>
 * do something for game preparing
 * including loading image resources and so on
 */
public class PrepareStage implements IStage {

    private static final Logger logger = LoggerFactory.getLogger("PrepareStage");

    @Override
    public void onInit(IStageHandleContext ctx) {

        if (logger.isDebugEnabled()) {
            logger.debug("{}.onInit()", getClass().getSimpleName());
        }

        try {

            // init resource package
            ResourcePackage.init();

        } catch (IOException e) {

            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(IStageHandleContext ctx) {

    }

    @Override
    public void draw(Graphics2D g) {

    }

    @Override
    public void onFinished(IStageHandleContext ctx) {

    }

    @Override
    public IStage next() {
        return new HeadlineStage();
    }
}
