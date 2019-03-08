package com.yotwei.battlecity.game.object.block;

import com.yotwei.battlecity.game.engine.ResourcePackage;
import com.yotwei.battlecity.game.object.GameObject;
import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.properties.BulletDamageAble;
import com.yotwei.battlecity.game.object.properties.Physic;
import com.yotwei.battlecity.util.Constant;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by YotWei on 2019/2/25.
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractBlock extends GameObject
        implements BulletDamageAble {

    private int typeId;

    protected final Rectangle hitbox;

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
    }

    /*
     * -------------------------------------------------------------------------------------
     * <p>
     * method implements from {@link com.yotwei.battlecity.game.object.properties.DrawAble}
     * <p>
     * -------------------------------------------------------------------------------------
     */
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
    public int getDrawPriority() {
        return 0;
    }

    /*
     * -------------------------------------------------------------------------------------
     * <p>
     * methods implements from {@link com.yotwei.battlecity.game.object.properties.LifeCycle}
     * <p>
     * -------------------------------------------------------------------------------------
     */
    @Override
    public void onActive() {
        // get image resource by blockTypeId
        image = ResourcePackage.getImage("block-" + typeId);

        if (image.getHeight() != hitbox.height
                || image.getWidth() % hitbox.width != 0
                || image.getWidth() < hitbox.width) {
            throw new RuntimeException("bad image resource: block-" + typeId);
        }
        framesOfAnimate = image.getWidth() / hitbox.width;

        // using "Block-" + [CLASS_NAME] as tag
        setTag("Block-" + getClass().getSimpleName());
    }

    @Override
    public void update() {

    }

    @Override
    public void onInactive() {

    }

    /*
     * -------------------------------------------------------------------------------------
     * <p>
     * methods implements from {@link Physic}
     * <p>
     * -------------------------------------------------------------------------------------
     */
    @Override
    public void onCollide(Physic<? extends Shape> anotherObject) {

    }

    @Override
    public void onTouchBound(Rectangle bound) {

    }

    @Override
    public Rectangle getHitbox() {
        return hitbox;
    }


    /* -------------------------------------------------------------------------------------
     *
     * method implements from {@link BulletDamageAble}
     *
     * -------------------------------------------------------------------------------------
     */
    @Override
    public int tryDamage(int damageValue) {
        // TODO: 2019/3/7 完善被子弹击中的逻辑
        setActive(false);
        return damageValue;
    }
}
