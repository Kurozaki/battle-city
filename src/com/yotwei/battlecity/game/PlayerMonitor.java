package com.yotwei.battlecity.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * Created by YotWei on 2019/3/31.
 */
public class PlayerMonitor {

    private static final Logger logger = LoggerFactory.getLogger(PlayerMonitor.class);
    private static final PlayerMonitor _instance = new PlayerMonitor(3, new Point(224, 448));

    private int liveCount;
    public final Point spawnCoord;

    private PlayerMonitor(int liveCount, Point spawnCoord) {
        this.liveCount = liveCount;
        this.spawnCoord = spawnCoord;
    }


    public void incLive() {
        liveCount++;
        if (logger.isDebugEnabled()) {
            logger.debug("Player remain live: {}", liveCount);
        }
    }

    /**
     * 玩家死亡，扣除生命
     * 当生命为0时，宣告游戏结束
     */
    public void decLive() {
        liveCount--;
        if (logger.isDebugEnabled()) {
            logger.debug("Player remaining live: {}", liveCount);
        }
        if (liveCount == 0) {
            if (logger.isInfoEnabled()) {
                logger.info("Player has no remain live, game over");
            }
            GameController.INST.notifyGameOver();
        }
    }

    /**
     * 玩家剩余命数
     */
    public int getRemainLives() {
        return liveCount;
    }

    public static PlayerMonitor getInstance() {
        return _instance;
    }
}
