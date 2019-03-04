package com.yotwei.battlecity.game.level.decoder;

import com.yotwei.battlecity.game.level.LevelPackage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by YotWei on 2019/2/27.
 */
public class LevelDecoderV1 implements ILevelDecoder {

    private static final Logger logger = LoggerFactory.getLogger("LevelDecoderV1");

    @Override
    public LevelPackage.LevelData decode(JSONObject json) {
        LevelPackage.LevelData levelDat = new LevelPackage.LevelData();

        if (logger.isDebugEnabled()) {
            logger.debug("level decoding: '{}'", levelDat.getCaption());
        }

        // get level caption
        levelDat.setCaption(json.getString("caption"));


        // get level background picture resource name
        levelDat.setBgpName(json.getString("bg_pic"));


        // get eagle coordinate
        levelDat.setEagleCoord(new Point(
                json.getJSONObject("eagle").getInt("x"),
                json.getJSONObject("eagle").getInt("y")));


        // get enemy max on screen count in level
        levelDat.setEnemyOnScreenCountMax(json.getInt("enemy_on_screen"));


        //
        // get enemy type counts of level
        //
        Map<Integer, Integer> enemyTypeCounts = new HashMap<>();
        JSONArray enemyCountsJson = json.getJSONArray("enemy_counts");

        for (int i = 0; i < enemyCountsJson.length(); i++) {

            int typeId = enemyCountsJson.getJSONObject(i).getInt("type");
            int typeCount = enemyCountsJson.getJSONObject(i).getInt("count");

            enemyTypeCounts.put(typeId, typeCount);
        }
        levelDat.setEnemyTypesCount(enemyTypeCounts);


        //
        // get map info
        // set them into a 2-dimension array
        //
        int[][] map;
        JSONArray mapRows = json.getJSONArray("map");
        map = new int[mapRows.length()][];

        for (int i = 0; i < mapRows.length(); i++) {    // the first dimension of the map

            String[] mapRow = mapRows.getString(i).split(",");
            map[i] = new int[mapRow.length];

            for (int j = 0; j < mapRow.length; j++) {   // the second dimension of the map

                // put block id into 2-dimension array
                map[i][j] = Integer.parseInt(mapRow[j]);
            }
        }
        levelDat.setMap(map);

        //
        // get players' start coordinate
        //
        JSONObject playersJson = json.getJSONObject("players");
        //
        // "players" : {
        //      "[PLAYER_NAME]": {
        //          "x": [X_VALUE],
        //          "y": [Y_VALUE]
        //      },
        //      ....
        // }
        //
        Map<String, Point> playersStartCoord = new HashMap<>();

        for (String name : playersJson.keySet()) {
            JSONObject playerJson = playersJson.getJSONObject(name);
            playersStartCoord.put(name,
                    new Point(playerJson.getInt("x"), playerJson.getInt("y")));
        }
        levelDat.setPlayersStartCoord(playersStartCoord);

        if (logger.isDebugEnabled()) {
            logger.debug("level decode finished: '{}'", levelDat.getCaption());
        }

        return levelDat;
    }
}
