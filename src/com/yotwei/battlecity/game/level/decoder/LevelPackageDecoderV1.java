package com.yotwei.battlecity.game.level.decoder;

import com.yotwei.battlecity.game.level.LevelPackage;
import com.yotwei.battlecity.util.FileUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by YotWei on 2019/2/27.
 */
public class LevelPackageDecoderV1 implements ILevelPackageDecoder {

    private static final Logger logger = LoggerFactory.getLogger("LevelPackageLoaderV1");

    private ILevelDecoder levelDecoder = ILevelDecoder.get();

    @Override
    public LevelPackage decode(File file) {

        LevelPackage lpkg = null;

        try {

            String lpkgPath = file.getName();

            if (logger.isDebugEnabled()) {
                logger.debug("decoding level package '{}'", lpkgPath);
            }

            // read json object from file
            JSONObject lpkgJson = FileUtil.createJsonObjectFromFile(file);

            // decode JSONObject into LevelPackage
            lpkg = new LevelPackage();

            //
            // get players init data
            // just one player now
            //
            JSONArray playersJsonArr = lpkgJson.getJSONArray("players");
            for (int i = 0; i < playersJsonArr.length(); i++) {

                // get player json data
                JSONObject playerJson = playersJsonArr.getJSONObject(i);

                // fill PlayerData instance with Json value
                LevelPackage.PlayerData playerDat = new LevelPackage.PlayerData();
                playerDat.setName(playerJson.getString("name"));
                playerDat.setLives(playerJson.getInt("lives"));

                lpkg.addPlayer(playerDat);
            }

            //
            // get levels data
            //
            JSONArray levelsJsonArr = lpkgJson.getJSONArray("levels");
            for (int i = 0; i < levelsJsonArr.length(); i++) {

                // get level json data
                JSONObject levelJson = levelsJsonArr.getJSONObject(i);

                // fill LevelData instance by using levelDecoder
                LevelPackage.LevelData levelDat = levelDecoder.decode(levelJson);

                lpkg.addLevel(levelDat);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("level package '{}' decoding finished", lpkgPath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lpkg;
    }
}
