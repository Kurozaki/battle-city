package com.yotwei.battlecity.game.beans;

import com.yotwei.battlecity.game.global.ResourcePool;
import com.yotwei.battlecity.util.Constant;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

/**
 * Created by YotWei on 2019/3/15.
 */
public class LevelBean {

    private static final int TILEMAP_WIDTH = 40;
    private static final int TILEMAP_HEIGHT = 30;

    private static final Logger logger = LoggerFactory.getLogger(LevelBean.class);

    private String caption;
    private BufferedImage background;
    private int[][] tilemap;

    private int enemyOnScreenMax;
    private int[] enemyCounts;

    private LevelBean() {
    }

    public String getCaption() {
        return caption;
    }

    public BufferedImage getBackground() {
        return background;
    }

    public int getEnemyOnScreenMax() {
        return enemyOnScreenMax;
    }

    public int[] getEnemyCounts() {
        return enemyCounts;
    }

    public int[][] getTilemap() {
        return tilemap;
    }

    public static LevelBean parseJson(JSONObject json) {
        LevelBean level = new LevelBean();

        // 通过 json 设置数据
        level.caption = json.getString("caption");

        // json 存储的是 bgp 图片的资源 id
        String bgpId = json.getString("bgp");
        level.background = ResourcePool.getImage(bgpId);

        // 设置地图
        level.tilemap = new int[TILEMAP_HEIGHT][TILEMAP_WIDTH];
        JSONArray tilemapJson = json.getJSONArray("tilemap");
        for (int i = 0; i < tilemapJson.length(); i++) {

            // 获取第 i 行
            JSONArray tilemapRowJson = tilemapJson.getJSONArray(i);

            for (int j = 0; j < tilemapRowJson.length(); j++) {
                int tileId = tilemapRowJson.getInt(j);

                // tileId 既是砖块 id
                level.tilemap[i][j] = tileId;
            }
        }

        level.enemyOnScreenMax = json.getJSONObject("enemy").getInt("on_screen_max");

        // 获取各种敌人的数量
        JSONArray enemyCountsJson = json.getJSONObject("enemy").getJSONArray("counts");
        level.enemyCounts = new int[enemyCountsJson.length()];
        for (int i = 0; i < enemyCountsJson.length(); i++) {
            int count = enemyCountsJson.getInt(i);
            level.enemyCounts[i] = count;
        }

        if (logger.isInfoEnabled()) {
            logger.info("Parsed level \"{}\"", level.caption);
        }

        return level;
    }
}
