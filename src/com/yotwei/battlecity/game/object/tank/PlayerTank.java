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

    private int speedBase;
    private int bulletSlotBase;

    private int freezeTicker = 0;

    public PlayerTank(LevelContext lvlCtx, int playerId) {
        super(lvlCtx);

        this.playerId = playerId;

        speedBase = 0x120;
        bulletSlotBase = 2;
        tankDurability = 100;

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
        float factory = 1.0f + (float) extra.getOrDefault("speedUp", 0.0f);
        if (freezeTicker > 0) {
            factory *= 0.2f;
        }
        return (int) (factory * speedBase);
    }

    public void setControlKeys(ControlKeys controlKeys) {
        this.controlKeys = controlKeys;
    }

    public ControlKeys getControlKeys() {
        return controlKeys;
    }

    @Override
    public void onActive() {
        // set tag
        setTag("Tank-Player");

        super.onActive();
    }

    @Override
    public void update() {
        super.update();

        boolean isAddLiveCount = extra.containsKey("addLiveCount");
        if (isAddLiveCount) {
            getLevelContext().triggerEvent(LevelContext.Event.wrap("addPlayerLiveCount", playerId));
            extra.remove("addLiveCount");
        }

        boolean isFrozen = extra.containsKey("freeze");
        if (isFrozen) {
            extra.remove("freeze");
            freezeTicker = 60;
        }

        if (freezeTicker > 0) freezeTicker--;
    }

    @Override
    public void onInactive() {
        super.onInactive();

        LevelContext.Event ev = LevelContext.Event.wrap("playerDeath", playerId);
        getLevelContext().triggerEvent(ev);
    }

    public int getBulletSlotsCount() {
        return bulletSlotBase +
                (int) extra.getOrDefault("addBulletSlot", 0);
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
