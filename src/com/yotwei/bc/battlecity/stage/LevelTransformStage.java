package com.yotwei.bc.battlecity.stage;

import com.yotwei.bc.battlecity.core.Const;
import com.yotwei.bc.battlecity.level.lvl.Level;
import com.yotwei.bc.battlecity.level.lvl.InitialParameters;
import com.yotwei.bc.battlecity.level.lvl.LevelReader;
import com.yotwei.bc.battlecity.level.lvl.LevelsHolder;
import com.yotwei.bc.battlecity.others.Counter;
import com.yotwei.bc.battlecity.sprite.TextSprite;

import java.awt.*;
import java.io.File;

/**
 * Created by YotWei on 2018/6/13.
 */
public class LevelTransformStage implements IStage {

    private static final int STAGE_WAITING_TIME = 120;

    private Counter counter = new Counter();

    private boolean finFlag = false;    // set to true if level loading has finished
    private String displayText = "";    // displaying text while waiting level loadingF

    public LevelTransformStage() {
        counter.start(STAGE_WAITING_TIME);

        // use new thread to load a level
        new Thread(() -> {
            File file = LevelsHolder.INSTANCE.next();
            if (file != null) {

                // read level from file
                LevelReader.LevelData levelData = LevelReader.INSTANCE.read(file);

                // get level and initial parameters instance
                Level level = levelData.getLevel();
                InitialParameters initParams = levelData.getInitialParameters();

                // add new stage
                StageHolder.INSTANCE.addStage(new LevelHandleStage(level, initParams));

                // get level name as display text
                displayText = initParams.getLevelName();
            } else {

                // no next level, skip to Game Over Stage
                StageHolder.INSTANCE.addStage(new GameOverStage());
            }
            finFlag = true;
        }).start();
    }

    @Override
    public void process() {
        counter.update();
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Const.SIZE_VIEWPORT.width, Const.SIZE_VIEWPORT.height);

        if (!displayText.isEmpty())
            TextSprite.NORMAL_TEXT.draw(displayText, TextSprite.DRAW_ALIGN_CENTER, 0, 0, g);
    }

    @Override
    public boolean isActivate() {
        return !finFlag || !counter.hasFinished();
    }

}
