package com.yotwei.battlecity.game.object.properties;

/**
 * Created by YotWei on 2019/3/2.
 */
public interface LifeCycle {

    void onActive();

    void update();

    void onInactive();
}
