package com.yotwei.battlecity.game.datastruct;

import com.yotwei.battlecity.game.object.GameObject;
import com.yotwei.battlecity.game.object.special.TankCreator;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by YotWei on 2019/2/28.
 */
public class DefaultGameObjectGroup<_ObjectType extends GameObject>
        implements IGameObjectGroup<_ObjectType> {

    private Set<_ObjectType> objectSet;

    DefaultGameObjectGroup() {
        objectSet = new HashSet<>();
    }

    @Override
    public boolean add(_ObjectType anObject) {
        return objectSet.add(anObject);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public int each(Consumer<_ObjectType> consumer) {

        Iterator<_ObjectType> itr = objectSet.iterator();
        while (itr.hasNext()) {
            _ObjectType anObject = itr.next();
            if (anObject.isActive()) {
                consumer.accept(anObject);
            } else {
                itr.remove();
            }
        }

        return objectSet.size();
    }

    @Override
    public Set<_ObjectType> retrieve(Rectangle retArea) {
        Set<_ObjectType> resultSet = new HashSet<>();
        for (_ObjectType anObject : objectSet) {
            if (anObject.isActive() && anObject.getHitbox().intersects(retArea))
                resultSet.add(anObject);
        }
        return resultSet;
    }

}
