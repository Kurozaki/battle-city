package com.yotwei.battlecity.game.datastruct;

import com.yotwei.battlecity.game.object.GameObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by YotWei on 2019/3/3.
 * <p>
 * Grid is a special quad-tree
 */
public class GridGameObjectGroup<_ObjectType extends GameObject>
        implements IGameObjectGroup<_ObjectType> {

    private final Array2D<Set<_ObjectType>> array2d;
    private final Set<_ObjectType> objectSet;

    private final Rectangle boundary;

    private final Dimension gridUnitSize;

    GridGameObjectGroup(Rectangle bound, int row, int col) {
        this.boundary = bound;

        // calculate grid unit size
        gridUnitSize = new Dimension(boundary.width / col, boundary.height / row);

        if (gridUnitSize.width * col < bound.width) col++;
        if (gridUnitSize.height * row < bound.height) row++;

        array2d = new Array2D<>(row, col);
        objectSet = new HashSet<>();
    }

    @Override
    public boolean add(_ObjectType anObject) {
        if (!boundary.intersects(anObject.getHitbox())
                || objectSet.contains(anObject)) {
            return false;
        }

        calcIndexRect(anObject.getHitbox()).itr(aGridObjectSet -> aGridObjectSet.add(anObject));
        objectSet.add(anObject);

        return true;
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
                calcIndexRect(anObject.getHitbox()).itr(aGridObjectSet -> {
                    aGridObjectSet.remove(anObject);
                });
            }
        }
        return objectSet.size();
    }

    @Override
    public Set<_ObjectType> retrieve(Rectangle retArea) {

        Set<_ObjectType> resultSet = new HashSet<>();

        calcIndexRect(retArea).itr(aGridObjectSet -> {
            for (_ObjectType anObject : aGridObjectSet) {
                if (!anObject.getHitbox().intersects(retArea)) {
                    continue;
                }
                resultSet.add(anObject);
            }
        });

        return resultSet;
    }

    private IndexRect calcIndexRect(Rectangle area) {
        IndexRect ir = new IndexRect();
        ir.x1 = (area.x - boundary.x) / gridUnitSize.width;
        ir.x2 = (area.x + area.width - 1 - boundary.x) / gridUnitSize.width;
        ir.y1 = (area.y - boundary.y) / gridUnitSize.height;
        ir.y2 = (area.y + area.height - 1 - boundary.y) / gridUnitSize.height;
        return ir;
    }

    private class IndexRect {

        int x1, x2, y1, y2;

        void itr(Consumer<Set<_ObjectType>> c) {

            for (int y = y1; y <= y2; y++) {
                for (int x = x1; x <= x2; x++) {

                    if (!array2d.isInRange(y, x))
                        continue;

                    if (array2d.get(y, x) == null)
                        array2d.set(y, x, new HashSet<>());

                    c.accept(array2d.get(y, x));
                }
            }

        }

    }
}
