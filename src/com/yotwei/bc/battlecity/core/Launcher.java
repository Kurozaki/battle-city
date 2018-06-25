package com.yotwei.bc.battlecity.core;

import com.yotwei.bc.battlecity.stage.BootstrapStage;
import com.yotwei.bc.battlecity.stage.StageHolder;

import javax.swing.*;

/**
 * Created by YotWei on 2018/6/12.
 */
public class Launcher {

    public void start() {
        StageHolder.INSTANCE.addStage(new BootstrapStage());

        // get bounds
        JFrame frame = new JFrame();
        frame.setLocation(600, 200);
        frame.setSize(Const.SIZE_VIEWPORT);

        // get game panel instance
        BattleCityPanel panel = new BattleCityPanel();

        // add panel
        frame.add(panel);

        // add key listener
        frame.addKeyListener(new KeyMonitor());

        // start
        frame.setUndecorated(true);
        frame.setVisible(true);
    }
}
