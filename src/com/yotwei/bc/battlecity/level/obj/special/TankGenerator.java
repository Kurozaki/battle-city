package com.yotwei.bc.battlecity.level.obj.special;

import com.yotwei.bc.battlecity.core.Const;
import com.yotwei.bc.battlecity.factory.GraphicsFactory;
import com.yotwei.bc.battlecity.level.lvl.Event;
import com.yotwei.bc.battlecity.level.lvl.Level;
import com.yotwei.bc.battlecity.level.obj.EntityObject;
import com.yotwei.bc.battlecity.level.obj.property.Type;
import com.yotwei.bc.battlecity.others.Counter;
import com.yotwei.bc.battlecity.sprite.DefaultAnimateSprite;

import java.awt.*;

/**
 * Created by YotWei on 2018/6/23.
 */
public class TankGenerator extends Special {

    private static final int CREATE_DELAY = 60;

    private final DefaultAnimateSprite animation;
    private final Counter _counter = new Counter();

    private final Type tankType;
    private final int tankModelId;

    private TankGenerator(Type tankType, int modelId, Level level) {
        super(level);

        this.setSize(Const.SIZE_UNIT_2X);

        this.tankType = tankType;
        this.tankModelId = modelId;

        this.animation = new DefaultAnimateSprite(6, this,
                GraphicsFactory.INSTANCE.getResourceById("special-2"));
        this._counter.start(CREATE_DELAY);
    }


    @Override
    public void draw(Graphics2D g) {
        this.animation.drawNextFrame(g);
    }

    @Override
    public void update() {
        this._counter.update();
        if (this._counter.hasFinished() && isAbleGenerate()) {

            // send tank creation event
            switch (tankType) {
                case PLAYER:
                    getLevel().addEvent(Event.playerCreate(x, y));
                    break;
                case ENEMY:
                    getLevel().addEvent(Event.enemyCreate(tankModelId, x, y));
                    break;
                default:
            }

            this.destroySelf();
        }
    }

    private boolean isAbleGenerate() {
        for (EntityObject eo : getLevel().retrieveIntersections(this)) {
            if (eo != this)
                return false;
        }
        return true;
    }

    public static TankGenerator create(Type tankType, int modelId, int x, int y, Level level) {
        TankGenerator gen = new TankGenerator(tankType, modelId, level);
        gen.setLocation(x, y);
        return gen;
    }
}
