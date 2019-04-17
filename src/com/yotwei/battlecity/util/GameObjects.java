package com.yotwei.battlecity.util;

import com.yotwei.battlecity.game.components.GOBoundingBox;
import com.yotwei.battlecity.game.components.GOSprite;
import com.yotwei.battlecity.game.objects.GameObject;

import java.awt.*;
import java.util.Base64;
import java.util.Comparator;

/**
 * Created by YotWei on 2019/3/29.
 * <p>
 * 关于 GameObject 的静态工具方法
 */
public class GameObjects {

    private GameObjects() throws Exception {
        throw new Exception("Unable to create a GameObjects instance");
    }

    /**
     * 绘制优先级的物体比较器
     * 使用场景是绘制物体的时候
     */
    public static final Comparator<GameObject> DRAW_PRIORITY_COMPARATOR = (o1, o2) -> {
        GOSprite s1 = o1.getComponent("Sprite");
        GOSprite s2 = o2.getComponent("Sprite");
        return s1.getDrawPriority() - s2.getDrawPriority();
    };

    /**
     * id 自增变量
     */
    private static int iid = 1000;

    public static int autoIncrementId() {
        return iid++;
    }

    /**
     * 获取物体的包围盒组件（{@link GOBoundingBox}）
     * 如果没有包围盒组件 ({@link GOBoundingBox})，就抛出异常
     */
    public static Rectangle boundingBox(GameObject go) {
        // 获取到的应该为 GOBoundingBox 对象，
        // 而 GOBoundingBox 继承自 Rectangle
        return (Rectangle) go.getComponent("BoundingBox");
    }

    /**
     * 获取物体包围盒的坐标中心点
     * 保证物体有 {@link GOBoundingBox} 组件，如果没有，抛出异常
     */
    public static Point boundingBoxCenter(GameObject go) {
        GOBoundingBox box = go.getComponent("BoundingBox");
        return new Point(
                box.x + (box.width >> 1),
                box.y + (box.height >> 1)
        );
    }
}
