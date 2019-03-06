package com.yotwei.battlecity.game.datastruct;

import com.yotwei.battlecity.game.object.GameObject;
import com.yotwei.battlecity.util.Constant;

import java.awt.*;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by YotWei on 2019/2/28.
 */
public interface IGameObjectGroup<_ObjectType extends GameObject> {

    boolean add(_ObjectType anObject);

    int each(Consumer<_ObjectType> consumer);

    Set<_ObjectType> retrieve(Rectangle retArea);

    static <_ObjectType extends GameObject> IGameObjectGroup<_ObjectType> create(String datastruct) {
//        if (datastruct.equals("Grid"))
//            return new GridGameObjectGroup<>(
//                    new Rectangle(Constant.WND_SIZE),
//                    8,
//                    8);
        return new DefaultGameObjectGroup<>();
    }
}
