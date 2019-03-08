package com.yotwei.battlecity.game.object.tank;

import com.yotwei.battlecity.framework.KeyInput;
import com.yotwei.battlecity.game.engine.ResourcePackage;
import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.tank.behavior.AbstractTankBulletProjection;
import com.yotwei.battlecity.game.object.tank.behavior.AbstractTankMovement;

/**
 * Created by YotWei on 2019/3/1.
 */
public class PlayerTank extends AbstractTank {

    private ControlKeys controlKeys;

    private final int playerId;

    public PlayerTank(LevelContext lvlCtx, int playerId) {
        super(lvlCtx);

        this.playerId = playerId;

        // get image resource according to player id
        image = ResourcePackage.getImage("player-" + playerId);

        // get moving strategy
        tankMovement = AbstractTankMovement.player(this);

        // get bullet project strategy
        tankBulletProj = AbstractTankBulletProjection.player(this);
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

    @Override
    public int tryDamage(int damageValue) {
        // TODO: 2019/3/8 完善子弹撞击逻辑
        setActive(false);

        LevelContext.Event ev = LevelContext.Event.wrap("playerDeath", playerId);
        getLevelContext().triggerEvent(ev);

        return damageValue;
//        return 0;
    }

    /**
     * key controller records of tank
     */
    public static class ControlKeys {

        private final int up, down, left, right;
        private final int projA, projB;

        private ControlKeys(int up, int down, int left, int right, int projA, int projB) {
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

        public static ControlKeys createByPlayerId(int pid) {
            return new ControlKeys(
                    'W',
                    'S',
                    'A',
                    'D',
                    'K',
                    'L'
            );
        }
    }
}
