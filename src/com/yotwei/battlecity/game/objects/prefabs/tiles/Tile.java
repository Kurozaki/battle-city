package com.yotwei.battlecity.game.objects.prefabs.tiles;

import com.yotwei.battlecity.game.LevelHandler;
import com.yotwei.battlecity.game.components.GOBoundingBox;
import com.yotwei.battlecity.game.components.GOSprite;
import com.yotwei.battlecity.game.objects.GameObject;
import com.yotwei.battlecity.util.Constant;

import java.awt.*;

/**
 * Created by YotWei on 2019/4/7.
 */
@SuppressWarnings("WeakerAccess")
public abstract class Tile extends GameObject {

    public static final int BLOCK_BRICK = 1;
    public static final int BLOCK_IRON = 2;
    public static final int TILE_GRASS = 3;
    public static final int TILE_RIVER = 4;

    private final int tileId;

    private GOSprite sprite;
    private GOBoundingBox boundBox;

    protected Tile(LevelHandler levelHandler, String tileTag, int tileId) {
        super(levelHandler, tileTag);
        this.tileId = tileId;

        Dimension blockSize = Constant.UNIT_SIZE;

        // 添加图形组件
        GOSprite sprite = new GOSprite();
        if (tileId > 0) {
            sprite.setImageResourceId("tile-" + tileId);
            sprite.setFrameSize(blockSize.width, blockSize.height);
        }
        addComponent(sprite);

        // 添加包围盒组件
        GOBoundingBox boundingBox = new GOBoundingBox();
        boundingBox.setSize(blockSize);
        addComponent(boundingBox);
    }

    @Override
    public void onActive() {
        sprite = getComponent("Sprite");
        boundBox = getComponent("BoundingBox");
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onDraw(Graphics2D g) {

        final Rectangle sr = sprite.getSrcRect();
        final Rectangle dr = boundBox;

        sprite.setFrameOffset(levelHandler.getFrameTicker() >> 3, 0);

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
    public void onInactive() {

    }

    public int getTileId() {
        return tileId;
    }

    public static Tile createInstance(LevelHandler levelHandler, int tileId, int x, int y) {

        // 校验 id 是否合法，不合法或 0 返回 null
        if (tileId == 0) {
            return null;
        }

        Tile tile;
        if (tileId == TILE_GRASS) {
            tile = new BGO(levelHandler, tileId);
        } else {
            tile = new Block(levelHandler, tileId);
        }

        // 组件已经在构造方法中添加
        GOBoundingBox box = tile.getComponent("BoundingBox");
        box.setLocation(x, y);

        tile.setActive(true);
        return tile;
    }

    public static Eagle createEagle(LevelHandler lh, int x, int y) {
        Eagle eagle = new Eagle(lh, "Eagle", -1, x, y);
        eagle.setActive(true);
        return eagle;
    }
}
