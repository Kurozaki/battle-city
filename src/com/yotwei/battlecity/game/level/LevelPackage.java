package com.yotwei.battlecity.game.level;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YotWei on 2019/2/27.
 */
public class LevelPackage {

    private Map<String, PlayerData> players;
    private Map<String, LevelData> levels;

    public LevelPackage() {
        players = new HashMap<>();
        levels = new HashMap<>();
    }

    public void addPlayer(PlayerData player) {
        players.put(player.name, player);
    }

    public void addLevel(LevelData level) {
        levels.put(level.caption, level);
    }

    public Map<String, PlayerData> getPlayers() {
        return players;
    }

    public Map<String, LevelData> getLevels() {
        return levels;
    }

    public static class PlayerData {

        private String name;
        private int lives;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public int getLives() {
            return lives;
        }

        public void setLives(int lives) {
            this.lives = lives;
        }
    }

    public static class LevelData {

        private String caption;
        private int[][] map;

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public String getCaption() {
            return caption;
        }

        public void setMap(int[][] map) {
            this.map = map;
        }

        public int[][] getMap() {
            return map;
        }
    }
}
