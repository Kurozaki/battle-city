package com.yotwei.battlecity.game.components;

import com.yotwei.battlecity.game.global.ResourcePool;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by YotWei on 2019/3/18.
 */
public class GOSprite implements GOComponent {

    //
    // 从图片资源中截取的区域
    //
    private final Rectangle srcRect;
    private BufferedImage image;

    private int frameCountX, frameCountY;

    // 绘制优先级，数值越大优先级越高
    private int drawPriority;

    public GOSprite() {
        srcRect = new Rectangle();
    }

    @Override
    public String name() {
        return "Sprite";
    }

    public void setImageResourceId(String id) {
        image = ResourcePool.getImage(id);
    }

    /**
     * 设置实际要绘制的尺寸大小
     */
    public void setFrameSize(int width, int height) {
        srcRect.setSize(width, height);

        // 重新计算水平和垂直方向的帧数
        frameCountX = image.getWidth() / srcRect.width;
        frameCountY = image.getHeight() / srcRect.height;
    }

    /**
     * 设置绘制的偏移的x和y
     */
    public void setFrameOffset(int x, int y) {
        // 防止越界
        if (x >= frameCountX) x %= frameCountX;
        if (y >= frameCountY) y %= frameCountY;

        srcRect.x = srcRect.width * x;
        srcRect.y = srcRect.height * y;
    }

    public Rectangle getSrcRect() {
        return srcRect;
    }

    public void setDrawPriority(int drawPriority) {
        this.drawPriority = drawPriority;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getDrawPriority() {
        return drawPriority;
    }
}
