package com.yotwei.bc.battlecity.datastructure;

import com.yotwei.bc.battlecity.level.obj.EntityObject;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by YotWei on 2018/6/5.
 * <p>
 * full quad tree for entity object
 */
public class FullQuadTree<E extends EntityObject> implements QuadTree<E> {

    private final int splitCellCount = 2 << 2;
    private Rectangle bound;

    private Map<Point, Set<E>> elementsSetMap = new HashMap<>();
    private List<E> listCache = null;

    public FullQuadTree(Rectangle bound) {
        this.bound = bound;
    }

    @Override
    public boolean add(E e) {
        if (!bound.intersects(e) || e.isEmpty())
            return false;
        for (Point point : getIndex2dSet(e)) {
            if (!isIndex2dInbound(point)) {
                continue;
            }
            if (!elementsSetMap.containsKey(point))
                elementsSetMap.put(point, new HashSet<>());
            elementsSetMap.get(point).add(e);
        }
        return true;
    }

    @Override
    public boolean destroy(E e) {
        return false;
    }

    @Override
    public Set<E> retrieve(Rectangle retArea) {
        Set<E> retSet = new HashSet<>();
        Set<Point> index2dSet = getIndex2dSet(retArea);
        for (Point point : index2dSet) {
            if (elementsSetMap.containsKey(point))
                retSet.addAll(elementsSetMap.get(point));
        }
        return retSet;
    }

    private List<E> toList() {
        if (listCache == null) {
            // user hash set to avoid adding duplicate elements
            Set<E> set = new HashSet<>();
            for (Set<E> eSet : elementsSetMap.values())
                set.addAll(eSet);
            listCache = new ArrayList<>(set);
        }
        return listCache;
    }

    public void drawEntities(Graphics2D g) {
        for (E e : toList()) {
            if (e.isActivate())
                e.draw(g);
        }
    }

    private Set<Point> getIndex2dSet(Rectangle r) {
        Set<Point> pointSet = new HashSet<>();
        pointSet.add(getIndex2d(r.x, r.y));
        pointSet.add(getIndex2d(r.x + r.width - 1, r.y));
        pointSet.add(getIndex2d(r.x, r.y + r.height - 1));
        pointSet.add(getIndex2d(r.x + r.width - 1, r.y + r.height - 1));
        return pointSet;
    }

    private Point getIndex2d(int x, int y) {
        return new Point(x / (bound.width / splitCellCount),
                y / (bound.height / splitCellCount));
    }

    private boolean isIndex2dInbound(Point p) {
        return p.x >= 0 && p.x < splitCellCount &&
                p.y >= 0 && p.y < splitCellCount;
    }
}