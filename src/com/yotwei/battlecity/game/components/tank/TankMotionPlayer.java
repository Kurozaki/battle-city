package com.yotwei.battlecity.game.components.tank;

import com.yotwei.battlecity.framework.KeyInput;
import com.yotwei.battlecity.game.objects.prefabs.tanks.PlayerTank;
import com.yotwei.battlecity.game.objects.properties.Direction;
import com.yotwei.battlecity.game.objects.properties.buff.Buff;

import java.awt.event.KeyEvent;

/**
 * Created by YotWei on 2019/3/30.
 * <p>
 * 玩家的坦克移动规则
 */
public class TankMotionPlayer extends TankMotion<PlayerTank> {

    public TankMotionPlayer(PlayerTank tank) {
        super(tank, 0x100);
        facingDirection = Direction.UP;
    }

    /**
     * 玩家的下一个方向取决于键盘输入
     */
    @Override
    public void calcNextDirection() {

        boolean up = KeyInput.isKeyPressed(KeyEvent.VK_W);
        boolean down = KeyInput.isKeyPressed(KeyEvent.VK_S);

        boolean left = KeyInput.isKeyPressed(KeyEvent.VK_A);
        boolean right = KeyInput.isKeyPressed(KeyEvent.VK_D);

        if (up ^ down) {
            if (up)
                moveDirection = Direction.UP;
            else
                moveDirection = Direction.DOWN;
        } else if (left ^ right) {
            if (left)
                moveDirection = Direction.LEFT;
            else
                moveDirection = Direction.RIGHT;
        } else
            moveDirection = null;
    }
}
