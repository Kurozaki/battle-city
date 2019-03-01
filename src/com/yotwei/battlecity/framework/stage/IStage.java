package com.yotwei.battlecity.framework.stage;

import com.yotwei.battlecity.framework.IStageHandleContext;

import java.awt.*;

/**
 * Created by YotWei on 2018/10/30.
 */
public interface IStage {

    void onInit(IStageHandleContext ctx);

    void update(IStageHandleContext ctx);

    void draw(Graphics2D g);

    void onFinished(IStageHandleContext ctx);

    IStage next();
}
