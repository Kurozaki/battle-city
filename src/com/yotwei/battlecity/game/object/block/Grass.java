package com.yotwei.battlecity.game.object.block;

import com.yotwei.battlecity.game.object.LevelContext;

/**
 * Created by YotWei on 2019/2/28.
 */
public class Grass extends AbstractBlock {

    protected Grass(LevelContext lvlCtx, int blockTypeId) {
        super(lvlCtx, blockTypeId);
    }

    @Override
    public int getDrawPriority() {
        return 2;
    }
}
