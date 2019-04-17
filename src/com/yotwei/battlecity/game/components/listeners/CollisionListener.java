package com.yotwei.battlecity.game.components.listeners;

import com.yotwei.battlecity.game.components.GOListener;
import com.yotwei.battlecity.game.objects.GameObject;
import com.yotwei.battlecity.util.GameObjects;

import java.awt.*;

/**
 * Created by YotWei on 2019/3/31.
 */
public abstract class CollisionListener<_ObjectType> implements GOListener {

    protected final _ObjectType gameObject;

    protected CollisionListener(_ObjectType gameObject) {
        this.gameObject = gameObject;
    }

    @Override
    public String name() {
        return "CollisionListener";
    }

    public abstract void onTouchBound(Rectangle boundary);

    public abstract void onCollide(GameObject anotherObject);
}
