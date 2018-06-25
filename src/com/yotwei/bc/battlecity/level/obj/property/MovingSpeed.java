package com.yotwei.bc.battlecity.level.obj.property;

/**
 * Created by YotWei on 2018/6/17.
 */
public class MovingSpeed {

    private int speed;
    private int mpx;    // moving pixel

    public void setSpeed(int speed) {
        if (speed < 0)
            throw new IllegalArgumentException("Illegal speed: " + speed);
        this.speed = speed;
    }

    public int getMovingPixel() {
        mpx = (mpx & 0xff) + speed;
        return mpx >> 8;
    }

}
