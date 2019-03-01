package com.yotwei.battlecity.game.stage.scene;

import com.yotwei.battlecity.game.engine.GameContext;
import com.yotwei.battlecity.util.GraphicUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * Created by YotWei on 2019/2/27.
 */
public class LevelPreviewer extends AbstractScene {

    private static final Logger logger = LoggerFactory.getLogger("LevelPreviewer");
    private static final int PREVIEW_TICK_COUNT = 120;

    /*
     * 1 tick = 1 frame
     * timerTicker decrease every frame from init value PREVIEW_TICK_COUNT
     * when decrease to 0, switch to next scene ( LevelHandler )
     */
    private int timerTicker;

    private String levelCaption;
    private Font levelCaptionFont = new Font("Consolas", Font.BOLD, 20);

    public LevelPreviewer(GameContext context) {
        super(context);
    }

    @Override
    public void resetScene() {
        timerTicker = PREVIEW_TICK_COUNT;
        levelCaption = getGameContext().getCurrentLevel().getCaption();
    }

    @Override
    public void updateScene() {
        if (timerTicker > 0) {
            timerTicker--;
        }
    }

    @Override
    public void drawScene(Graphics2D g) {
        GraphicUtil.clearScreen(g, Color.BLACK);

        GraphicUtil.drawCenterText(g, levelCaptionFont, Color.GRAY, levelCaption);
    }

    @Override
    public boolean isSceneFinished() {

        // when timerTicker decrease to 0, switch to next scene ( LevelHandler )
        return timerTicker <= 0;
    }
}
