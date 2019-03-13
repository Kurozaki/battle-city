package com.yotwei.battlecity.game.object.block;

import com.yotwei.battlecity.game.engine.ResourcePackage;
import com.yotwei.battlecity.game.object.GameObject;
import com.yotwei.battlecity.game.object.GameObjectFactory;
import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.effect.Effect;
import com.yotwei.battlecity.game.object.properties.BulletDamageAble;
import com.yotwei.battlecity.game.object.properties.Physic;
import com.yotwei.battlecity.util.Constant;
import sun.management.HotspotClassLoadingMBean;

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
    protected BufferedImage image;
    private int framesOfAnimate;

    protected int durability;

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

        Effect burstEffect = GameObjectFactory.createEffect(getLevelContext(), 1);
        burstEffect.setCoordinate(
                hitbox.x + (hitbox.width - burstEffect.getHitbox().width >> 1),
                hitbox.y + (hitbox.height - burstEffect.getHitbox().height >> 1));

        LevelContext.Event ev = LevelContext.Event.wrap("addObject", burstEffect);
        getLevelContext().triggerEvent(ev);
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

        // calculate new durability after being damage
        int readDamage = Math.min(durability, damageValue);
        durability -= readDamage;

        if (durability == 0) {
            setActive(false);
        }

        return readDamage;
    }
}
