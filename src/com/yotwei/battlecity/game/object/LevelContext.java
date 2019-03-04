package com.yotwei.battlecity.game.object;

import java.awt.*;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by YotWei on 2019/2/28.
 * <p>
 * level context is a environment of {@link GameObject}
 * providing some necessary method
 */
public interface LevelContext {

    Set<GameObject> retrieveGameObject(
            Rectangle retArea,
            Set<String> retrieveGroupNames,
            RetrieveFilter<GameObject> filter);

    Rectangle getLevelBound();

    /**
     * frameTicker is a clock in a level
     * promise that it's an integer increases frame by frame in implement class
     *
     * @return value of frame ticker
     */
    int getFrameTicker();

    interface RetrieveFilter<_ObjectType extends GameObject> {

        boolean filter(_ObjectType anObject);
    }
}
