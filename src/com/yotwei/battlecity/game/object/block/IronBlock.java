package com.yotwei.battlecity.game.object.block;

import com.yotwei.battlecity.game.object.LevelContext;

/**
 * Created by YotWei on 2019/2/28.
 */
public class IronBlock extends AbstractBlock {

    private int durability = 400;

    protected IronBlock(LevelContext lvlCtx, int typeId) {
        super(lvlCtx, typeId);
    }

    @Override
    public int tryDamage(int damageValue) {
        if (damageValue < durability) {
            return damageValue;
        } else {
            setActive(false);
            return durability;
        }
    }
}
