package com.yotwei.bc.battlecity.level.obj.effect;

import com.yotwei.bc.battlecity.core.Const;
import com.yotwei.bc.battlecity.factory.GraphicsFactory;
import com.yotwei.bc.battlecity.level.lvl.Level;
import com.yotwei.bc.battlecity.level.obj.EntityObject;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by YotWei on 2018/6/23.
 */
public class Effect extends EntityObject {

    private final BufferedImage image;
    private final int frame;
    private int aliveTime;

    private Effect(int effectId, int aliveTime, Level level) {
        super(level);

        this.image = GraphicsFactory.INSTANCE.getResourceById("effect-" + effectId);
        this.setSize(Const.SIZE_UNIT_2X);
        this.aliveTime = aliveTime;
        this.frame = image.getWidth() / width;
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(image.getSubimage((aliveTime / 4) % frame * width, 0, width, height), x, y, null);
    }

    @Override
    public void update() {
        if (aliveTime-- < 0)
            this.destroySelf();
    }

    public static Effect create(int effectId, int x, int y, int aliveTime, Level level) {
        Effect e = new Effect(effectId, aliveTime, level);
        e.setLocation(x, y);
        return e;
    }
}
