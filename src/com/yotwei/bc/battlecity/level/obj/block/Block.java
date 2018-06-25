package com.yotwei.bc.battlecity.level.obj.block;

import com.yotwei.bc.battlecity.factory.GraphicsFactory;
import com.yotwei.bc.battlecity.level.lvl.Level;
import com.yotwei.bc.battlecity.level.obj.EntityObject;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by YotWei on 2018/6/17.
 */
public class Block extends EntityObject {

    private final BufferedImage image;

    public Block(int modelId, Level level) {
        super(level);

        this.image = GraphicsFactory.INSTANCE.getResourceById("block-" + modelId);

        if (modelId == 2) {
            this.setDurability(Integer.MAX_VALUE);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(image, x, y, null);

        /*  for debug
        if (debugFlag != 0) {
            g.setColor(new Color(255, 255, 255, 64));
            g.fillRect(x, y, width, height);
            debugFlag = 0;
        }
        */
    }

    @Override
    public void update() {

    }
}
