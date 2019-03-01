package com.yotwei.battlecity.framework;

/**
 * Created by YotWei on 2019/2/25.
 */
public class KeyInput {

    private static final int KEY_COUNT = 0x100;

    private static boolean[] keyRecArr = new boolean[KEY_COUNT];

    private static boolean checkCodeRange(int keycode) {
        return 0 < keycode && keycode < KEY_COUNT;
    }

    static void setKeyStatus(int keycode, boolean status) {
        if (!checkCodeRange(keycode)) {
            return;
        }

        if (keyRecArr[keycode] != status) {
            keyRecArr[keycode] = status;
        }
    }

    public static boolean isKeyPressed(int keycode) {
        return checkCodeRange(keycode) && keyRecArr[keycode];
    }
}
