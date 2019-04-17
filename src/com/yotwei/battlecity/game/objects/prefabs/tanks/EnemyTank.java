package com.yotwei.battlecity.game.objects.prefabs.tanks;

import com.yotwei.battlecity.game.LevelHandler;
import com.yotwei.battlecity.game.components.GOSprite;
import com.yotwei.battlecity.game.components.tank.TankBulletLauncherEnemy;
import com.yotwei.battlecity.game.components.tank.TankMotionEnemy;
import com.yotwei.battlecity.util.Constant;

import java.awt.*;

/**
 * Created by YotWei on 2019/3/30.
 */
public class EnemyTank extends Tank {

    private final int typeId;

    EnemyTank(LevelHandler levelHandler, int typeId) {
        super(levelHandler, "EnemyTank");
        this.typeId = typeId;

        Dimension tankSize = Constant.UNIT_SIZE_2X;

        GOSprite sprite = getComponent("Sprite");
        sprite.setImageResourceId("enemy-" + typeId);
        sprite.setFrameSize(tankSize.width, tankSize.height);

        addComponent(new TankMotionEnemy(this));
    }

    @Override
    public void onInactive() {
        super.onInactive();
        // 敌人死亡事件
        levelHandler.triggerEnemyDeath(this);
    }
}
