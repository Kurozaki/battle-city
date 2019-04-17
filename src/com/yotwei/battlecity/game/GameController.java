package com.yotwei.battlecity.game;

import com.yotwei.battlecity.game.beans.LevelBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by YotWei on 2019/3/15.
 */
public enum GameController {

    INST;

    // --------------------------------------------------------
    //
    // 游戏状态管理用
    //
    // --------------------------------------------------------

    private Iterator<LevelBean> levelBeanIter;

    private boolean isGameOver;


    // --------------------------------------------------------
    //
    // 一些改变游戏状态的重要方法
    //
    // --------------------------------------------------------

    /**
     * 重置游戏状态
     * <p>
     * 保证只在 {@link com.yotwei.battlecity.game.stages.GamePrepareStage} 的 onInit() 下调用
     */
    public void reset(LevelBean[] levels) {
        List<LevelBean> levelBeans = new ArrayList<>(Arrays.asList(levels));
        levelBeanIter = levelBeans.iterator();

        isGameOver = false;
    }

    /**
     * 获取下一个关卡
     * 如果没有下一个关卡，说明已经通关，返回 null，同时设置游戏状态为通关
     * <p>
     * 该方法保证只在 {@link com.yotwei.battlecity.game.stages.LevelPreviewStage} 中调用
     */
    public LevelBean getNextLevel() {
        if (levelBeanIter.hasNext()) {
            // 下一个关卡
            return levelBeanIter.next();
        } else {
            // 没有下一个关卡了，游戏完成
            isGameOver = true;
            return null;
        }
    }

    /**
     * 强制游戏结束
     * 目前仅在玩家死亡时调用 ({@link PlayerMonitor} 的 playerDeath() 方法)
     */
    public void notifyGameOver() {
        isGameOver = true;
    }

    /**
     * 告诉场景是否已经游戏结束
     * <p>
     * 方法保证只在 {@link com.yotwei.battlecity.game.stages.LevelPreviewStage} 中调用
     */
    public boolean isGameOver() {
        return isGameOver;
    }

}
