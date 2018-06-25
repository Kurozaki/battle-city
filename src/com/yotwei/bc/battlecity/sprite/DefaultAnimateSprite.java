package com.yotwei.bc.battlecity.sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by YotWei on 2018/6/23.
 */
public class DefaultAnimateSprite extends AnimateSprite {

    private final Dimension frameSize;
    private final int frames;
    private final Rectangle attachBox;

    private int _counter = 0;

    public DefaultAnimateSprite(int frames, Rectangle attachBox, BufferedImage imgSrc) {
        super(imgSrc);
        this.attachBox = attachBox;
        this.frames = frames;
        this.frameSize = new Dimension(imgSrc.getWidth() / frames, imgSrc.getHeight());
    }

    @Override
    public void drawNextFrame(Graphics2D g) {
        int offsetX = (_counter++ / 8) % frames * frameSize.width;
        g.drawImage(
                imgSrc.getSubimage(offsetX, 0, frameSize.width, frameSize.height),
                attachBox.x,
                attachBox.y,
                null);
    }
}
