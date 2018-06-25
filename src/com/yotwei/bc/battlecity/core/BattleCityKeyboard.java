package com.yotwei.bc.battlecity.core;

/**
 * Created by YotWei on 2018/6/12.
 */
public enum BattleCityKeyboard {

    INSTANCE;

    private boolean[] keyPressRecs;

    BattleCityKeyboard() {
        keyPressRecs = new boolean[0x100];
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean keyCodeInRange(int keyCode) {
        return 0 <= keyCode && keyCode < keyPressRecs.length;
    }

    public void press(int keyCode) {
        if (!keyCodeInRange(keyCode))
            return;
        if (!keyPressRecs[keyCode]) {
//            Debugger.Log.info("press: " + keyCode);
            keyPressRecs[keyCode] = true;
        }
    }

    public void release(int keyCode) {
        if (!keyCodeInRange(keyCode))
            return;
        if (keyPressRecs[keyCode]) {
//            Debugger.Log.info("release: " + keyCode);
            keyPressRecs[keyCode] = false;
        }
    }

    public boolean isKeyPressing(int keyCode) {
        return keyCodeInRange(keyCode) && keyPressRecs[keyCode];
    }
}
