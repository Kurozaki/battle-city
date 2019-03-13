package com.yotwei.battlecity.game.object.bullet;

import com.yotwei.battlecity.game.object.GameObjectFactory;
import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.effect.Effect;
import com.yotwei.battlecity.game.object.tank.AbstractTank;
import com.yotwei.battlecity.util.Constant;

import java.awt.*;

/**
 * Created by YotWei on 2019/3/11.
 */
public class BurstBullet extends AbstractBullet {

    protected BurstBullet(LevelContext lvlCtx, int bulletId, AbstractTank associateTank) {
        super(lvlCtx, bulletId, associateTank);

        bulletATK = 50;
    }

    @Override
    public void onInactive() {
        super.onInactive();

        Dimension unitSize = Constant.UNIT_SIZE;
        Rectangle hitbox = getHitbox();

        int uw = unitSize.width;
        int uh = unitSize.height;

        int centerX = ((hitbox.x + (hitbox.width >> 1)) / uw) * uw - (uw >> 1);
        int centerY = ((hitbox.y + (hitbox.height >> 1)) / uh) * uh - (uh >> 1);

        int rad = 2;

        for (int i = -rad; i <= rad; i++) {
            for (int j = -rad; j <= rad; j++) {

                int dis = Math.abs(i) + Math.abs(j);
                if (dis > rad)
                    continue;

                BurstFire fire = new BurstFire(getLevelContext(), -1, getAssociateTank());
                fire.getHitbox().setLocation(
                        centerX + i * uw,
                        centerY + j * uh
                );

                // active an trigger event
                fire.setActive(true);
                fire.ticker += dis * 4;
                getLevelContext().triggerEvent(LevelContext.Event.wrap("addObject", fire));
            }
        }

    }

    private static class BurstFire extends AbstractBullet {

        private int ticker = 4;

        BurstFire(LevelContext lvlCtx, int bulletId, AbstractTank associateTank) {
            super(lvlCtx, bulletId, associateTank);

            getHitbox().setSize(Constant.UNIT_SIZE);
            setSpeed(0);

            bulletATK = 40;
        }

        @Override
        public void onActive() {
//            super.onActive();
        }

        @Override
        public void update() {
            if (ticker == 3) {
                Effect effect = GameObjectFactory.createEffect(getLevelContext(), 3);
                effect.setCoordinate(
                        getHitbox().x,
                        getHitbox().y
                );
                getLevelContext().triggerEvent(LevelContext.Event.wrap("addObject", effect));
            }
            if (--ticker <= 0) {
                setActive(false);
            }
        }

        @Override
        public void onInactive() {
        }

        @Override
        public void draw(Graphics2D g) {
        }

        @Override
        public int tryDamage(int damageValue) {
            return 0;
        }
    }
}
