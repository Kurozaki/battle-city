package com.yotwei.bc.battlecity.sprite;


import com.yotwei.bc.battlecity.core.Const;
import com.yotwei.bc.battlecity.factory.GraphicsFactory;
import com.yotwei.bc.battlecity.util.Debugger;
import sun.security.provider.PolicySpiFile;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by YotWei on 2018/6/12.
 */
public class TextSprite {

    public static final int DRAW_ALIGN_NONE = 0;
    public static final int DRAW_ALIGN_CENTER_HORIZONTAL = 1;
    public static final int DRAW_ALIGN_CENTER_VERTICAL = 2;
    public static final int DRAW_ALIGN_CENTER = DRAW_ALIGN_CENTER_HORIZONTAL | DRAW_ALIGN_CENTER_VERTICAL;

    public static final TextSprite NORMAL_TEXT = new TextSprite(
            GraphicsFactory.INSTANCE.getResourceById("font-1"),
            0x20, 0x7f, new Dimension(20, 24));

    private BufferedImage[] imageCache;
    private Dimension charImgSize;
    private int offsetAscii;

    private TextSprite(BufferedImage srcImage, int startAscii, int endAscii, Dimension charImageSize) {
        if (endAscii <= startAscii) {
            throw new IllegalArgumentException("endAscii must larger than startAscii");
        }

        this.offsetAscii = startAscii;
        this.charImgSize = charImageSize;

        int lNum = srcImage.getWidth() / charImageSize.width;
        imageCache = new BufferedImage[endAscii - startAscii];

        for (int i = 0; i < imageCache.length; i++) {
            int offsetX = (i % lNum) * charImageSize.width;
            int offsetY = (i / lNum) * charImageSize.height;
            imageCache[i] = srcImage.getSubimage(offsetX, offsetY, charImageSize.width, charImageSize.height);
        }
    }

    public void draw(String drawStr, int alignFlag, int x, int y, Graphics2D g) {
        char[] chars = drawStr.toCharArray();

        if ((alignFlag & DRAW_ALIGN_CENTER_HORIZONTAL) != 0) {
            x = (Const.SIZE_VIEWPORT.width - chars.length * charImgSize.width) >> 1;
        }
        if ((alignFlag & DRAW_ALIGN_CENTER_VERTICAL) != 0) {
            y = (Const.SIZE_VIEWPORT.height - charImgSize.height) >> 1;
        }

        for (int i = 0; i < chars.length; i++) {
            // get char's array index
            int charIndex = chars[i] - offsetAscii;

            // index range check
            if (0 > charIndex || charIndex >= imageCache.length)
                continue;

            g.drawImage(imageCache[charIndex], x + i * charImgSize.width, y, null);
        }
    }
}
