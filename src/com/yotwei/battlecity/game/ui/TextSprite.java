package com.yotwei.battlecity.game.ui;


import com.yotwei.battlecity.util.Constant;

import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * Created by YotWei on 2019/4/6.
 */
@SuppressWarnings("WeakerAccess")
public abstract class TextSprite extends UIObject {

    public static final int DRAW_COORD_CENTER = -1;

    protected Font font;
    protected String text;
    protected Shape textShape;

    void calcTextShape(String text, Font font, Graphics2D g) {
        TextLayout layout = new TextLayout(text, font, g.getFontRenderContext());

        double x = drawCoordinate.x;
        double y = drawCoordinate.y;

        if (DRAW_COORD_CENTER == x || DRAW_COORD_CENTER == y) {
            Rectangle2D textBound = layout.getBounds();
            if (DRAW_COORD_CENTER == x) x = (Constant.WND_SIZE.width - textBound.getWidth()) / 2;
            if (DRAW_COORD_CENTER == y) y = (Constant.WND_SIZE.height - textBound.getHeight()) / 2;
        }

        textShape = layout.getOutline(AffineTransform.getTranslateInstance(x, y));
    }

    /**
     * 改变坐标
     */
    @Override
    public void setDrawCoordinate(int x, int y) {
        super.setDrawCoordinate(x, y);
        textShape = null;
    }

    /**
     * 改变文本内容
     */
    public void setText(String text) {
        this.text = text;
        textShape = null;
    }

    public static TextSprite createOutlineTextSprite(
            Font font,
            Color textColor,
            Color outlineColor) {
        return new OutlineTextSprite(font, textColor, outlineColor, "");
    }

    private static class OutlineTextSprite extends TextSprite {

        private Color textColor, outlineColor;
        private Stroke stroke = new BasicStroke(2);

        private OutlineTextSprite(Font font, Color textColor, Color outlineColor, String text) {
            this.text = text;
            this.font = font;
            this.textColor = textColor;
            this.outlineColor = outlineColor;
        }

        @Override
        public void draw(Graphics2D g) {

            if ("".equals(text)) {
                return;
            }

            if (textShape == null) {
                calcTextShape(text, font, g);
            }
            g.setStroke(stroke);

            g.setColor(outlineColor);
            g.draw(textShape);

            g.setColor(textColor);
            g.fill(textShape);
        }
    }
}
