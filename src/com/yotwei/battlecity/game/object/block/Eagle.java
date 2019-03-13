package com.yotwei.battlecity.game.object.block;

import com.yotwei.battlecity.game.engine.ResourcePackage;
import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.util.Constant;

import java.awt.*;

/**
 * Created by YotWei on 2019/3/11.
 */
public class Eagle extends AbstractBlock {

    private boolean isDestroyed = false;

    public Eagle(LevelContext lvlCtx) {
        super(lvlCtx, -1);

        setTag("Block-Eagle");
    }

    @Override
    public void onActive() {

        hitbox.setSize(Constant.UNIT_SIZE_2X);
        image = ResourcePackage.getImage("special_eagle");
    }

    @Override
    public void draw(Graphics2D g) {

        int offsetX = isDestroyed ? hitbox.width : 0;

        g.drawImage(
                image,

                hitbox.x,
                hitbox.y,
                hitbox.x + hitbox.width,
                hitbox.y + hitbox.height,

                offsetX,
                0,
                offsetX + hitbox.width,
                hitbox.height,

                null
        );
    }

    @Override
    public int tryDamage(int damageValue) {
        if (!isDestroyed) {
            getLevelContext().triggerEvent(LevelContext.Event.wrap("eagleDestroyed"));
            isDestroyed = true;
        }
        return damageValue;
    }
}
