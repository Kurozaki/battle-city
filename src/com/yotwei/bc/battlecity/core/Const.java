package com.yotwei.bc.battlecity.core;


import java.awt.*;
import java.net.URL;

/**
 * Created by YotWei on 2018/6/12.
 */
public class Const {

    private static final int COLUMN_COUNT = 20, ROW_COUNT = 16;

    public static final Dimension SIZE_UNIT = new Dimension(16, 16);

    public static final Dimension SIZE_UNIT_2X = new Dimension(SIZE_UNIT.width << 1, SIZE_UNIT.height << 1);

    public static final Dimension SIZE_VIEWPORT = new Dimension(
            SIZE_UNIT.width * (COLUMN_COUNT << 1),
            SIZE_UNIT.height * (ROW_COUNT << 1));

    public static URL ROOT_DIR_URL = Const.class.getClassLoader().getResource("res");
}
