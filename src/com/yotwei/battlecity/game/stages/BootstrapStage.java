package com.yotwei.battlecity.game.stages;

import com.yotwei.battlecity.framework.IStageHandleContext;
import com.yotwei.battlecity.framework.stage.IStage;
import com.yotwei.battlecity.game.global.ResourcePool;
import com.yotwei.battlecity.util.GraphicUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * Created by YotWei on 2019/3/14.
 * <p>
 * 本场景用于加载资源
 */
public class BootstrapStage implements IStage {

    // 用来标识资源加载有没有完成
    private volatile boolean isProcessFinished = false;

    @Override
    public void onInit(IStageHandleContext ctx) {
        ResourceProcessThread th = new ResourceProcessThread();
        th.start();
    }

    @Override
    public void update(IStageHandleContext ctx) {

    }

    @Override
    public void draw(Graphics2D g) {
        GraphicUtil.clearScreen(g, Color.BLACK);
    }

    @Override
    public void onFinished(IStageHandleContext ctx) {

    }

    @Override
    public IStage next() {
        return isProcessFinished ? new HeadlineStage() : this;
    }

    class ResourceProcessThread extends Thread {

        private final Logger logger = LoggerFactory.getLogger(ResourceProcessThread.class);

        @Override
        public void run() {

            long start = System.currentTimeMillis();    // 记录开始时间

            if (logger.isInfoEnabled()) {
                logger.info("Start to process resources...");
            }

            ResourcePool.init();    // 在新的线程里加载资源
            isProcessFinished = true;

            if (logger.isInfoEnabled()) {
                logger.info(
                        "Resources process finished, time cost: {}ms",
                        (System.currentTimeMillis() - start));
            }
        }
    }
}
