package com.yotwei.bc.battlecity.level.obj;

import com.yotwei.bc.battlecity.core.Const;
import com.yotwei.bc.battlecity.level.lvl.Level;
import com.yotwei.bc.battlecity.level.obj.property.Type;

/**
 * Created by YotWei on 2018/6/17.
 */
public abstract class EntityObject extends AbstractObject {

    /**
     * debug flag for object
     */
    public int debugFlag = 0;

    private static final int DEFAULT_DURABILITY = 1;

    private int durability = DEFAULT_DURABILITY;
    private boolean isActivate = true;
    private Type type = Type.DEFAULT;

    protected EntityObject(Level level) {
        super(level);
        this.setSize(Const.SIZE_UNIT);
    }

    public void incDurability(int inc) {
        this.durability += inc;
    }

    public void decDurability(int dec) {
        this.durability -= dec;
        if (this.durability <= 0) {
            this.destroySelf();
        }
    }

    protected void setDurability(int durability) {
        this.durability = durability;
    }

    public int getDurability() {
        return durability;
    }

    public boolean isActivate() {
        return isActivate;
    }

    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        this.type = type;
    }

    protected void destroySelf() {
        this.isActivate = false;
    }
}
