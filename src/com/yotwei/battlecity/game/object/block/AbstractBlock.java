package com.yotwei.battlecity.game.object.block;

import com.yotwei.battlecity.game.engine.ResourcePackage;
import com.yotwei.battlecity.game.object.GameObject;
import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.Physic;
import com.yotwei.battlecity.util.Constant;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by YotWei on 2019/2/25.
 */
public abstract class AbstractBlock extends GameObject
        implements Physic.CollideAble<Rectangle> {

    protected final Rectangle hitbox;

    private int typeId;

    /*
     * members for drawing
     */
    private BufferedImage image;
    private int framesOfAnimate;

    protected AbstractBlock(LevelContext lvlCtx, int blockTypeId) {
        super(lvlCtx);

        typeId = blockTypeId;

        // all block size is UNIT_SIZE
        hitbox = new Rectangle(Constant.UNIT_SIZE);

        // get image resource by blockTypeId
        image = ResourcePackage.getImage("block-" + blockTypeId);

        if (image.getHeight() != hitbox.height
                || image.getWidth() % hitbox.width != 0
                || image.getWidth() < hitbox.width) {
            throw new RuntimeException("bad image resource: block-" + blockTypeId);
        }
        framesOfAnimate = image.getWidth() / hitbox.width;
    }

    @Override
    public Rectangle getHitbox() {
        return hitbox;
    }

    @Override
    public void draw(Graphics2D g) {

        int curframe = (getLevelContext().getFrameTicker() >> 4) % framesOfAnimate;

        g.drawImage(
                image,

                hitbox.x,
                hitbox.y,
                hitbox.x + hitbox.width,
                hitbox.y + hitbox.height,

                curframe * hitbox.width,
                0,
                (curframe + 1) * hitbox.width,
                hitbox.height,

                null
        );
    }

    @Override
    public void update() {
    }

    @Override
    public void onCollide(Physic.CollideAble<? extends Shape> anotherObject) {

    }
}
