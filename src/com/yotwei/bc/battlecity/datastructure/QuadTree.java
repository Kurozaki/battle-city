package com.yotwei.bc.battlecity.datastructure;


import java.awt.*;
import java.util.Set;

/**
 * Created by YotWei on 2018/5/21.
 */
public interface QuadTree<E extends Rectangle> {

    /**
     * add specify element to the quad tree, element object must extend from Rectangle
     *
     * @param e the element to be added to the quad tree, must extends Rectangle
     * @return return true if add successfully
     */
    boolean add(E e);

    /**
     * destroy specify element from the quad tree, element object must extend from Rectangle
     *
     * @param e the element to be destroyed from the quad tree, must extends Rectangle
     * @return return true if element is from the quad tree
     */
    boolean destroy(E e);

    /**
     * retrieve all elements intersect with the specify area
     *
     * @param retArea the specify area (retrieve area)
     * @return element set intersect with the specify area
     */
    Set<E> retrieve(Rectangle retArea);
}
