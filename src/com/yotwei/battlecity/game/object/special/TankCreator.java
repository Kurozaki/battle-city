package com.yotwei.battlecity.game.object.special;

import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.properties.Physic;
import com.yotwei.battlecity.game.object.tank.AbstractTank;
import com.yotwei.battlecity.util.Constant;

import java.awt.*;

/**
 * Created by YotWei on 2019/3/5.
 */
public class TankCreator<_TankType extends AbstractTank> extends SpecialObject {

    private static final int TANK_CREATE_DELAY_TICK = 60;

    private final _TankType accessTank;
    private int ticker;

    private Rectangle hitbox;

    public TankCreator(LevelContext lvlCtx, _TankType accessTank) {
        super(lvlCtx);

        this.accessTank = accessTank;
        this.hitbox = new Rectangle(Constant.UNIT_SIZE_2X);
    }


    @Override
    public void onActive() {
        ticker = TANK_CREATE_DELAY_TICK;
    }

    @Override
    public void update() {

        if (ticker-- <= 0) {
            // destroyed self
            setActive(false);
        }
    }

    @Override
    public void onInactive() {
        accessTank.getHitbox().setLocation(hitbox.getLocation());
        LevelContext.Event e = LevelContext.Event.wrap("addObject", accessTank);
        getLevelContext().triggerEvent(e);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.GRAY);
        g.fillOval(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }

    @Override
    public int getDrawPriority() {
        return 0;
    }

    @Override
    public Rectangle getHitbox() {
        return hitbox;
    }

    @Override
    public void onCollide(Physic<? extends Shape> anotherObject) {

    }

    @Override
    public void onTouchBound(Rectangle bound) {

    }
}
