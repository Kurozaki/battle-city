package com.yotwei.battlecity.game.components.tank;

import com.yotwei.battlecity.game.components.GOComponent;
import com.yotwei.battlecity.game.objects.prefabs.tanks.Tank;
import com.yotwei.battlecity.game.objects.properties.Direction;
import com.yotwei.battlecity.game.objects.properties.buff.Buff;

import java.awt.*;

/**
 * Created by YotWei on 2019/3/30.
 * <p>
 * 坦克用的组件，作用如下
 * 1、用于决定坦克下一个移动方向
 * 2、决定坦克的移动距离
 */
@SuppressWarnings("WeakerAccess")
public abstract class TankMotion<_TankType extends Tank> implements GOComponent {

    /**
     * 与实例关联的 Tank 对象
     */
    protected final _TankType tank;

    protected final int speedBase;
    private int speedSub;

    protected Direction facingDirection;
    protected Direction moveDirection;

    public TankMotion(_TankType tank, int speedBase) {
        this.tank = tank;
        this.speedBase = speedBase;
    }

    @Override
    public String name() {
        return "TankMotion";
    }

    /**
     * 决定物体的下一个移动方向
     * 结果存放在 direction 成员上
     */
    public abstract void calcNextDirection();

    /**
     * 坦克的移动速度是可变的，故封装成函数
     *
     * @return 移动速度的值
     */
    protected int calcMoveSpeed() {
        float speedFactory = 1.0f;  // 速度因子，最终速度= baseSpeed * speedFactory

        TankBuffContainer buffs = tank.getComponent("BuffContainer");
        // 加速 buff 检测
        final Buff<Float> speedupBuff = buffs.getBuff(Buff.TYPE_MOVE_SPEED_UP);
        if (speedupBuff != null) {
            speedFactory += speedupBuff.getValue();
        }

        // 减速 buff 检测
        final Buff<Float> slowDownBuff = buffs.getBuff(Buff.TYPE_SLOW_DOWN);
        if (slowDownBuff != null) {
            speedFactory -= slowDownBuff.getValue();
        }

        return (int) (speedBase * speedFactory);
    }

    /**
     * 决定物体下一步所移动的距离
     *
     * @return 移动距离单位为像素
     */
    public int getMoveDist() {
        speedSub += calcMoveSpeed();
        int md = speedSub >> 8;
        speedSub &= 0xff;
        return md;
    }

    /**
     * 获取坦克移动朝向
     * null 表示坦克当前静止
     */
    public Direction getMoveDirection() {
        return moveDirection;
    }

    /**
     * 获取坦克的朝向
     * 即使静止也有朝向
     */
    public Direction getFacingDirection() {
        if (moveDirection != null)
            facingDirection = moveDirection;
        return facingDirection;
    }

    protected _TankType getTank() {
        return tank;
    }
}
