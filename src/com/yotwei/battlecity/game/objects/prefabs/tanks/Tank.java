package com.yotwei.battlecity.game.objects.prefabs.tanks;

import com.yotwei.battlecity.game.LevelHandler;
import com.yotwei.battlecity.game.components.GOBoundingBox;
import com.yotwei.battlecity.game.components.GOSprite;
import com.yotwei.battlecity.game.components.listeners.BulletHitListener;
import com.yotwei.battlecity.game.components.listeners.CollisionListener;
import com.yotwei.battlecity.game.components.tank.TankBuffContainer;
import com.yotwei.battlecity.game.components.tank.TankBulletLauncher;
import com.yotwei.battlecity.game.components.tank.TankBulletLauncherEnemy;
import com.yotwei.battlecity.game.components.tank.TankMotion;
import com.yotwei.battlecity.game.objects.GameObject;
import com.yotwei.battlecity.game.objects.prefabs.bullet.Bullet;
import com.yotwei.battlecity.game.objects.prefabs.effects.Effect;
import com.yotwei.battlecity.game.objects.properties.Direction;
import com.yotwei.battlecity.game.objects.properties.buff.Buff;
import com.yotwei.battlecity.util.AudioUtil;
import com.yotwei.battlecity.util.Constant;
import com.yotwei.battlecity.util.GameObjects;

import java.awt.*;
import java.util.Set;

/**
 * Created by YotWei on 2019/3/19.
 */
public class Tank extends GameObject {

    private GOSprite sprite;
    private GOSprite guardSprite;
    private GOBoundingBox boundBox;

    private TankMotion tankMotion;
    private TankBulletLauncher bulletLauncher;
    private TankBuffContainer buffs;

    /**
     * 闪烁计数器，当坦克被子弹击中时会发生短暂闪烁
     */
    private int glintTicker;
    private final Composite glintAlphaComposite = AlphaComposite
            .getInstance(AlphaComposite.SRC_ATOP, 0.6f);

    Tank(LevelHandler levelHandler, String tankTag) {
        super(levelHandler, tankTag);

        Dimension tankSize = Constant.UNIT_SIZE_2X;

        // 添加图形组件，但暂时先不设置图形资源
        GOSprite sprite = new GOSprite();
        addComponent(sprite);

        // 提那家包围盒组件
        GOBoundingBox boundingBox = new GOBoundingBox();
        boundingBox.setSize(tankSize);
        addComponent(boundingBox);

        guardSprite = new GOSprite();
        guardSprite.setImageResourceId("special_guard");
        guardSprite.setFrameSize(tankSize.width, tankSize.height);

        // buff 容器
        addComponent(new TankBuffContainer());
        // 碰撞监听器
        addListener(new TankCollisionListener(this));
    }

    @Override
    public void onActive() {
        //
        // 坦克必须包含以下组件
        //
        sprite = getComponent("Sprite");
        boundBox = getComponent("BoundingBox");

        tankMotion = getComponent("TankMotion");
        bulletLauncher = getComponent("BulletLauncher");

        buffs = getComponent("BuffContainer");

        // 刚出生的坦克拥有一个持续 2 秒的无敌 buff
        buffs.addBuff(new Buff<>(Buff.TYPE_GUARD, 60 * 2, 0));
    }

    @Override
    public void onUpdate() {
        tankMove();
        bulletLaunch();
        updateBuffs();
    }

    @Override
    public void onDraw(Graphics2D g) {

        if ((glintTicker >> 1) % 2 == 1) {
            // 受子弹撞击出现短暂闪烁，使用变化alpha实现
            g.setComposite(glintAlphaComposite);
        }
        if (glintTicker > 0) glintTicker--;

        final Rectangle dr = getComponent("BoundingBox");
        Rectangle sr;

        sprite.setFrameOffset(0, tankMotion.getFacingDirection().index);
        sr = sprite.getSrcRect();
        g.drawImage(
                sprite.getImage(),

                dr.x,
                dr.y,
                dr.x + dr.width,
                dr.y + dr.height,

                sr.x,
                sr.y,
                sr.x + sr.width,
                sr.y + sr.height,

                null
        );
        g.setComposite(AlphaComposite.SrcAtop);

        // 检查有没有无敌 buff
        TankBuffContainer buffs = getComponent("BuffContainer");

        // 如果存在无敌 buff，绘制保护罩效果
        if (buffs.getBuff(Buff.TYPE_GUARD) != null) {
            guardSprite.setFrameOffset(levelHandler.getFrameTicker() >> 2, 0);
            sr = guardSprite.getSrcRect();
            g.drawImage(
                    guardSprite.getImage(),

                    dr.x,
                    dr.y,
                    dr.x + dr.width,
                    dr.y + dr.height,

                    sr.x,
                    sr.y,
                    sr.x + sr.width,
                    sr.y + sr.height,

                    null
            );
        }
    }

    @Override
    public void onInactive() {
        Effect effect = Effect.createInstance(
                levelHandler,
                Effect.ID_BOOM,
                boundBox.x,
                boundBox.y);
        levelHandler.addObject("effects", effect);
    }

    /**
     * 坦克移动的方法
     */
    private void tankMove() {

        tankMotion.calcNextDirection();

        int moveDist = tankMotion.getMoveDist();
        Direction nextDirection = tankMotion.getMoveDirection();

        if (nextDirection == null)
            return;

        switch (nextDirection) {
            case DOWN:
                boundBox.y += moveDist;
                break;
            case RIGHT:
                boundBox.x += moveDist;
                break;
            case LEFT:
                boundBox.x -= moveDist;
                break;
            case UP:
                boundBox.y -= moveDist;
                break;
        }

        // 坐标对齐
        Dimension unitSize = Constant.UNIT_SIZE;
        if (nextDirection.isVertical()) {
            int offsetX = boundBox.x % unitSize.width;
            if (offsetX < unitSize.width >> 1) {
                boundBox.x -= offsetX;
            } else {
                boundBox.x += unitSize.width - offsetX;
            }
        } else if (nextDirection.isHorizontal()) {
            int offsetY = boundBox.y % unitSize.height;
            if (offsetY < unitSize.height >> 1) {
                boundBox.y -= offsetY;
            } else {
                boundBox.y += unitSize.height - offsetY;
            }
        }
    }

    /**
     * 处理炮弹发射的逻辑
     */
    private void bulletLaunch() {
        final Set<Bullet> bulletSet = bulletLauncher.getBullets();
        for (Bullet bullet : bulletSet) {
            // 向 levelHandler 中添加炮弹物体
            levelHandler.addObject("bullets", bullet);
        }
    }

    private void updateBuffs() {
        TankBuffContainer buffContainer = getComponent("BuffContainer");
        buffContainer.updateBuffs();
    }

    /**
     * 创建玩家坦克，并设置初始xy值
     */
    public static PlayerTank createPlayerTankInstance(LevelHandler levelHandler, int x, int y) {
        PlayerTank tank = new PlayerTank(levelHandler);

        // 获取包围盒，设置坐标
        GOBoundingBox box = tank.getComponent("BoundingBox");
        box.setLocation(x, y);

        // 激活坦克
        tank.setActive(true);
        return tank;
    }

    /**
     * 根据敌人类型 id 创建敌人坦克，并设置初始 xy 值
     */
    public static EnemyTank createEnemyInstance(LevelHandler levelHandler, int typeId, int x, int y) {
        EnemyTank tank = new EnemyTank(levelHandler, typeId);

        GOBoundingBox tankBox = tank.getComponent("BoundingBox");
        tankBox.setLocation(x, y);

        //
        // 设置坦克的子弹发射种类，
        // 还有坦克耐久度
        //
        switch (typeId) {
            case 1:
                tank.addComponent(new TankBulletLauncherEnemy(tank, 1, 1));
                tank.addListener(new TankBulletHitListener(tank, 2));
                break;
            case 2:
                tank.addComponent(new TankBulletLauncherEnemy(tank, 1, 1));
                tank.addListener(new TankBulletHitListener(tank, 3));
                break;
            case 3:
                tank.addComponent(new TankBulletLauncherEnemy(tank, 1, 3));
                tank.addListener(new TankBulletHitListener(tank, 8));
                break;
            case 4:
                tank.addComponent(new TankBulletLauncherEnemy(tank, 1, 2));
                tank.addListener(new TankBulletHitListener(tank, 4));
                break;
            case 5:
                tank.addComponent(new TankBulletLauncherEnemy(tank, 1, 4));
                tank.addListener(new TankBulletHitListener(tank, 4));
                break;
        }

        tank.setActive(true);
        return tank;
    }

    private static class TankCollisionListener extends CollisionListener<Tank> {

        private final Tank tank;

        TankCollisionListener(Tank tank) {
            super(tank);
            this.tank = tank;
        }

        @Override
        public void onTouchBound(Rectangle boundary) {
            Rectangle tankBox = GameObjects.boundingBox(tank);

            if (tankBox.x < boundary.x)
                // 左侧边界
                tankBox.x = boundary.x;
            else if (tankBox.x + tankBox.width > boundary.x + boundary.width)
                // 右侧边界
                tankBox.x = boundary.x + boundary.width - tankBox.width;

            if (tankBox.y < boundary.y)
                // 上侧边界
                tankBox.y = boundary.y;
            else if (tankBox.y + tankBox.height > boundary.y + boundary.height)
                // 下侧边界
                tankBox.y = boundary.y + boundary.height - tankBox.height;
        }

        @Override
        public void onCollide(GameObject anotherObject) {
            TankMotion<? extends Tank> tankMotion = tank.getComponent("TankMotion");

            // 获取坦克朝向
            Direction dir = tankMotion.getMoveDirection();

            // 坦克静止，无修正坐标
            if (dir == null) return;

            GOBoundingBox tankBox = tank.getComponent("BoundingBox");
            GOBoundingBox anoBox = anotherObject.getComponent("BoundingBox");

            Point p1 = GameObjects.boundingBoxCenter(tank);
            Point p2 = GameObjects.boundingBoxCenter(anotherObject);

            int dx = Math.abs(p1.x - p2.x);
            int dy = Math.abs(p1.y - p2.y);

            if (dir == Direction.LEFT || dir == Direction.RIGHT) {
                if (dy <= dx) {
                    if (dir == Direction.RIGHT && p1.x < p2.x) {
                        tankBox.x = anoBox.x - tankBox.width;
                    } else if (dir == Direction.LEFT && p1.x > p2.x) {
                        tankBox.x = anoBox.x + anoBox.width;
                    }
                }
            } else if (dir == Direction.UP || dir == Direction.DOWN) {
                if (dy >= dx) {
                    if (dir == Direction.DOWN && p1.y < p2.y) {
                        tankBox.y = anoBox.y - tankBox.height;
                    } else if (dir == Direction.UP && p1.y > p2.y) {
                        tankBox.y = anoBox.y + anoBox.height;
                    }
                }
            }
        }
    }

    protected static class TankBulletHitListener extends BulletHitListener<Tank> {

        private final Tank tank;
        private int tankDurability;

        TankBulletHitListener(Tank tank, int tankDurability) {
            super(tank);
            this.tank = tank;
            this.tankDurability = tankDurability;
        }

        @Override
        public int onBulletHit(Bullet bullet) {

            // 无敌 buff 检查
            if (tank.buffs.getBuff(Buff.TYPE_GUARD) != null) {
                // 免疫一切伤害
                return bullet.getPower();
            } else {
                // 计算伤害
                int damage = Math.min(bullet.getPower(), tankDurability);
                tankDurability -= damage;
                if (tankDurability > 0) {
                    // 给坦克 16 帧闪烁表示击中
                    tank.glintTicker = 16;
                    //
                    // 如果子弹附带 buff，要添加到击中的坦克上
                    //
                    Buff buff = bullet.getAppendBuff();
                    if (null != buff) {
                        tank.buffs.addBuff(buff);
                    }
                } else {
                    tank.setActive(false);
                }
                return damage;
            }
        }
    }
}