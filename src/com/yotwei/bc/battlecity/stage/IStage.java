package com.yotwei.bc.battlecity.stage;

import java.awt.*;

/**
 * Created by YotWei on 2018/6/12.
 */
public interface IStage {

    void process();

    void draw(Graphics2D g);

    boolean isActivate();
}
