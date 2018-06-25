package com.yotwei.bc.battlecity.level.obj.block;

import com.yotwei.bc.battlecity.core.Const;
import com.yotwei.bc.battlecity.factory.GraphicsFactory;
import com.yotwei.bc.battlecity.level.lvl.Event;
import com.yotwei.bc.battlecity.level.lvl.Level;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by YotWei on 2018/6/25.
 */
public class Eagle extends Block {

    private static final BufferedImage EAGLE_IMG = GraphicsFactory.INSTANCE.getResourceById("special-1");

    private boolean destroyedFlag = false;

    public Eagle(int modelId, Level level) {
        super(modelId, level);
        this.setSize(Const.SIZE_UNIT_2X);
    }

    @Override
    public void decDurability(int dec) {
        if (!destroyedFlag) {
            destroyedFlag = true;

            getLevel().addEvent(Event.createEffect(2, x, y, 12));
            getLevel().addEvent(Event.gameOver());
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(
                EAGLE_IMG.getSubimage(width * (destroyedFlag ? 1 : 0),
                        0,
                        width,
                        height),
                x, y, null);
    }
}
