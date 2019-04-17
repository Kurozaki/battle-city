package com.yotwei.battlecity.game.objects.prefabs.tiles;

import com.yotwei.battlecity.game.LevelHandler;


/**
 * Created by YotWei on 2019/4/7.
 * <p>
 * BGO = Background Object
 */
@SuppressWarnings("WeakerAccess")
public class BGO extends Tile {

    BGO(LevelHandler levelHandler, int tileId) {
        super(levelHandler, "Decorate", tileId);
    }
}
