package com.yotwei.battlecity.game.datastruct;

import com.yotwei.battlecity.game.object.GameObject;

import java.util.function.Consumer;

/**
 * Created by YotWei on 2019/2/28.
 */
public interface IGameObjectGroup<_ObjectType extends GameObject> {

    boolean add(_ObjectType anObject);

    int each(Consumer<_ObjectType> consumer);
}
