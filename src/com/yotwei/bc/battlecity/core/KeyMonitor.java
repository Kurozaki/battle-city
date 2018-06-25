package com.yotwei.bc.battlecity.core;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by YotWei on 2018/6/12.
 */
public class KeyMonitor extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        BattleCityKeyboard.INSTANCE.press(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        BattleCityKeyboard.INSTANCE.release(e.getKeyCode());
    }
}
