package com.yotwei.battlecity.game.datastruct;

import com.yotwei.battlecity.game.object.GameObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by YotWei on 2019/3/3.
 * <p>
 * Grid is a special quad-tree
 */
public class GridGameObjectGroup<_ObjectType extends GameObject>
        implements IGameObjectGroup<_ObjectType> {

    private final Set[][] objectsGrid;
    private final Set<_ObjectType> objectSet;

    private final Rectangle boundary;
    private final int horizontalDivision, verticalDivision;

    private final Dimension gridUnitSize;

    GridGameObjectGroup(Rectangle bound, int horizontalDiv, int verticalDiv) {
        this.boundary = bound;

        // calculate grid unit size
        gridUnitSize = new Dimension(
                boundary.width / horizontalDiv,
                boundary.height / verticalDiv);

        if (gridUnitSize.width * horizontalDiv < bound.width)
            this.horizontalDivision = horizontalDiv + 1;
        else
            this.horizontalDivision = horizontalDiv;

        if (gridUnitSize.height * verticalDiv < bound.height)
            this.verticalDivision = verticalDiv + 1;
        else
            this.verticalDivision = verticalDiv;

        objectsGrid = new Set[verticalDiv][horizontalDiv];
        objectSet = new HashSet<>();
    }

    // TODO: 2019/3/4 完善格子算法

    @Override
    public boolean add(_ObjectType anObject) {
        return false;
    }

    @Override
    public int each(Consumer<_ObjectType> consumer) {
        return 0;
    }

    @Override
    public Set<_ObjectType> retrieve(Rectangle retArea) {
        return new HashSet<>();
    }

    private IndexRect calcIndexRect(Rectangle area) {
        IndexRect ir = new IndexRect();
        ir.x1 = (area.x - boundary.x) / gridUnitSize.width;
        ir.x2 = ((area.x + area.width) - (boundary.x + boundary.width - 1)) / gridUnitSize.width;
        ir.y1 = (area.y - boundary.y) / gridUnitSize.height;
        ir.y2 = ((area.y + area.height) - (boundary.y + boundary.height - 1)) / gridUnitSize.height;
        return ir;
    }

    private class IndexRect {

        int x1, x2, y1, y2;

        void itr(Consumer<Set<GameObject>> c) {
            for (int y = y1; y <= y2; y++) {
                for (int x = x1; x <= x2; x++) {
                    c.accept(objectsGrid[y][x]);
                }
            }
        }
    }
}
