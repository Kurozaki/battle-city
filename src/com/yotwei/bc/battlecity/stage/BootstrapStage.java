package com.yotwei.bc.battlecity.stage;

import com.yotwei.bc.battlecity.core.Const;
import com.yotwei.bc.battlecity.exception.InitialException;
import com.yotwei.bc.battlecity.factory.GraphicsFactory;
import com.yotwei.bc.battlecity.level.lvl.LevelsHolder;
import com.yotwei.bc.battlecity.util.Debugger;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by YotWei on 2018/6/12.
 */
public class BootstrapStage implements IStage {

    private final Font drawFont = new Font("Consolas", Font.BOLD, 24);

    private volatile boolean loadingFinished = false;

    public BootstrapStage() {
        new ResourceLoadingThread().start();
    }

    public void process() {
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Const.SIZE_VIEWPORT.width, Const.SIZE_VIEWPORT.height);

        g.setColor(Color.LIGHT_GRAY);
        g.setFont(drawFont);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        String drawText = "Loading Resource.";
        Rectangle2D r = drawFont.getStringBounds(drawText, g.getFontRenderContext());
        g.drawString(drawText,
                ((int) (Const.SIZE_VIEWPORT.width - r.getWidth())) >> 1,
                ((int) (Const.SIZE_VIEWPORT.height + r.getHeight())) >> 1);
    }

    public boolean isActivate() {
        return !loadingFinished;
    }

    class ResourceLoadingThread extends Thread {

        @Override
        public void run() {
            try {
                LevelsHolder.INSTANCE.init();
                GraphicsFactory.INSTANCE.loadGraphics();
            } catch (InitialException e) {
                e.printStackTrace();
            }
            Debugger.Log.info("loading finished");
            StageHolder.INSTANCE.addStage(new HeadlineStage());
            loadingFinished = true;
        }
    }
}
