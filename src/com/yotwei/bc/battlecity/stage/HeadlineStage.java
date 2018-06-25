package com.yotwei.bc.battlecity.stage;

import com.yotwei.bc.battlecity.core.BattleCityKeyboard;
import com.yotwei.bc.battlecity.core.Const;
import com.yotwei.bc.battlecity.level.lvl.LevelMonitor;
import com.yotwei.bc.battlecity.level.lvl.LevelsHolder;
import com.yotwei.bc.battlecity.others.Counter;
import com.yotwei.bc.battlecity.sprite.TextSprite;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by YotWei on 2018/6/12.
 */
public class HeadlineStage implements IStage {

    private Counter counter = new Counter();
    private TextSprite textSprite = TextSprite.NORMAL_TEXT;

    @Override
    public void process() {
        // update counter

        counter.update();

        // check key press
        if (BattleCityKeyboard.INSTANCE.isKeyPressing(KeyEvent.VK_SPACE))
            counter.start(5);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, Const.SIZE_VIEWPORT.width, Const.SIZE_VIEWPORT.height);

        textSprite.draw("Battle City", TextSprite.DRAW_ALIGN_CENTER_HORIZONTAL, 64, 128, g);
        textSprite.draw("Made by Yotwei", TextSprite.DRAW_ALIGN_CENTER, 0, 0, g);
    }

    @Override
    public boolean isActivate() {
        if (!counter.hasFinished())
            return true;
        else {
            StageHolder.INSTANCE.addStage(new LevelTransformStage());

            LevelsHolder.INSTANCE.reset();
            LevelMonitor.reset();

            return false;
        }
    }
}







