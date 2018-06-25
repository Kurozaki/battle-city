package com.yotwei.bc.battlecity.stage;

import com.yotwei.bc.battlecity.level.lvl.Level;
import com.yotwei.bc.battlecity.level.lvl.InitialParameters;
import com.yotwei.bc.battlecity.level.lvl.LevelMonitor;

import java.awt.*;

/**
 * Created by YotWei on 2018/6/13.
 */
public class LevelHandleStage implements IStage {

    private final LevelMonitor _levelMonitor;

    LevelHandleStage(Level level, InitialParameters initialParameters) {
        _levelMonitor = new LevelMonitor(level, initialParameters);
    }

    @Override
    public void process() {
        _levelMonitor.update();
    }

    @Override
    public void draw(Graphics2D g) {
        _levelMonitor.draw(g);
    }

    @Override
    public boolean isActivate() {
        return _levelMonitor.isActivate();
    }
}
