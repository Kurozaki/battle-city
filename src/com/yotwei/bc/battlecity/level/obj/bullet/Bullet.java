package com.yotwei.bc.battlecity.level.obj.bullet;

import com.yotwei.bc.battlecity.level.lvl.Level;
import com.yotwei.bc.battlecity.level.obj.EntityObject;
import com.yotwei.bc.battlecity.level.obj.MovableObject;
import com.yotwei.bc.battlecity.level.obj.property.Direction;
import com.yotwei.bc.battlecity.level.obj.tank.AbstractTank;

import java.awt.*;
import java.util.Set;

/**
 * Created by YotWei on 2018/6/19.
 */
public class Bullet extends MovableObject {

    private static final Dimension DEFAULT_BULLET_SIZE = new Dimension(4, 4);

    private Bullet(Level level) {
        super(level);

        this.movsp.setSpeed(0x400);
        this.setSize(DEFAULT_BULLET_SIZE);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
    }

    @Override
    public void update() {

        /* resolve movement */
        if (getLevel().isOutOfBound(this)) {
            this.destroySelf();
        }
        updatePosition();

        /* resolve collision */
        Set<EntityObject> intersSet = getLevel().retrieveIntersections(this);
        for (EntityObject eo : intersSet) {
            if (eo.getType() != this.getType()) {
                int dec = Math.min(eo.getDurability(), this.getDurability());

                // decrease the min durability value in two object
                eo.decDurability(dec);
                this.decDurability(dec);
            }
        }
    }

    private void updatePosition() {
        switch (direction) {
            case RIGHT:
                this.x += movsp.getMovingPixel();
                break;
            case LEFT:
                this.x -= movsp.getMovingPixel();
                break;
            case UP:
                this.y -= movsp.getMovingPixel();
                break;
            case DOWN:
                this.y += movsp.getMovingPixel();
                break;
        }
    }

    private void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * A factory method for creating a bullet
     *
     * @param modelId model ID of the bullet ,default 0 now
     * @param lchTank the launcher tank
     * @param level   level
     * @return bullet instance
     */
    public static Bullet create(int modelId, AbstractTank lchTank, Level level) {
        Bullet bullet = new Bullet(level);

        // set tank's direction and type as bullet as direction and type
        bullet.setDirection(lchTank.getDirection());
        bullet.setType(lchTank.getType());

        // set tank's center as the initial position of the bullet
        bullet.setLocation(lchTank.x + ((lchTank.width - bullet.width) >> 1),
                lchTank.y + ((lchTank.height - bullet.height) >> 1));

        return bullet;
    }
}
