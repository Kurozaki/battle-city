package com.yotwei.battlecity.game.object.bullet;

import com.yotwei.battlecity.game.object.GameObject;
import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.properties.BulletDamageAble;
import com.yotwei.battlecity.game.object.properties.Direction;
import com.yotwei.battlecity.game.object.properties.Physic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.management.HotspotClassLoadingMBean;

import java.awt.*;

/**
 * Created by YotWei on 2019/3/6.
 */
public abstract class AbstractBullet extends GameObject
        implements BulletDamageAble {

    private static final Logger logger = LoggerFactory.getLogger("AbstractBullet");

    private static final Dimension SIZE_DEFAULT = new Dimension(8, 8);
    private static final int SPEED_DEFAULT = 640;
    private static final int ATK_DEFAULT = 100;

    private final Rectangle hitbox = new Rectangle(SIZE_DEFAULT);

    private int bulletATK = ATK_DEFAULT;

    private int speed = SPEED_DEFAULT;
    private int movePixel;
    private Direction direction;

    public AbstractBullet(LevelContext lvlCtx) {
        super(lvlCtx);
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /* -------------------------------------------------------
     *
     * method implements from {@link BulletDamageAble}
     *
     * -------------------------------------------------------
     */
    @Override
    public int tryDamage(int damageValue) {
        return 0;
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
        // TODO: 2019/3/6 replace with image later
        g.setColor(Color.WHITE);
        g.fillOval(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }

    @Override
    public int getDrawPriority() {
        return 0;
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
//        if (logger.isInfoEnabled()) {
//            logger.info("bullet: {} removed()", getUID());
//        }
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
