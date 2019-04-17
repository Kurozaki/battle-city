package com.yotwei.battlecity.game.components.block;

import com.yotwei.battlecity.game.components.listeners.BulletHitListener;
import com.yotwei.battlecity.game.objects.prefabs.bullet.Bullet;
import com.yotwei.battlecity.game.objects.prefabs.tiles.Block;

/**
 * Created by YotWei on 2019/4/9.
 */
public class Block01BulletHitListener extends BulletHitListener<Block> {

    private final Block block;
    private int blockDurability = 2;

    public Block01BulletHitListener(Block block) {
        super(block);
        this.block = block;
    }

    @Override
    public int onBulletHit(Bullet bullet) {
        int damage = Math.min(blockDurability, bullet.getPower());
        blockDurability -= damage;
        if (blockDurability == 0) {
            this.block.setActive(false);
        }
        return damage;
    }
}
