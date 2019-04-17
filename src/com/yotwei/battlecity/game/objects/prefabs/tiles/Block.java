package com.yotwei.battlecity.game.objects.prefabs.tiles;

import com.yotwei.battlecity.game.LevelHandler;
import com.yotwei.battlecity.game.components.block.Block01BulletHitListener;
import com.yotwei.battlecity.game.components.block.Block02BulletHitListener;
import com.yotwei.battlecity.game.components.listeners.CollisionListener;
import com.yotwei.battlecity.game.objects.GameObject;

import java.awt.*;

/**
 * Created by YotWei on 2019/3/16.
 */
public class Block extends Tile {

    Block(LevelHandler levelHandler, int blockId) {
        super(levelHandler, "Block", blockId);

        // 砖块有碰撞监听器
        addListener(new BlockCollisionListener(this));

        // 根据 id 选择不同的炮弹碰撞监听器
        switch (blockId) {
            case BLOCK_BRICK: // 普通红砖
                addListener(new Block01BulletHitListener(this));
                break;
            case BLOCK_IRON: // 坚硬砖块
                addListener(new Block02BulletHitListener(this));
                break;
            case TILE_RIVER: // 河流
                // 河流不需要添加子弹碰撞监听器
                break;
            default:
                throw new IllegalArgumentException("block (id=" + blockId + ") doesn't exist");
        }
    }

    private static class BlockCollisionListener extends CollisionListener<Block> {

        private BlockCollisionListener(Block gameObject) {
            super(gameObject);
        }

        @Override
        public void onTouchBound(Rectangle boundary) {

        }

        @Override
        public void onCollide(GameObject anotherObject) {

        }
    }
}
