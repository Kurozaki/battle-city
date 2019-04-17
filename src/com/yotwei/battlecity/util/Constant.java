package com.yotwei.battlecity.util;

import java.awt.*;
import java.net.URL;

/**
 * Created by YotWei on 2019/2/25.
 */
public class Constant {

    public static final Dimension WND_SIZE = new Dimension(640, 480);

    public static final Dimension UNIT_SIZE = new Dimension(16, 16);
    public static final Dimension UNIT_SIZE_HALF = new Dimension(8, 8);
    public static final Dimension UNIT_SIZE_2X = new Dimension(32, 32);

    public static final URL RES_PATH = Constant.class.getClassLoader().getResource("res");
}
