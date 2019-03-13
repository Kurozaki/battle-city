package com.yotwei.battlecity.game.object.tank;

import com.yotwei.battlecity.game.engine.ResourcePackage;
import com.yotwei.battlecity.game.object.GameObjectFactory;
import com.yotwei.battlecity.game.object.block.AbstractBlock;
import com.yotwei.battlecity.game.object.GameObject;
import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.bullet.AbstractBullet;
import com.yotwei.battlecity.game.object.effect.Effect;
import com.yotwei.battlecity.game.object.properties.BulletDamageAble;
import com.yotwei.battlecity.game.object.properties.Direction;
import com.yotwei.battlecity.game.object.properties.Physic;
import com.yotwei.battlecity.game.object.tank.behavior.AbstractTankBulletProjection;
import com.yotwei.battlecity.game.object.tank.behavior.AbstractTankMovement;
import com.yotwei.battlecity.util.Constant;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by YotWei on 2019/3/1.
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractTank extends GameObject
        implements BulletDamageAble {

    protected AbstractTankMovement<? extends AbstractTank> tankMovement;
    protected AbstractTankBulletProjection<? extends AbstractTank> tankBulletProj;

    protected Map<String, Object> extra;

    protected int invincibleTime;
    protected int tankDurability;

    private int hurtTicker = 0;


    /*
     * image resource of tank
     */
    protected BufferedImage image;
    private int framesOfAnimate;
    private int animateTicker;

    /*
     * hitboxPrev is a copy of hitbox
     * when collision happen, recover hitbox's coordinate to hitboxPrev's coordinate
     */
    private final Rectangle hitboxPrev;
    private final Rectangle hitbox;

    /*
     * members for moving behavior
     */
    protected Direction direction = Direction.UP;
    private Direction directionPrev = null;

    private int movePixel;


    protected AbstractTank(LevelContext lvlCtx) {
        super(lvlCtx);

        hitbox = new Rectangle(Constant.UNIT_SIZE_2X);
        hitboxPrev = new Rectangle(hitbox);
        extra = new HashMap<>();
    }

    public final Map<String, Object> getExtra() {
        return extra;
    }

    /**
     * get the move speed of tank
     * tank's moving speed can be change at any time
     * so it need a method to calculate
     * <p>
     * moving speed is an integer, with follow format
     * +------------+-----------+
     * | 24 bits    |  8 bits   |
     * | speed      | speed sub |
     * +------------+-----------+
     */
    protected abstract int calcMoveSpeed();

    /**
     * update tank's coordinate according to giving direction
     */
    private void updateTankCoord(Direction nextDir) {
        if (null == nextDir) {

            // stop moving, set animate ticker to 0
            animateTicker = 0;
            return;
        }

        if (!Direction.isOpposite(direction, nextDir)
                && direction != nextDir) {
            //
            // fixed coordinate
            //
            Dimension unitSize = Constant.UNIT_SIZE;

            if (nextDir == Direction.LEFT || nextDir == Direction.RIGHT) {
                //
                // change to horizontal direction
                //

                // fixed vertical coordinate
                int offsetY = hitbox.y % unitSize.height;
                if ((offsetY << 1) < unitSize.height) {   // offsetY < unitSize.height/2
                    hitbox.y -= offsetY;
                } else {
                    hitbox.y += unitSize.height - offsetY;
                }

            } else {
                //
                // change to vertical direction
                //

                // fixed horizontal coordinate
                int offsetX = hitbox.x % unitSize.width;
                if ((offsetX << 1) < unitSize.width) {  // offsetX < unitSize.width/2;
                    hitbox.x -= offsetX;
                } else {
                    hitbox.x += unitSize.width - offsetX;
                }
            }
        } else {

            if (nextDir == direction) {

                //
                // calculate moving pixel
                // and update coordinate
                //
                movePixel += calcMoveSpeed();
                int pixel = movePixel >> 8;
                movePixel &= 0xff;

                switch (nextDir) {
                    case UP:
                        hitbox.y -= pixel;
                        break;

                    case DOWN:
                        hitbox.y += pixel;
                        break;

                    case LEFT:
                        hitbox.x -= pixel;
                        break;

                    case RIGHT:
                        hitbox.x += pixel;
                        break;
                }
            }
        }

        animateTicker++;
        direction = nextDir;
    }

    private void projectBullet(AbstractBullet bullet) {
        if (bullet == null)
            return;
        //
        // trigger an addObject event
        // append data is bullet instance
        //
        LevelContext.Event ev =
                LevelContext.Event.wrap("addObject", bullet);
        getLevelContext().triggerEvent(ev);
    }


    /*
     * -------------------------------------------------------------------------------------
     * <p>
     * method implements from {@link com.yotwei.battlecity.game.object.properties.LifeCycle}
     * <p>
     * -------------------------------------------------------------------------------------
     */

    @Override
    public void onActive() {
        if (image != null)
            framesOfAnimate = image.getWidth() / hitbox.width;

        setInvincibleTime(60 * 2 /* init 2s invincible time */);
    }

    @Override
    public void update() {

        // decrease invincible time
        if (invincibleTime > 0)
            --invincibleTime;

        if (hurtTicker > 0)
            --hurtTicker;

        //
        // update hitboxPrev's coordinate as hitbox's
        // and update directionPrev value as direction's value
        //
        hitboxPrev.setLocation(hitbox.getLocation());
        directionPrev = direction;

        // update tank coordinate according to the next moving direction
        Direction nextDir = Objects.requireNonNull(tankMovement).nextMoveDirection();
        updateTankCoord(nextDir);

        // handle bullet projection
        AbstractBullet bullet = Objects.requireNonNull(tankBulletProj).getProjBullet();
        projectBullet(bullet);
    }

    @Override
    public void onInactive() {
        Effect burstEffect = GameObjectFactory.createEffect(getLevelContext(), 2);
        burstEffect.setCoordinate(
                hitbox.x + (hitbox.width - burstEffect.getHitbox().width >> 1),
                hitbox.y + (hitbox.height - burstEffect.getHitbox().height >> 1));

        LevelContext.Event ev = LevelContext.Event.wrap("addObject", burstEffect);
        getLevelContext().triggerEvent(ev);
    }


    /*
     * -------------------------------------------------------------------------------------
     * <p>
     * method implements from {@link DrawAble}
     * <p>
     * -------------------------------------------------------------------------------------
     */
    @Override
    public void draw(Graphics2D g) {
        if (image == null) return;

        if ((hurtTicker & 1) == 0) {

            int sx = ((animateTicker >> 2) % framesOfAnimate) * hitbox.width;
            int sy = direction.index * hitbox.height;

            g.drawImage(
                    image,

                    hitbox.x,
                    hitbox.y,
                    hitbox.x + hitbox.width,
                    hitbox.y + hitbox.height,

                    sx,
                    sy,
                    sx + hitbox.width,
                    sy + hitbox.height,
                    null
            );
        }

        if (invincibleTime > 0) {
            BufferedImage pimg = ResourcePackage.getImage("special_protected");
            int x = ((invincibleTime >> 3) % 2) * hitbox.width;

            g.drawImage(
                    pimg,

                    hitbox.x,
                    hitbox.y,
                    hitbox.x + hitbox.width,
                    hitbox.y + hitbox.height,

                    x,
                    0,
                    x + hitbox.width,
                    hitbox.height,

                    null
            );
        }
    }

    @Override
    public int getDrawPriority() {
        return 2;
    }

    /*
     * -------------------------------------------------------------------------------------
     *
     * implements from {@link Physic}
     *
     * -------------------------------------------------------------------------------------
     */
    @Override
    public void onCollide(Physic<? extends Shape> anotherObject) {

        if (!(anotherObject instanceof GameObject)) {
            return;
        }

        String objectTag = ((GameObject) anotherObject).getTag();
        String prefix = objectTag.substring(0, objectTag.indexOf("-"));

        switch (prefix) {
            case "Block":
                collideWithBlock(((AbstractBlock) anotherObject));
                break;

            case "Tank":
                collideWithTank((AbstractTank) anotherObject);
                break;
        }
    }

    @Override
    public void onTouchBound(Rectangle bound) {
        hitbox.setLocation(hitboxPrev.getLocation());
        direction = directionPrev;
    }

    @Override
    public Rectangle getHitbox() {
        return hitbox;
    }


    /*
     * -------------------------------------------------------------------------------------
     *
     * self declare methods
     *
     * -------------------------------------------------------------------------------------
     */
    public Direction getDirection() {
        return direction;
    }

    private void collideWithBlock(AbstractBlock block) {

        if (block.getTag().equals("Block-Grass"))
            return;

        hitbox.setLocation(hitboxPrev.getLocation());
        direction = directionPrev;
    }

    private void collideWithTank(AbstractTank tank) {

        boolean fixedCoord = false;

        Rectangle tankHitbox = tank.getHitbox();

        int cx1 = tankHitbox.x + tankHitbox.width / 2;
        int cy1 = tankHitbox.y + tankHitbox.height / 2;

        int cx2 = hitbox.x + hitbox.width / 2;
        int cy2 = hitbox.y + hitbox.height / 2;

        int disH = Math.abs(cx2 - cx1);
        int disV = Math.abs(cy2 - cy1);

        switch (direction) {

            case UP:
                fixedCoord = cy2 > cy1 && disH < disV;
                break;

            case DOWN:
                fixedCoord = cy2 < cy1 && disH < disV;
                break;

            case RIGHT:
                fixedCoord = cx2 < cx1 && disV < disH;
                break;

            case LEFT:
                fixedCoord = cx2 > cx1 && disV < disH;
                break;

        }

        if (fixedCoord) {
            hitbox.setLocation(hitboxPrev.getLocation());
            direction = directionPrev;
        }
    }

    @Override
    public int tryDamage(int damageValue) {

        if (invincibleTime > 0) {
            // invincible status, tank can block any bullet
            return damageValue;
        }

        hurtTicker = 16;

        int readDamage = Math.min(tankDurability, damageValue);
        tankDurability -= readDamage;

        if (tankDurability == 0) {
            setActive(false);

        }
        return readDamage;
    }

    public void setInvincibleTime(int time) {
        this.invincibleTime = time;
    }
}
