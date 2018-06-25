package com.yotwei.bc.battlecity.datastructure;

import com.yotwei.bc.battlecity.level.obj.EntityObject;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by YotWei on 2018/6/17.
 */
public class EntityList<E extends EntityObject> {

    private final ArrayList<E> _list = new ArrayList<>();

    public boolean add(E e) {
        return this._list.add(e);
    }

    public void updateEntities() {
        int destroyedCount = 0;
        for (E e : _list) {
            if (e.isActivate())
                e.update();
            else
                destroyedCount++;
        }
        if (destroyedCount > (_list.size() >> 1))
            _list.removeIf(e -> !e.isActivate());
    }

    public int size() {
        return _list.size();
    }

    public void drawEntities(Graphics2D g) {
        for (E e : _list) {
            if (e.isActivate())
                e.draw(g);
        }
    }

    public ArrayList<E> toArrayList() {
        return _list;
    }
}