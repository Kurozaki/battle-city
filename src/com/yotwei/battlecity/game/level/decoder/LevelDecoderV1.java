package com.yotwei.battlecity.game.level.decoder;

import com.yotwei.battlecity.game.level.LevelPackage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by YotWei on 2019/2/27.
 */
public class LevelDecoderV1 implements ILevelDecoder {

    private static final Logger logger = LoggerFactory.getLogger("LevelDecoderV1");

    @Override
    public LevelPackage.LevelData decode(JSONObject json) {
        LevelPackage.LevelData levelDat = new LevelPackage.LevelData();

        // get level caption
        levelDat.setCaption(json.getString("caption"));

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

            for (int j = 0; j < mapRow.length; j++) {  // the second dimension of the map

                // put block id into 2-dimension array
                map[i][j] = Integer.parseInt(mapRow[j]);
            }
        }
        levelDat.setMap(map);

        return levelDat;
    }
}
