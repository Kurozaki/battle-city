package com.yotwei.battlecity.game.components.tank;

import com.yotwei.battlecity.game.LevelHandler;
import com.yotwei.battlecity.game.objects.GameObject;
import com.yotwei.battlecity.game.objects.prefabs.tanks.EnemyTank;
import com.yotwei.battlecity.game.objects.prefabs.tiles.Block;
import com.yotwei.battlecity.game.objects.properties.Direction;
import com.yotwei.battlecity.util.Constant;
import com.yotwei.battlecity.util.GameObjects;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by YotWei on 2019/3/30.
 */
public class TankMotionEnemy extends TankMotion<EnemyTank> {

    private static final int DIR_CHANGE_TICK = 30;

    private Map<Direction, Rectangle> findWayBoxes;

    private Random rand;
    private int ticker;

    public TankMotionEnemy(EnemyTank tank) {
        super(tank, 0x120);

        facingDirection = Direction.DOWN;
        findWayBoxes = new EnumMap<>(Direction.class);
        rand = new Random();

        //
        // 初始化寻路盒子
        //
        Dimension size = Constant.UNIT_SIZE_HALF;
        findWayBoxes.put(Direction.LEFT, new Rectangle(size));
        findWayBoxes.put(Direction.DOWN, new Rectangle(size));
        findWayBoxes.put(Direction.RIGHT, new Rectangle(size));
        findWayBoxes.put(Direction.UP, new Rectangle(size));
    }

    private void updateFindWayBoxes() {

        // 获取坦克的包围盒
        Rectangle tankBox = GameObjects.boundingBox(tank);

        findWayBoxes.forEach((dir, box) -> {
            switch (dir) {
                case UP:
                    box.setLocation(
                            tankBox.x + (tankBox.width - box.width >> 1),
                            tankBox.y - box.height
                    );
                    break;
                case DOWN:
                    box.setLocation(
                            tankBox.x + (tankBox.width - box.width >> 1),
                            tankBox.y + tankBox.height
                    );
                    break;
                case LEFT:
                    box.setLocation(
                            tankBox.x - box.width,
                            tankBox.y + (tankBox.height - box.height >> 1)
                    );
                    break;
                case RIGHT:
                    box.setLocation(
                            tankBox.x + tankBox.width,
                            tankBox.y + (tankBox.height - box.height >> 1)
                    );
                    break;
            }
        });
    }

    private void doFindWay() {

        LevelHandler lh = tank.getLevelHandler();

        // 检查是否有必要更变方向
        if (ticker-- > 0 && moveDirection != null) {
            //
            // ticker 没有减为 0，并且无碰撞，保持原方向
            //
            Rectangle box = findWayBoxes.get(facingDirection);
            if (!hasOverlapObjects(lh, box)) {
                return;
            }
        }

        Map<Direction, Integer> dirWeights = new EnumMap<>(Direction.class);
        int totalWeight = 0;

        for (Map.Entry<Direction, Rectangle> e : findWayBoxes.entrySet()) {
            Direction dir = e.getKey();
            Rectangle box = e.getValue();

            // 不可用方向
            if (hasOverlapObjects(lh, box)) {
                continue;
            }

            int weight;
            if (Direction.isOpposite(dir, moveDirection)) {
                weight = 1;
            } else {
                if (dir == Direction.DOWN) {
                    weight = 20;
                } else {
                    weight = 10;
                }
            }

            totalWeight += weight;
            dirWeights.put(dir, weight);
        }

        if (totalWeight > 0) {

            int randInt = rand.nextInt(totalWeight);
            for (Map.Entry<Direction, Integer> e : dirWeights.entrySet()) {
                randInt -= e.getValue();
                if (randInt < 0) {
                    moveDirection = e.getKey();
                    break;
                }
            }
        } else {
            moveDirection = null;
        }

        // 重置计数器
        ticker = DIR_CHANGE_TICK;
    }

    private boolean hasOverlapObjects(LevelHandler lh, Rectangle box) {
        if (!lh.getBoundary().contains(box)) return true;
        for (GameObject tile : lh.retrieve("tiles", box)) {
            if (tile instanceof Block) {
                return true;
            }
        }
        return !lh.retrieve("tanks", box).isEmpty();
    }

    @Override
    public void calcNextDirection() {
        updateFindWayBoxes();
        doFindWay();
    }
}
