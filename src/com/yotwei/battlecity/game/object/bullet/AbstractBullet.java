package com.yotwei.battlecity.game.object.bullet;

import com.yotwei.battlecity.game.engine.ResourcePackage;
import com.yotwei.battlecity.game.object.GameObject;
import com.yotwei.battlecity.game.object.GameObjectFactory;
import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.effect.Effect;
import com.yotwei.battlecity.game.object.properties.BulletDamageAble;
import com.yotwei.battlecity.game.object.properties.Direction;
import com.yotwei.battlecity.game.object.properties.Physic;
import com.yotwei.battlecity.game.object.tank.AbstractTank;
import com.yotwei.battlecity.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.management.HotspotClassLoadingMBean;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by YotWei on 2019/3/6.
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractBullet extends GameObject
        implements BulletDamageAble {

    private static final Logger logger = LoggerFactory.getLogger("AbstractBullet");

    private static final Dimension SIZE_DEFAULT = new Dimension(8, 8);
    private static final int SPEED_DEFAULT = 720;
    private static final int ATK_DEFAULT = 100;

    private final int bulletId;

    private Rectangle hitbox = new Rectangle(SIZE_DEFAULT);

    private BufferedImage image;

    protected int bulletATK = ATK_DEFAULT;

    private int speed = SPEED_DEFAULT;
    private int movePixel;
    private Direction direction;

    private final AbstractTank associateTank;

    protected AbstractBullet(LevelContext lvlCtx, int bulletId, AbstractTank associateTank) {
        super(lvlCtx);
        this.bulletId = bulletId;
        this.associateTank = associateTank;

    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    protected AbstractTank getAssociateTank() {
        return associateTank;
    }

    /* -------------------------------------------------------
     *
     * method implements from {@link BulletDamageAble}
     *
     * -------------------------------------------------------
     */
    @Override
    public int tryDamage(int damageValue) {

        int readDamage = Math.min(bulletATK, damageValue);
        bulletATK -= readDamage;

        if (bulletATK == 0) {
            setActive(false);
        }

        return readDamage;
    }

    /*
     * -------------------------------------------------------
     *
     * methods implements from {@link com.yotwei.battlecity.game.object.properties.DrawAble}
     *
     * -------------------------------------------------------
     */
    @Override
    public void draw(Graphics2D g) {
        g.drawImage(
                image,

                hitbox.x - 4,
                hitbox.y - 4,
                hitbox.x - 4 + Constant.UNIT_SIZE.width,
                hitbox.y - 4 + Constant.UNIT_SIZE.height,

                direction.index * Constant.UNIT_SIZE.width,
                0,
                (direction.index + 1) * Constant.UNIT_SIZE.width,
                Constant.UNIT_SIZE.height,

                null
        );
    }

    @Override
    public int getDrawPriority() {
        return 1;
    }

    /*
     * -------------------------------------------------------------------------------------
     *
     * method implements from {@link com.yotwei.battlecity.game.object.properties.LifeCycle}
     *
     * -------------------------------------------------------------------------------------
     */
    @Override
    public void onActive() {
        this.image = ResourcePackage.getImage("bullet-" + bulletId);
        Dimension unitSize = Constant.UNIT_SIZE;
        if (this.image.getWidth() != unitSize.width * 4 &&
                this.image.getHeight() != unitSize.height) {
            throw new RuntimeException("Bad image bullet-" + bulletId);
        }
    }

    @Override
    public void update() {

        movePixel += speed;
        int pixel = movePixel >> 8;
        movePixel &= 0xff;

        switch (direction) {
            case UP:
                hitbox.y -= pixel;
                break;

            case RIGHT:
                hitbox.x += pixel;
                break;

            case DOWN:
                hitbox.y += pixel;
                break;

            case LEFT:
                hitbox.x -= pixel;
                break;

            default:
        }
    }

    @Override
    public void onInactive() {
        Effect effect = GameObjectFactory.createEffect(getLevelContext(), 1);
        effect.setCoordinate(
                hitbox.x + (hitbox.width - effect.getHitbox().width >> 1),
                hitbox.y + (hitbox.height - effect.getHitbox().height >> 1)
        );
        LevelContext.Event ev = LevelContext.Event.wrap("addObject", effect);
        getLevelContext().triggerEvent(ev);
    }

    /*
     * --------------------------------------------------------
     *
     * method implements from {@link Physic}
     *
     * --------------------------------------------------------
     */
    @Override
    public Rectangle getHitbox() {
        return hitbox;
    }

    @Override
    public void onCollide(Physic<? extends Shape> anotherObject) {
        if (!(anotherObject instanceof BulletDamageAble)) {
            return;
        }

        if (anotherObject instanceof AbstractTank) {
            if (((AbstractTank) anotherObject).getTag().equals(associateTank.getTag()))
                return;
        }


        // calculate read damage
        int readDamage = ((BulletDamageAble) anotherObject).tryDamage(bulletATK);
        bulletATK -= readDamage;

        if (bulletATK <= 0) {
            setActive(false);
        }
    }

    @Override
    public void onTouchBound(Rectangle bound) {
        if (!bound.intersects(hitbox)) {
            setActive(false);
        }
    }
}
