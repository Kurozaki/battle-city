package com.yotwei.battlecity.game.object.tank;

import com.yotwei.battlecity.game.object.block.Grass;
import com.yotwei.battlecity.game.object.GameObject;
import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.properties.Physic;
import com.yotwei.battlecity.game.object.tank.behavior.ITankBehavior;
import com.yotwei.battlecity.util.Constant;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * Created by YotWei on 2019/3/1.
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractTank extends GameObject {

    protected ITankBehavior<? extends AbstractTank> behavior;

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
    }


    /**
     * get the move speed of tank
     * move speed is an integer, with follow format
     * +------------+-----------+
     * | 24 bits    |  8 bits   |
     * | speed      | speed sub |
     * +------------+-----------+
     */
    protected abstract int calcMoveSpeed();

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
    }

    @Override
    public void update() {
        //
        // update hitboxPrev's coordinate as hitbox's
        // and update directionPrev value as direction's value
        //
        hitboxPrev.setLocation(hitbox.getLocation());
        directionPrev = direction;

        // update tank coordinate according to the next moving direction
        Direction nextDir = Objects.requireNonNull(behavior).nextMoveDirection();
        updateTankCoord(nextDir);
    }

    @Override
    public void onInactive() {

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

//        g.setColor(Color.WHITE);
//        g.drawString(direction.toString(), hitbox.x, hitbox.y);
    }

    @Override
    public int getDrawPriority() {
        return 1;
    }

    /*
     * -------------------------------------------------------------------------------------
     * <p>
     * implements from {@link Physic}
     * <p>
     * -------------------------------------------------------------------------------------
     */
    @Override
    public void onCollide(Physic<? extends Shape> anotherObject) {

        if (anotherObject instanceof Grass) {
            return;
        }

        hitbox.setLocation(hitboxPrev.getLocation());
        direction = directionPrev;
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

    /**
     * direction is a enum
     * to tell which direction the tank will move to in the next step
     */
    public enum Direction {

        LEFT(0), UP(1), RIGHT(2), DOWN(3);

        private int index;

        Direction(int index) {
            this.index = index;
        }

        public static boolean isOpposite(Direction d1, Direction d2) {
            return d1 != null && d2 != null &&
                    Math.abs(d1.index - d2.index) == 2;
        }
    }
}
