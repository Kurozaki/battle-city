package com.yotwei.battlecity.game.objects.prefabs.tanks;

import com.yotwei.battlecity.game.LevelHandler;
import com.yotwei.battlecity.game.PlayerMonitor;
import com.yotwei.battlecity.game.components.GOSprite;
import com.yotwei.battlecity.game.components.tank.TankBuffContainer;
import com.yotwei.battlecity.game.components.tank.TankBulletLauncherPlayer;
import com.yotwei.battlecity.game.components.tank.TankMotion;
import com.yotwei.battlecity.game.components.tank.TankMotionPlayer;
import com.yotwei.battlecity.game.objects.properties.Direction;
import com.yotwei.battlecity.game.objects.properties.buff.Buff;
import com.yotwei.battlecity.util.Constant;

import java.awt.*;

/**
 * Created by YotWei on 2019/3/30.
 */
public class PlayerTank extends Tank {

    PlayerTank(LevelHandler levelHandler) {
        super(levelHandler, "PlayerTank");

        Dimension tankSize = Constant.UNIT_SIZE_2X;

        // 设置图形组件
        GOSprite sprite = getComponent("Sprite");
        sprite.setImageResourceId("player");
        sprite.setFrameSize(tankSize.width, tankSize.height);

        addComponent(new TankMotionPlayer(this));
        addComponent(new TankBulletLauncherPlayer(this));
        addListener(new TankBulletHitListener(this, 2));
    }

    @Override
    public void onInactive() {
        super.onInactive();
        // 触发玩家死亡事件
        levelHandler.triggerPlayerDeath(this);
    }
}
