package com.yotwei.bc.battlecity.level.obj;

import com.yotwei.bc.battlecity.level.lvl.Level;
import com.yotwei.bc.battlecity.level.obj.property.Direction;
import com.yotwei.bc.battlecity.level.obj.property.MovingSpeed;

/**
 * Created by YotWei on 2018/6/17.
 */
public abstract class MovableObject extends EntityObject {

    protected final MovingSpeed movsp = new MovingSpeed();
    protected Direction direction = Direction.UP;

    protected MovableObject(Level level) {
        super(level);
    }

    public Direction getDirection() {
        return direction;
    }

}
