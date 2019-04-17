package com.yotwei.battlecity.game.objects.prefabs.tiles;

import com.yotwei.battlecity.game.LevelHandler;
import com.yotwei.battlecity.game.components.GOBoundingBox;
import com.yotwei.battlecity.game.components.GOSprite;
import com.yotwei.battlecity.game.components.listeners.BulletHitListener;
import com.yotwei.battlecity.game.objects.GameObject;
import com.yotwei.battlecity.game.objects.prefabs.bullet.Bullet;
import com.yotwei.battlecity.util.Constant;
import com.yotwei.battlecity.util.GameObjects;

import java.awt.*;

/**
 * Created by YotWei on 2019/4/12.
 */
public class Eagle extends Tile {

    private static final int GUARD_TICK = 15 * 60;

    private boolean isDestroyed = false;

    /**
     * 周围保护砖块（被强化后）的有效时间
     */
    private int guardTicker;

    Eagle(LevelHandler levelHandler, String tileTag, int tileId, int x, int y) {
        super(levelHandler, tileTag, tileId);

        Dimension eagleSize = Constant.UNIT_SIZE_2X;

        GOSprite sprite = getComponent("Sprite");
        sprite.setImageResourceId("special_eagle");
        sprite.setFrameSize(eagleSize.width, eagleSize.height);

        GOBoundingBox boundBox = getComponent("BoundingBox");
        boundBox.setSize(eagleSize.width, eagleSize.height);
        boundBox.setLocation(x, y);

        addListener(new EagleBulletHitListener(this));
//        addListener(new CollisionListener<Eagle>(this) {
//            @Override
//            public void onTouchBound(Rectangle boundary) {
//
//            }
//
//            @Override
//            public void onCollide(GameObject anotherObject) {
//
//            }
//        });
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    @Override
    public void onDraw(Graphics2D g) {
        final GOSprite sprite = getComponent("Sprite");
        sprite.setFrameOffset(isDestroyed ? 1 : 0, 0);

        final Rectangle dr = GameObjects.boundingBox(this);
        final Rectangle sr = sprite.getSrcRect();

        g.drawImage(
                sprite.getImage(),

                dr.x,
                dr.y,
                dr.x + dr.width,
                dr.y + dr.height,

                sr.x,
                sr.y,
                sr.x + sr.width,
                sr.y + sr.height,

                null
        );
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (guardTicker > 0) {
            guardTicker--;
            if (guardTicker == 0) {
                // 恢复周围的砖块
                changeBlockAround(Tile.BLOCK_BRICK);
            }
        }
    }

    private void changeBlockAround(int blockId) {
        Dimension offset = Constant.UNIT_SIZE;
        final Rectangle retArea = new Rectangle(GameObjects.boundingBox(this));

        retArea.x -= offset.width;
        retArea.y -= offset.height;
        retArea.width = retArea.width + offset.width * 2;
        retArea.height = retArea.height + offset.height * 2;

        for (GameObject block : levelHandler.retrieve("tiles", retArea)) {
            if (!block.getTag().equals("Block")) {
                continue;
            }

            // 替换原来的砖块
            Point p = GameObjects.boundingBox(block).getLocation();
            levelHandler.addObject(
                    "tiles",
                    Tile.createInstance(levelHandler, blockId, p.x, p.y)
            );
            block.setActive(false);
        }
    }

    /**
     * 强化周围的砖块
     */
    public void strengthenAround() {
        guardTicker = GUARD_TICK;

        // 强化周围的砖块
        changeBlockAround(Tile.BLOCK_IRON);
    }

    private static class EagleBulletHitListener extends BulletHitListener<Eagle> {

        private final Eagle eagle;

        EagleBulletHitListener(Eagle gameObject) {
            super(gameObject);
            this.eagle = gameObject;
        }

        @Override
        public int onBulletHit(Bullet bullet) {
            if (!eagle.isDestroyed) {
                eagle.isDestroyed = true;
            }
            return bullet.getPower();
        }
    }
}
