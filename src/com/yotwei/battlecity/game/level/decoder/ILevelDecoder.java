package com.yotwei.battlecity.game.level.decoder;

import com.yotwei.battlecity.game.level.LevelPackage;
import org.json.JSONObject;

/**
 * Created by YotWei on 2019/2/27.
 */
public interface ILevelDecoder {

    static ILevelDecoder get() {
        return new LevelDecoderV1();
    }

    LevelPackage.LevelData decode(JSONObject levelJson);
}
