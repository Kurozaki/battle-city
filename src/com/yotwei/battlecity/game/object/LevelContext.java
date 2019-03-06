package com.yotwei.battlecity.game.object;

import java.awt.*;
import java.util.Set;

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

    void triggerEvent(Event ev);

    interface RetrieveFilter<_ObjectType extends GameObject> {

        boolean filter(_ObjectType anObject);
    }

    class Event {

        public final String evTag;
        public final Object[] dat;

        private Event(String evTag, Object... dat) {
            this.evTag = evTag;
            this.dat = dat;
        }

        public static Event wrap(String evTag, Object... dat) {
            return new Event(evTag, dat);
        }
    }
}
