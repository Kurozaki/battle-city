package com.yotwei.bc.battlecity.level.obj;

import com.yotwei.bc.battlecity.level.lvl.Level;

import java.awt.*;

/**
 * Created by YotWei on 2018/6/14.
 */
public abstract class AbstractObject extends Rectangle {

    private final Level levelContext;

    protected AbstractObject(Level level) {
        this.levelContext = level;
    }

    public abstract void draw(Graphics2D g);

    public abstract void update();

    public Level getLevel() {
        return levelContext;
    }
}
