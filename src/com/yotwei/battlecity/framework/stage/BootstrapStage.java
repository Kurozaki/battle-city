package com.yotwei.battlecity.framework.stage;

import com.yotwei.battlecity.framework.IStageHandleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * Created by YotWei on 2018/10/30.
 */
public abstract class BootstrapStage implements IStage {

    private static final Logger logger = LoggerFactory.getLogger(BootstrapStage.class);

    @Override
    public void onInit(IStageHandleContext ctx) {
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
}
