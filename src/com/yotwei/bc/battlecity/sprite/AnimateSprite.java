package com.yotwei.bc.battlecity.sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by YotWei on 2018/6/18.
 */
public abstract class AnimateSprite {

    protected final BufferedImage imgSrc;

    public AnimateSprite(BufferedImage imgSrc) {
        this.imgSrc = imgSrc;
    }

    public abstract void drawNextFrame(Graphics2D g);
}
