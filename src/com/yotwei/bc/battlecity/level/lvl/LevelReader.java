package com.yotwei.bc.battlecity.level.lvl;

import com.yotwei.bc.battlecity.util.FileUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by YotWei on 2018/6/13.
 */
public enum LevelReader {

    INSTANCE;

    public LevelData read(File levelFile) {
        Level level = null;
        InitialParameters initParams = null;
        try {
            JSONObject levelJson = FileUtil.readJsonFile(levelFile);

            // read initial params
            initParams = InitialParameters.fromJson(levelJson.getJSONObject("initial"));

            // build level
            Level.Builder levelBuilder = new Level.Builder();

            // read obj
            JSONArray entitiesJson = levelJson.getJSONArray("entities");
            for (int rowIdx = 0; rowIdx < entitiesJson.length(); rowIdx++) {
                String[] split = entitiesJson.getString(rowIdx).split(",");
                for (int colIdx = 0; colIdx < split.length; colIdx++) {
                    int modelId = Integer.parseInt(split[colIdx], 16);
                    if (modelId == 0)
                        continue;
                    levelBuilder.addBlockEntity(modelId, colIdx, rowIdx);
                }
            }

            // build level
            level = levelBuilder.build();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return new LevelData(level, initParams);
    }

    public static class LevelData {

        private Level level;
        private InitialParameters initialParameters;

        public LevelData(Level level, InitialParameters initialParameters) {
            this.level = level;
            this.initialParameters = initialParameters;
        }

        public Level getLevel() {
            return level;
        }

        public InitialParameters getInitialParameters() {
            return initialParameters;
        }
    }
}

