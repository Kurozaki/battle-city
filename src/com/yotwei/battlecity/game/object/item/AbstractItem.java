package com.yotwei.battlecity.game.object.item;

import com.yotwei.battlecity.game.engine.ResourcePackage;
import com.yotwei.battlecity.game.object.GameObject;
import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.properties.Physic;
import com.yotwei.battlecity.game.object.tank.AbstractTank;
import com.yotwei.battlecity.util.Constant;
import sun.util.resources.cldr.id.CalendarData_id_ID;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by YotWei on 2019/3/11.
 */
public abstract class AbstractItem extends GameObject {

    private boolean hasBeenPickedUp = false;

    private int ticker = 10 * 60;

    private final int itemId;
    private BufferedImage image;
    private Rectangle hitbox;

    protected AbstractItem(LevelContext lvlCtx, int itemId) {
        super(lvlCtx);

        this.itemId = itemId;
    }

    @Override
    public void draw(Graphics2D g) {
        if (hasBeenPickedUp)
            return;

        if (ticker < 150 && (ticker & 1) == 1) {
            return;
        }

        if (image != null) {
            g.drawImage(image, hitbox.x, hitbox.y, null);
        } else {
            g.setColor(Color.white);
            g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
            g.drawString(getClass().getSimpleName().substring(0, 4), hitbox.x, hitbox.y + hitbox.height);
        }
    }

    @Override
    public int getDrawPriority() {
        return 8;
    }

    @Override
    public void onActive() {
        image = ResourcePackage.getImage("item-" + itemId);
        hitbox = new Rectangle(Constant.UNIT_SIZE_2X);

//        if (image.getWidth() != hitbox.width && image.getHeight() != hitbox.height) {
//            throw new RuntimeException("bad image: item-" + itemId);
//        }
    }

    @Override
    public void update() {
        if (hasBeenPickedUp) {
            setActive(false);
        }

        if (ticker-- <= 0) {
            setActive(false);
        }
    }

    @Override
    public void onInactive() {

    }

    @Override
    public Rectangle getHitbox() {
        return hitbox;
    }

    @Override
    public void onCollide(Physic<? extends Shape> anotherObject) {
        if (anotherObject instanceof AbstractTank) {
            if (!hasBeenPickedUp) {
                hasBeenPickedUp = onTankPickUp((AbstractTank) anotherObject);
            }
        }
    }

    @Override
    public void onTouchBound(Rectangle bound) {

    }

    protected abstract boolean onTankPickUp(AbstractTank tank);
}
