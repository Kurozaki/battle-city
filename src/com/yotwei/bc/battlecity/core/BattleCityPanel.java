package com.yotwei.bc.battlecity.core;

import com.yotwei.bc.battlecity.stage.IStage;
import com.yotwei.bc.battlecity.stage.StageHolder;
import com.yotwei.bc.battlecity.util.Debugger;

import javax.swing.*;
import java.awt.*;

/**
 * Created by YotWei on 2018/6/12.
 */
class BattleCityPanel extends JPanel {

    private boolean isAlive = true;

    /**
     * the handling stage
     */
    private IStage curStage;

    BattleCityPanel() {
        // set init viewport size
        setSize(Const.SIZE_VIEWPORT);

        // get next handle stage
        curStage = StageHolder.INSTANCE.nextStage();

        // stage handle thread start
        new PanelHandleThread(this).start();
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void paint() {
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (curStage != null)
            curStage.draw((Graphics2D) g);
    }

    public void update() {
        if (curStage != null) {
            curStage.process();
            if (!curStage.isActivate()) {
                curStage = StageHolder.INSTANCE.nextStage();
            }
        } else {
            isAlive = false;
            Debugger.Log.important("END");
        }
    }
}

class PanelHandleThread extends Thread {

    private BattleCityPanel panel;

    PanelHandleThread(BattleCityPanel panel) {
        this.panel = panel;
    }

    @Override
    public void run() {
        while (panel.isAlive()) {
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            panel.update();
            panel.paint();
        }
    }
}