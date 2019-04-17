package com.yotwei.battlecity.game.objects.prefabs;

import com.yotwei.battlecity.game.LevelHandler;
import com.yotwei.battlecity.game.components.GOBoundingBox;
import com.yotwei.battlecity.game.components.GOSprite;
import com.yotwei.battlecity.game.components.item.*;
import com.yotwei.battlecity.game.objects.GameObject;
import com.yotwei.battlecity.util.Constant;
import com.yotwei.battlecity.util.GameObjects;

import java.awt.*;
import java.util.Random;

/**
 * Created by YotWei on 2019/4/10.
 */
@SuppressWarnings("WeakerAccess")
public class Item extends GameObject {

    public static final int ITEM_ALIVE_TICK = 12 * 60;

    public static final int ADD_LIVE = 1;
    public static final int ADD_BULLET_SLOT = 2;    // 增加子弹发射上限的道具
    public static final int CLEAR_ENEMIES = 3;
    public static final int FREEZE_ENEMIES = 4;
    public static final int GUARD = 5;  // 坦克保护罩
    public static final int GUARD_EAGLE = 6; // 基地保护
    public static final int SPEED_UP = 7;

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final int typeId;

    private int ticker = ITEM_ALIVE_TICK;   // 道具的存活时间

    private Item(LevelHandler levelHandler, int typeId) {
        super(levelHandler, "Item");
        this.typeId = typeId;

        Rectangle itemBox = new Rectangle(Constant.UNIT_SIZE_2X);

        GOSprite sprite = new GOSprite();
        sprite.setImageResourceId("item-" + typeId);
        sprite.setFrameSize(itemBox.width, itemBox.height);
        sprite.setDrawPriority(6);
        addComponent(sprite);

        GOBoundingBox boundingBox = new GOBoundingBox();
        boundingBox.setSize(itemBox.getSize());
        addComponent(boundingBox);
    }

    @Override
    public void onActive() {

    }

    @Override
    public void onUpdate() {
        if (--ticker <= 0) {
            setActive(false);
        }
    }

    @Override
    public void onDraw(Graphics2D g) {
        final GOSprite sprite = getComponent("Sprite");

        final Rectangle sr = sprite.getSrcRect();
        final Rectangle dr = GameObjects.boundingBox(this);

        if (ticker < 210) {
            float alpha = Math.abs((ticker + 30) % 60 - 30) * 1.0f / 30;
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
        }

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

        // 恢复原来的 Alpha
        g.setComposite(AlphaComposite.SrcAtop);
    }

    @Override
    public void onInactive() {

    }

    public static Item createInstance(LevelHandler levelHandler, int typeId, int x, int y) {
        Item item = new Item(levelHandler, typeId);

        //
        // 根据道具的 id 选择不同的碰撞器
        //
        switch (typeId) {
            case ADD_LIVE:
                item.addListener(new ItemAddLiveCollisionListener(item));
                break;
            case ADD_BULLET_SLOT:
                item.addListener(new ItemAddBulletSlotCollisionListener(item));
                break;
            case CLEAR_ENEMIES:
                item.addListener(new ItemClearEnemyCollisionListener(item));
                break;
            case FREEZE_ENEMIES:
                item.addListener(new ItemFreezeCollisionListener(item));
                break;
            case GUARD:
                item.addListener(new ItemGuardCollisionListener(item));
                break;
            case GUARD_EAGLE:
                item.addListener(new ItemGuardEagleCollisionListener(item));
                break;
            case SPEED_UP:
                item.addListener(new ItemSpeedUpCollisionListener(item));
                break;
            default:
                throw new RuntimeException("Invalid item id:" + typeId);
        }

        GameObjects.boundingBox(item).setLocation(x, y);
        item.setActive(true);

        return item;
    }

    public static Item randomInstance(LevelHandler lh, int x, int y) {
        final int totalItem = 7;
        final Random rand = new Random();

        return createInstance(lh, rand.nextInt(totalItem) + 1, x, y);
    }
}
