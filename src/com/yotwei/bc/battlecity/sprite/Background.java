package com.yotwei.bc.battlecity.sprite;

import com.yotwei.bc.battlecity.core.Const;

import java.awt.*;

/**
 * Created by YotWei on 2018/6/18.
 */
public class Background {

    public void draw(Graphics2D gg) {
        gg.setColor(Color.BLACK);
        gg.fillRect(0, 0, Const.SIZE_VIEWPORT.width, Const.SIZE_VIEWPORT.height);
    }
}
