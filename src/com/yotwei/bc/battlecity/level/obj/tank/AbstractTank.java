package com.yotwei.bc.battlecity.level.obj.tank;

import com.yotwei.bc.battlecity.core.Const;
import com.yotwei.bc.battlecity.level.lvl.Event;
import com.yotwei.bc.battlecity.level.lvl.Level;
import com.yotwei.bc.battlecity.level.obj.EntityObject;
import com.yotwei.bc.battlecity.level.obj.MovableObject;
import com.yotwei.bc.battlecity.level.obj.block.Block;
import com.yotwei.bc.battlecity.level.obj.property.Direction;
import com.yotwei.bc.battlecity.sprite.TankAnimateSprite;

import java.awt.*;

/**
 * Created by YotWei on 2018/6/17.
 */
public abstract class AbstractTank extends MovableObject {

    private final int modelId;
    private final TankAnimateSprite sprite;

    AbstractTank(int modelId, Level level) {
        super(level);
        this.setSize(Const.SIZE_UNIT_2X);

        this.modelId = modelId;
        this.sprite = new TankAnimateSprite(this);
    }

    public int getModelId() {
        return modelId;
    }

    @Override
    public void update() {
        resolveMovement();
        resolveBulletProjection();
    }

    private void resolveBulletProjection() {
        if (isAbleProjectBullet())
            projectBullet();
    }

    @Override
    public void draw(Graphics2D g) {
        sprite.drawNextFrame(g);
    }

    private void resolveMovement() {
        Direction nextDir = nextDirection();
        if (nextDir == null)
            return;

        // get next move box
        Rectangle nextMovBox = getNextMoveBox(nextDir, nextDir != this.direction);

        // test next move
        if (!isMovBoxAbleToBePlace(nextMovBox))
            return;

        // update location and direction
        this.setLocation(nextMovBox.getLocation());
        this.direction = nextDir;
    }

    Rectangle getNextMoveBox(Direction nextDir, boolean isDirChange) {
        Rectangle r = new Rectangle(this);
        if (isDirChange) {
            if (this.direction.opposite() != nextDir) {
                if (nextDir.isHorizontal()) {
                    r.y = ((r.y + (Const.SIZE_UNIT.height >> 1)) / Const.SIZE_UNIT.height)
                            * Const.SIZE_UNIT.height;
                } else {
                    r.x = ((r.x + (Const.SIZE_UNIT.width >> 1)) / Const.SIZE_UNIT.width)
                            * Const.SIZE_UNIT.width;
                }
            }
        } else {
            switch (nextDir) {
                case DOWN:
                    r.y += movsp.getMovingPixel();
                    break;
                case RIGHT:
                    r.x += movsp.getMovingPixel();
                    break;
                case LEFT:
                    r.x -= movsp.getMovingPixel();
                    break;
                case UP:
                    r.y -= movsp.getMovingPixel();
                    break;
            }
        }
        return r;
    }

    private void projectBullet() {
        getLevel().addEvent(Event.projectBullet(this));
    }

    boolean isMovBoxAbleToBePlace(Rectangle box) {
        if (!getLevel().isInBound(box))
            return false;
        for (EntityObject eo : getLevel().retrieveIntersections(box)) {
            if (eo != this && isEntityObjectBlock(eo))
                return false;
        }
        return true;
    }

    @Override
    protected void destroySelf() {
        super.destroySelf();

        // create effect
        getLevel().addEvent(Event.createEffect(2, x, y, 12));
    }

    private boolean isEntityObjectBlock(EntityObject eo) {
        return eo instanceof AbstractTank ||
                eo instanceof Block;
    }

    protected abstract Direction nextDirection();

    protected abstract boolean isAbleProjectBullet();
}
