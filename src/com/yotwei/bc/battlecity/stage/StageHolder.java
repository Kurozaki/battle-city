package com.yotwei.bc.battlecity.stage;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by YotWei on 2018/6/12.
 */
public enum StageHolder {

    INSTANCE;

    StageHolder() {
    }

    private final Queue<IStage> stageQueue = new LinkedList<>();

    public IStage nextStage() {
        return stageQueue.poll();
    }

    public void addStage(IStage stage) {
        stageQueue.add(stage);
    }
}
