package com.yotwei.battlecity.game.datastruct;

import com.yotwei.battlecity.game.object.GameObject;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by YotWei on 2019/2/28.
 */
public class DefaultGameObjectGroup<_ObjectType extends GameObject>
        implements IGameObjectGroup<_ObjectType> {

    private Set<_ObjectType> objectSet;

    public DefaultGameObjectGroup() {
        objectSet = new HashSet<>();
    }

    @Override
    public boolean add(_ObjectType anObject) {
        return objectSet.add(anObject);
    }

    @Override
    public int each(Consumer<_ObjectType> consumer) {
        objectSet.forEach(consumer);
        return objectSet.size();
    }
}
