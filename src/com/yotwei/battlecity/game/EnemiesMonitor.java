package com.yotwei.battlecity.game;

import com.yotwei.battlecity.util.Bundle;
import com.yotwei.battlecity.util.Constant;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by YotWei on 2019/4/4.
 */
public class EnemiesMonitor {

    private List<Point> spawnPoints;

    /**
     * 每一辆要生成的敌方坦克的信息
     * 信息包括: 生成点坐标, id
     */
    private Queue<Bundle> enemiesSpawnInfo;

    /**
     * @param counts 敌人种类的数量，其中敌人的id=下标+1
     *               例:
     *               {2, 4, 5} 表示
     *               id = 1 的敌人数量为 2，
     *               id = 2 的敌人数量为 4,
     *               id = 3 的敌人数量为 5
     */
    private EnemiesMonitor(int[] counts) {
        spawnPoints = new ArrayList<>();
        for (int x = 0; x < Constant.WND_SIZE.width; x += Constant.UNIT_SIZE_2X.width) {
            spawnPoints.add(new Point(x, 0));
        }
        Collections.shuffle(spawnPoints);

        enemiesSpawnInfo = new LinkedList<>();

        for (int i = 0, spawnPointIndex = 0; i < counts.length; i++) {
            int typeId = i + 1;
            int count = counts[i];

            for (int j = 0; j < count; j++) {
                Bundle bundle = new Bundle();
                if (spawnPointIndex == spawnPoints.size()) {
                    spawnPointIndex = 0;
                    // 打乱一下生成点位置
                    Collections.shuffle(spawnPoints);
                }

                // 填充信息
                Point sp = spawnPoints.get(spawnPointIndex++);
                bundle.set("id", typeId);
                bundle.set("x", sp.x);
                bundle.set("y", sp.y);

                // 加入队列
                enemiesSpawnInfo.add(bundle);
            }
        }
    }

    public Bundle nextEnemySpawnInfo() {
        if (enemiesSpawnInfo.isEmpty())
            return null;
        else
            return enemiesSpawnInfo.poll();
    }

    public static EnemiesMonitor createInstance(int[] counts) {
        return new EnemiesMonitor(counts);
    }
}

