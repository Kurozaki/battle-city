package com.yotwei.battlecity.game.datastruct;

import com.yotwei.battlecity.game.objects.GameObject;
import com.yotwei.battlecity.util.GameObjects;

import java.awt.*;
import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by YotWei on 2019/3/23.
 */
public class GridGameObjectGroup implements IGameObjectGroup {

    private final Dimension unitSize;
    private final Rectangle boundary;

    private final Array2D<Set<GameObject>> grid;
    private final Set<GameObject> set;


    public GridGameObjectGroup(Rectangle boundary, int col, int row) {
        this.boundary = boundary;
        unitSize = new Dimension(boundary.width / col, boundary.height / row);

        if (unitSize.width * col < boundary.width) col++;
        if (unitSize.height * row < boundary.height) row++;

        grid = new Array2D<>(row, col);
        for (int r = 0; r < grid.getRowCount(); r++) {
            for (int c = 0; c < grid.getColCount(); c++) {
                grid.set(r, c, new HashSet<>());
            }
        }

        set = new HashSet<>();
    }

    @Override
    public boolean add(GameObject go) {
        if (set.contains(go)) {
            return false;
        }
        calcAccessRange(GameObjects.boundingBox(go))
                .each(aSet -> aSet.add(go));
        return set.add(go);
    }

    @Override
    public boolean remove(GameObject go) {
        if (!set.contains(go)) {
            return false;
        }
        calcAccessRange(GameObjects.boundingBox(go)).each(aSet -> aSet.remove(go));
        return set.remove(go);
    }

    @Override
    public Set<GameObject> retrieve(Rectangle retrieveArea) {
        Set<GameObject> resultSet = new HashSet<>();
        calcAccessRange(retrieveArea).each(aSet -> aSet.forEach(anObject -> {
            if (anObject.isActive() &&
                    GameObjects.boundingBox(anObject).intersects(retrieveArea)) {
                resultSet.add(anObject);
            }
        }));
        return resultSet;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void each(Consumer<GameObject> consumer) {
        Iterator<GameObject> itr = set.iterator();
        while (itr.hasNext()) {
            GameObject next = itr.next();
            if (next.isActive()) {
                consumer.accept(next);
            } else {
                itr.remove();

                // 还需要从格子中删除
                calcAccessRange(GameObjects.boundingBox(next))
                        .each(aGridSet -> aGridSet.remove(next));
            }
        }
    }

    @Override
    public int size() {
        return set.size();
    }

    private AccessRange calcAccessRange(Rectangle rc) {
        rc = rc.intersection(boundary);
        return new AccessRange(
                rc.x / unitSize.width,
                rc.y / unitSize.height,
                (rc.x + rc.width - 1) / unitSize.width,
                (rc.y + rc.height - 1) / unitSize.height
        );
    }

    private class AccessRange {

        final int fromX, fromY, toX, toY;

        AccessRange(int fromX, int fromY, int toX, int toY) {
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }

        void each(Consumer<Set<GameObject>> c) {
            for (int x = fromX; x <= toX; x++) {
                for (int y = fromY; y <= toY; y++) {
                    c.accept(grid.get(y, x));
                }
            }
        }
    }
}
