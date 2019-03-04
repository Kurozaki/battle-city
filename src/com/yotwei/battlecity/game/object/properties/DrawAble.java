package com.yotwei.battlecity.game.object.properties;

import java.awt.*;
import java.util.Comparator;

/**
 * Created by YotWei on 2019/3/2.
 */
public interface DrawAble {

    void draw(Graphics2D g);

    int getDrawPriority();

    Comparator<DrawAble> DrawPriorityComparator =
            Comparator.comparingInt(DrawAble::getDrawPriority);
}
