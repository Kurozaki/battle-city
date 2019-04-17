package com.yotwei.battlecity.game.ui;

import java.awt.*;

/**
 * Created by YotWei on 2019/4/6.
 */
public abstract class UIObject {

    /**
     * UI 绘制的位置，如果 -1 表示居中
     */
    protected final Point drawCoordinate;

    UIObject() {
        this.drawCoordinate = new Point();
    }

    public abstract void draw(Graphics2D g);

    public void setDrawCoordinate(int x, int y) {
        drawCoordinate.setLocation(x, y);
    }
}
