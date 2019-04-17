package com.yotwei.battlecity.game.stages;

import com.yotwei.battlecity.framework.IStageHandleContext;
import com.yotwei.battlecity.framework.stage.IStage;
import com.yotwei.battlecity.game.GameController;
import com.yotwei.battlecity.game.beans.LevelBean;
import com.yotwei.battlecity.game.ui.TextSprite;
import com.yotwei.battlecity.util.GraphicUtil;

import java.awt.*;

/**
 * Created by YotWei on 2019/3/15.
 */
public class LevelPreviewStage implements IStage {

    //
    // 在 onInit() 下通过 GameController 获取
    // 如果为值 true， 直接跳转至 GameOver 场景
    //
    private boolean isGameOver;

    //
    // 非 GameOver 状态下，展示关卡标题时间的计数器
    //
    private int captionDisplayTicker = 60;

    // 保存即将进行游戏的关卡
    private LevelBean levelBeanCurrent;

    private TextSprite textSprite;

    @Override
    public void onInit(IStageHandleContext ctx) {
        levelBeanCurrent = GameController.INST.getNextLevel();
        isGameOver = GameController.INST.isGameOver();

        if (!isGameOver && levelBeanCurrent != null) {
            //
            // 存入 Context
            // 让 LevelHandleStage 获取
            //
            ctx.putVar("_LevelBeanCurrent", levelBeanCurrent);

            // 初始化 textSprite
            textSprite = TextSprite.createOutlineTextSprite(
                    new Font("Consolas", Font.PLAIN, 36),
                    Color.WHITE,
                    Color.LIGHT_GRAY
            );
            textSprite.setText(levelBeanCurrent.getCaption());
            textSprite.setDrawCoordinate(TextSprite.DRAW_COORD_CENTER, TextSprite.DRAW_COORD_CENTER);
        }
    }

    @Override
    public void update(IStageHandleContext ctx) {
        captionDisplayTicker--;
    }

    @Override
    public void draw(Graphics2D g) {
        // 先清屏
        GraphicUtil.clearScreen(g, Color.BLACK);
        if (null != textSprite) textSprite.draw(g);
    }

    @Override
    public void onFinished(IStageHandleContext ctx) {

    }

    @Override
    public IStage next() {

        if (isGameOver) {
            // 游戏结束，跳转至 GameOverStage
            return new GameOverStage();
        } else {
            if (captionDisplayTicker <= 0) {
                return new LevelHandleStage();
            } else {
                return this;
            }
        }
    }
}
