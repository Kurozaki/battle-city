package com.yotwei.battlecity.game.engine;

import com.yotwei.battlecity.game.level.LevelPackage;
import com.yotwei.battlecity.game.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by YotWei on 2019/2/27.
 */
public abstract class GameContext {

    private static final Logger logger = LoggerFactory.getLogger("GameContext");

    private Iterator<Map.Entry<String, LevelPackage.LevelData>> levelIterator;

    private LevelPackage.LevelData levelDataCurrent;
    private Map<String, Player> players;

    private boolean isGameOver;

    protected void setLevelPackage(LevelPackage levelPackage) {

        // assigned levelIterator
        levelIterator = levelPackage.getLevels().entrySet().iterator();

        //
        // create players
        //
        players = new HashMap<>();
        for (LevelPackage.PlayerData playerData : levelPackage.getPlayers().values()) {
            Player player = new
                    Player(playerData.getName(), playerData.getLives());
            players.put(playerData.getName(), player);

            if (logger.isDebugEnabled()) {
                logger.debug("create player '{}'", playerData.getName());
            }
        }
    }


    /**
     * to switch levelDataCurrent instance
     */
    public void switchLevel() {

        if (levelIterator.hasNext()) {

            // get next level by using levelIterator
            Map.Entry<String, LevelPackage.LevelData> entry = levelIterator.next();

            if (logger.isDebugEnabled()) {
                logger.debug("switch to level '{}'", entry.getKey());
            }

            // set to current level
            levelDataCurrent = entry.getValue();
        } else {

            if (logger.isDebugEnabled()) {
                logger.debug("all level finished.");
            }

            //
            // no no more level
            // set status to game over
            //
            notifyGameOver();
        }
    }

    public LevelPackage.LevelData getCurrentLevel() {
        return levelDataCurrent;
    }

    public Player getPlayer(String name) {
        return players.get(name);
    }

    public void notifyGameOver() {
        isGameOver = true;
    }

    public boolean isGameOver() {
        return isGameOver;
    }
}
