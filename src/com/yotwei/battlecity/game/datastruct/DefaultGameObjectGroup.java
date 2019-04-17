package com.yotwei.battlecity.game.datastruct;

import com.yotwei.battlecity.game.objects.GameObject;
import com.yotwei.battlecity.util.GameObjects;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by YotWei on 2019/3/18.
 */
public class DefaultGameObjectGroup implements IGameObjectGroup {

    private Set<GameObject> set;

    public DefaultGameObjectGroup() {
        set = new HashSet<>();
    }

    @Override
    public boolean add(GameObject go) {
        return set.add(go);
    }

    @Override
    public boolean remove(GameObject go) {
        return set.remove(go);
    }

    @Override
    public Set<GameObject> retrieve(Rectangle retrieveArea) {
        Set<GameObject> resultSet = new HashSet<>();

        // 集合逐个遍历，然后找到与矩形区域有交集的物体
        for (GameObject anObject : set) {
            if (anObject.isActive() &&
                    GameObjects.boundingBox(anObject).intersects(retrieveArea)) {
                resultSet.add(anObject);
            }
        }
        return resultSet;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void each(Consumer<GameObject> consumer) {
        Iterator<GameObject> itr = set.iterator();

        while (itr.hasNext()) {
            GameObject go = itr.next();
            if (go.isActive()) {
                consumer.accept(go);
            } else {
                itr.remove();
            }
        }
    }

    @Override
    public int size() {
        return set.size();
    }
}
