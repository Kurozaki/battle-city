package com.yotwei.battlecity.util;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by YotWei on 2019/2/28.
 */
public class GraphicUtil {

    private GraphicUtil() {
    }

    public static void clearScreen(Graphics2D g, Color color) {
        g.setColor(color);
        g.fillRect(0, 0, Constant.WND_SIZE.width, Constant.WND_SIZE.height);
    }


    public static void drawCenterText(Graphics2D g, Font font, Color color, String text) {

        g.setFont(font);
        g.setColor(color);

        Rectangle2D bd = font.getStringBounds(text, g.getFontRenderContext());

        int drawX = (Constant.WND_SIZE.width - (int) bd.getWidth()) >> 1;
        int drawY = (Constant.WND_SIZE.height + (int) bd.getHeight()) >> 1;

        g.drawString(text, drawX, drawY);
    }
}
