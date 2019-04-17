package com.yotwei.battlecity.game.objects.prefabs.effects;

import com.yotwei.battlecity.game.LevelHandler;
import com.yotwei.battlecity.game.components.GOBoundingBox;
import com.yotwei.battlecity.game.components.GOSprite;
import com.yotwei.battlecity.game.objects.GameObject;

import java.awt.*;

/**
 * Created by YotWei on 2019/4/8.
 */
public class Effect extends GameObject {

    public static final int ID_BOOM_SMALL = 1;
    public static final int ID_BOOM = 2;
    public static final int ID_BURST = 3;

    private final int typeId;
    private final int frameSpeed;
    private final int liveTick;

    private int ticker;

    private Effect(LevelHandler levelHandler, int typeId) {
        this(levelHandler, typeId, 4);
    }

    private Effect(LevelHandler levelHandler, int typeId, int frameSpeed) {
        super(levelHandler, "Effect");

        this.typeId = typeId;
        this.frameSpeed = frameSpeed;

        GOSprite sprite = new GOSprite();
        sprite.setImageResourceId("effect-" + typeId);

        // 素材按帧横向排列，并且保证是正方形
        // 因此原素材的高度就是每一帧的边长
        final int frameSide = sprite.getImage().getHeight();

        // 设置帧尺寸
        sprite.setFrameSize(frameSide, frameSide);
        addComponent(sprite);

        GOBoundingBox box = new GOBoundingBox();
        box.setSize(frameSide, frameSide);
        addComponent(box);

        liveTick = sprite.getImage().getWidth() * frameSpeed / frameSide;
    }

    @Override
    public void onActive() {

    }

    @Override
    public void onUpdate() {
        if (++ticker >= liveTick) {
            setActive(false);
        }
    }

    @Override
    public void onDraw(Graphics2D g) {

        final GOSprite sprite = getComponent("Sprite");
        sprite.setFrameOffset(ticker / frameSpeed, 0);

        final Rectangle sr = sprite.getSrcRect();
        final Rectangle dr = getComponent("BoundingBox");

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

    public static Effect createInstance(
            LevelHandler levelHandler,
            int id,
            int x,
            int y) {

        Effect e = new Effect(levelHandler, id);

        GOBoundingBox effectBox = e.getComponent("BoundingBox");
        effectBox.setLocation(x, y);

        e.setActive(true);
        return e;
    }
}
