package com.yotwei.battlecity.client;

import com.yotwei.battlecity.framework.App;
import com.yotwei.battlecity.framework.stage.EntryStage;
import com.yotwei.battlecity.framework.stage.IStage;
import com.yotwei.battlecity.game.stages.BootstrapStage;
import com.yotwei.battlecity.util.Constant;

import javax.swing.*;

/**
 * Created by YotWei on 2018/10/30.
 */
public class Client {

    public static void main(String[] args) {

        App app = new App(new EntryStage() {

            @Override
            public IStage next() {
                return new BootstrapStage();
            }

        }, Constant.WND_SIZE);

        try {
            app.start();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    app,
                    e.getMessage(),
                    "Exception",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            app.clear();
        }
    }

}
