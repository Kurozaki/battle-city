package com.yotwei.battlecity.game.objects;

import com.yotwei.battlecity.game.LevelHandler;
import com.yotwei.battlecity.game.components.GOComponent;
import com.yotwei.battlecity.game.components.GOListener;
import com.yotwei.battlecity.util.GameObjects;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YotWei on 2019/3/13.
 */
@SuppressWarnings("WeakerAccess")
public abstract class GameObject {

    private final int uniqueId;

    /**
     * 物体的标签
     */
    private final String tag;

    /**
     * 为了能在类里面调用 LevelHandler 的接口，
     * 必须持有一个 LevelHandler 的引用
     */
    protected final LevelHandler levelHandler;

    /**
     * 物体是否存活
     */
    private boolean isActive;

    /**
     * 存放组件
     */
    private final Map<String, GOComponent> components;

    /**
     * 存放监听器
     */
    private final Map<String, GOListener> listeners;

    protected GameObject(LevelHandler levelHandler, String tag) {
        this.levelHandler = levelHandler;
        this.tag = tag;
        this.uniqueId = GameObjects.autoIncrementId();

        this.components = new HashMap<>();
        this.listeners = new HashMap<>();
    }

    // ------------------------------------
    //
    // 向指定容器中添加或获取组件，监听器的方法
    //
    // ------------------------------------
    public void addComponent(GOComponent comp) {
        components.put(comp.name(), comp);
    }

    public void addListener(GOListener l) {
        listeners.put(l.name(), l);
    }

    @SuppressWarnings("unchecked")
    public <_CType extends GOComponent> _CType getComponent(String name) {
        if (components.containsKey(name))
            return (_CType) components.get(name);
        throw new RuntimeException("Component for " + this.getClass().getSimpleName() + " not found: " + name);
    }

    @SuppressWarnings("unchecked")
    public <_LType extends GOListener> _LType getListener(String name) {
        return (_LType) listeners.get(name);
    }

    public void setActive(boolean active) {
        if (isActive != active) {
            if (active) {
                onActive();
            } else {
                onInactive();
            }
            isActive = active;
        } else {
            throw new RuntimeException("Illegal operation for object: setActive(" + active + ")");
        }
    }

    public boolean isActive() {
        return isActive;
    }

    // ------------------------------------
    //
    // 物体声明周期的抽象方法
    //
    // ------------------------------------
    public abstract void onActive();

    public abstract void onUpdate();

    public abstract void onDraw(Graphics2D g);

    public abstract void onInactive();

    // ------------------------------------
    //
    // 重写 hashCode 和 equals 方法
    //
    // ------------------------------------
    @Override
    public int hashCode() {
        return uniqueId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GameObject &&
                ((GameObject) obj).uniqueId == this.uniqueId;
    }

    // ------------------------------------
    //
    // getter 方法
    //
    // ------------------------------------
    public int getUniqueId() {
        return uniqueId;
    }

    public String getTag() {
        return tag;
    }

    public LevelHandler getLevelHandler() {
        return levelHandler;
    }
}
