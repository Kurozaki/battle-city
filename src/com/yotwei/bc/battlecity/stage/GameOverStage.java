package com.yotwei.bc.battlecity.stage;

import com.yotwei.bc.battlecity.core.Const;
import com.yotwei.bc.battlecity.others.Counter;
import com.yotwei.bc.battlecity.sprite.TextSprite;

import java.awt.*;

/**
 * Created by YotWei on 2018/6/14.
 */
public class GameOverStage implements IStage {

    private static final int WAITING_TIME = 60 * 5;    // 5s

    private Counter counter = new Counter();

    public GameOverStage() {
        counter.start(WAITING_TIME);
    }

    @Override
    public void process() {
        counter.update();
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Const.SIZE_VIEWPORT.width, Const.SIZE_VIEWPORT.height);

        TextSprite.NORMAL_TEXT.draw("Thank you", TextSprite.DRAW_ALIGN_CENTER, 0, 32, g);
    }

    @Override
    public boolean isActivate() {
        if (counter.hasFinished()) {
            StageHolder.INSTANCE.addStage(new HeadlineStage());
            return false;
        } else {
            return true;
        }
    }
}
