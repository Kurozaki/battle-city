package com.yotwei.battlecity.game.object.special;

import com.yotwei.battlecity.game.engine.ResourcePackage;
import com.yotwei.battlecity.game.object.GameObject;
import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.properties.Physic;
import com.yotwei.battlecity.game.object.tank.AbstractTank;
import com.yotwei.battlecity.util.Constant;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by YotWei on 2019/3/5.
 */
public class TankCreator<_TankType extends AbstractTank> extends SpecialObject {

    private static final int TANK_CREATE_DELAY_TICK = 60;

    private final _TankType accessTank;
    private int ticker;

    private Rectangle hitbox;

    private BufferedImage image;
    private int frameCount;

    private Set<String> retrieveGroupNames ;
    private LevelContext.RetrieveFilter<GameObject> retrieveFilter;

    public TankCreator(LevelContext lvlCtx, _TankType accessTank) {
        super(lvlCtx);

        this.accessTank = accessTank;
        this.hitbox = new Rectangle(Constant.UNIT_SIZE_2X);
    }

    @Override
    public void onActive() {

        this.image = ResourcePackage.getImage("special_tank_creator");
        this.frameCount = image.getWidth() / hitbox.width;

        retrieveGroupNames = new HashSet<>();
        retrieveGroupNames.add("tankGroup");
        retrieveFilter = anObject -> true;
    }

    @Override
    public void update() {

        if (ticker++ < TANK_CREATE_DELAY_TICK) {
            return;
        }

        Set<GameObject> retSet = getLevelContext()
                .retrieveGameObject(hitbox, retrieveGroupNames, retrieveFilter);
        if (retSet.isEmpty()) {
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
        int frameX = (ticker >> 3) % frameCount;
        g.drawImage(
                image,

                hitbox.x,
                hitbox.y,
                hitbox.x + hitbox.width,
                hitbox.y + hitbox.height,

                frameX * hitbox.width,
                0,
                (frameX + 1) * hitbox.width,
                hitbox.height,

                null
        );
    }

    @Override
    public int getDrawPriority() {
        return 3;
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
