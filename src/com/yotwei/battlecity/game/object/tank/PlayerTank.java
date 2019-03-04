package com.yotwei.battlecity.game.object.tank;

import com.yotwei.battlecity.framework.KeyInput;
import com.yotwei.battlecity.game.engine.ResourcePackage;
import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.tank.behavior.ITankBehavior;

/**
 * Created by YotWei on 2019/3/1.
 */
public class PlayerTank extends AbstractTank {

    private ControlKeys controlKeys;

    public PlayerTank(LevelContext lvlCtx, int playerId) {
        super(lvlCtx);

        // get image resource according to player id
        image = ResourcePackage.getImage("player-" + playerId);

        // get moving behavior strategy
        behavior = ITankBehavior.player(this);
    }


    /**
     * -------------------------------------------------------------------------------------
     * <p>
     * method implements from {@link AbstractTank}
     * <p>
     * -------------------------------------------------------------------------------------
     */
    @Override
    protected int calcMoveSpeed() {
        return 0x180;
    }

    public void setControlKeys(ControlKeys controlKeys) {
        this.controlKeys = controlKeys;
    }

    public ControlKeys getControlKeys() {
        return controlKeys;
    }

    /**
     * key controller records of tank
     */
    public static class ControlKeys {

        private final int up, down, left, right;
        private final int projA, projB;

        public ControlKeys(int up, int down, int left, int right, int projA, int projB) {
            this.up = up;
            this.down = down;
            this.left = left;
            this.right = right;
            this.projA = projA;
            this.projB = projB;
        }

        public boolean isUp() {
            return KeyInput.isKeyPressed(up);
        }

        public boolean isDown() {
            return KeyInput.isKeyPressed(down);
        }

        public boolean isLeft() {
            return KeyInput.isKeyPressed(left);
        }

        public boolean isRight() {
            return KeyInput.isKeyPressed(right);
        }

        public boolean isProjA() {
            return KeyInput.isKeyPressed(projA);
        }

        public boolean isProjB() {
            return KeyInput.isKeyPressed(projB);
        }
    }
}
