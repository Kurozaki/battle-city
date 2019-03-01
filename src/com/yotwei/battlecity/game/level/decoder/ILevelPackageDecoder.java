package com.yotwei.battlecity.game.level.decoder;

import com.yotwei.battlecity.game.level.LevelPackage;

import java.io.File;

/**
 * Created by YotWei on 2019/2/27.
 */
public interface ILevelPackageDecoder {

    static ILevelPackageDecoder getDecoder() {
        return new LevelPackageDecoderV1();
    }

    LevelPackage decode(File file);
}
