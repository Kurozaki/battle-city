package com.yotwei.battlecity.game.engine;

import com.yotwei.battlecity.game.level.LevelPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by YotWei on 2019/2/27.
 */
public abstract class GameContext {

    private static final Logger logger = LoggerFactory.getLogger("GameContext");

    private LevelPackage levelPackage;
    private Iterator<Map.Entry<String, LevelPackage.LevelData>> levelIterator;
    private LevelPackage.LevelData levelDataCurrent;

    protected void setLevelPackage(LevelPackage levelPackage) {
        this.levelPackage = levelPackage;
        this.levelIterator = levelPackage.getLevels().entrySet().iterator();
    }

    /**
     * to switch levelDataCurrent instance
     */
    public void switchLevel() {

        // get next level by using levelIterator
        Map.Entry<String, LevelPackage.LevelData> entry = levelIterator.next();

        if (logger.isDebugEnabled()) {
            logger.debug("switch to level '{}'", entry.getKey());
        }

        // set to current level
        levelDataCurrent = entry.getValue();
    }

    public LevelPackage.LevelData getCurrentLevel() {
        return levelDataCurrent;
    }
}
