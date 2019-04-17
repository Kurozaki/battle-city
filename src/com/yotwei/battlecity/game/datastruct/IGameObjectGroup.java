package com.yotwei.battlecity.game.datastruct;


import com.yotwei.battlecity.game.objects.GameObject;

import java.awt.*;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by YotWei on 2019/3/17.
 * <p>
 * 游戏物体组，用于管理一组游戏物体
 */
public interface IGameObjectGroup {


    /**
     * 向物体组添加游戏物体
     *
     * @param go 所添加的游戏物体
     * @return 如果 true 表示添加成功
     */
    boolean add(GameObject go);

    /**
     * 删除游戏物体
     *
     * @param go 要删除的游戏物体
     * @return 如果物体存在，则删除成功，返回 true，否则返回 false
     */
    boolean remove(GameObject go);

    /**
     * 检索同矩形区域相交的所有游戏物体
     *
     * @param retrieveArea 检索的矩形区域
     * @return 与矩形区域相交的物体集，如果没有则返回一个空集合（不是 null 值）
     */
    Set<GameObject> retrieve(Rectangle retrieveArea);

    /**
     * 遍历组内的每一个物体
     */
    void each(Consumer<GameObject> consumer);

    /**
     * 包含元素的数量
     */
    int size();
}
