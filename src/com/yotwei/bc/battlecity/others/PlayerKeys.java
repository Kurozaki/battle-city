package com.yotwei.bc.battlecity.others;

import com.yotwei.bc.battlecity.core.BattleCityKeyboard;

import java.awt.event.KeyEvent;
import java.security.Key;

/**
 * Created by YotWei on 2018/6/18.
 */
public class PlayerKeys {

    public static final PlayerKeys P1_KEYS = new PlayerKeys(
            KeyEvent.VK_W,
            KeyEvent.VK_S,
            KeyEvent.VK_A,
            KeyEvent.VK_D,
            KeyEvent.VK_L);

    private final int up, down, left, right, project;

    private PlayerKeys(int up, int down, int left, int right, int project) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.project = project;
    }

    public boolean isDownActivate() {
        return BattleCityKeyboard.INSTANCE.isKeyPressing(down);
    }

    public boolean isUpActivate() {
        return BattleCityKeyboard.INSTANCE.isKeyPressing(up);
    }

    public boolean isLeftActivate() {
        return BattleCityKeyboard.INSTANCE.isKeyPressing(left);
    }

    public boolean isRightActivate() {
        return BattleCityKeyboard.INSTANCE.isKeyPressing(right);
    }

    public boolean isProjectActivate() {
        return BattleCityKeyboard.INSTANCE.isKeyPressing(project);
    }
}