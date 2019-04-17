package com.yotwei.battlecity.game.components.block;

import com.yotwei.battlecity.game.components.listeners.BulletHitListener;
import com.yotwei.battlecity.game.objects.prefabs.bullet.Bullet;
import com.yotwei.battlecity.game.objects.prefabs.tiles.Block;

/**
 * Created by YotWei on 2019/4/9.
 */
public class Block02BulletHitListener extends BulletHitListener<Block> {

    private final Block block;

    @SuppressWarnings("FieldCanBeLocal")
    private final int blockDurability = 2;

    public Block02BulletHitListener(Block block) {
        super(block);
        this.block = block;
    }

    @Override
    public int onBulletHit(Bullet bullet) {
        if (bullet.getPower() < blockDurability) {
            return bullet.getPower();
        }
        block.setActive(false);
        return blockDurability;
    }
}
