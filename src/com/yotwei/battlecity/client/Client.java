package com.yotwei.battlecity.client;

import com.yotwei.battlecity.framework.App;
import com.yotwei.battlecity.framework.stage.BootstrapStage;
import com.yotwei.battlecity.framework.stage.IStage;
import com.yotwei.battlecity.game.stage.HeadlineStage;
import com.yotwei.battlecity.game.stage.PrepareStage;
import com.yotwei.battlecity.util.Constant;

/**
 * Created by YotWei on 2018/10/30.
 */
public class Client {

    public static void main(String[] args) {

        App app = new App(new BootstrapStage() {

            @Override
            public IStage next() {
                return new PrepareStage();
            }

        }, Constant.WND_SIZE);

        try {
            app.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            app.clear();
        }
    }
}
