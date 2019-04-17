package com.yotwei.battlecity.game.objects.prefabs.bullet;

import com.yotwei.battlecity.game.LevelHandler;
import com.yotwei.battlecity.game.components.GOBoundingBox;
import com.yotwei.battlecity.game.components.GOSprite;
import com.yotwei.battlecity.game.components.bullet.BulletMotion;
import com.yotwei.battlecity.game.components.listeners.BulletHitListener;
import com.yotwei.battlecity.game.components.listeners.CollisionListener;
import com.yotwei.battlecity.game.components.tank.TankBulletLauncher;
import com.yotwei.battlecity.game.components.tank.TankMotion;
import com.yotwei.battlecity.game.objects.GameObject;
import com.yotwei.battlecity.game.objects.prefabs.effects.Effect;
import com.yotwei.battlecity.game.objects.prefabs.tanks.Tank;
import com.yotwei.battlecity.game.objects.properties.Direction;
import com.yotwei.battlecity.game.objects.properties.buff.Buff;
import com.yotwei.battlecity.util.Constant;
import com.yotwei.battlecity.util.GameObjects;
import org.w3c.dom.css.Rect;

import java.awt.*;

/**
 * Created by YotWei on 2019/3/31.
 */
public class Bullet extends GameObject {

    public static final int TYPE_ID_COMMON = 1;

    private final int typeId;

    private BulletMotion bulletMotion;
    private TankBulletLauncher bulletLauncher;
    private GOSprite sprite;

    private Buff appendBuff;

    /**
     * 子弹的威力，表示能够对物体造成的伤害值
     * 威力会随着对物体造成伤害而减少
     * 当威力减至 0 时，销毁子弹
     */
    private int power;

    private Bullet(LevelHandler levelHandler, int typeId) {
        super(levelHandler, "Bullet");

        this.typeId = typeId;

        Dimension bulletSize = Constant.UNIT_SIZE_HALF;

        // 图形组件
        GOSprite sprite = new GOSprite();
        sprite.setImageResourceId("bullet-" + typeId);
        sprite.setFrameSize(Constant.UNIT_SIZE.width, Constant.UNIT_SIZE.height);
        sprite.setDrawPriority(2);
        addComponent(sprite);

        // 包围盒组件
        GOBoundingBox boundingBox = new GOBoundingBox();
        boundingBox.setSize(bulletSize);
        addComponent(boundingBox);

        // 添加监听事件
        addListener(new BulletBulletHitListener(this));
    }

    @Override
    public void onActive() {
        sprite = getComponent("Sprite");
        bulletMotion = getComponent("BulletMotion");
        bulletLauncher = getComponent("BulletLauncher");
    }

    @Override
    public void onUpdate() {
        move();
    }

    @Override
    public void onDraw(Graphics2D g) {
        final Rectangle sr = sprite.getSrcRect();
        final Rectangle dr = GameObjects.boundingBox(this);

        sprite.setFrameOffset(bulletMotion.direction().index, 0);

        int dx = dr.x + (dr.width - sr.width >> 1);
        int dy = dr.y + (dr.height - sr.height >> 1);

        g.drawImage(
                sprite.getImage(),

                dx,
                dy,
                dx + sr.width,
                dy + sr.height,

                sr.x,
                sr.y,
                sr.x + sr.width,
                sr.y + sr.height,

                null
        );
    }

    @Override
    public void onInactive() {
        final Rectangle bulletBox = GameObjects.boundingBox(this);
        final Dimension unitSize = Constant.UNIT_SIZE;

        Effect effect = Effect.createInstance(
                levelHandler,
                Effect.ID_BOOM_SMALL,
                bulletBox.x + (bulletBox.width - unitSize.width >> 1),
                bulletBox.y + (bulletBox.height - unitSize.height >> 1));

        levelHandler.addObject("effects", effect);
    }

    private void move() {

        final int moveDist = bulletMotion.getMoveDist();
        final GOBoundingBox bulletBox = getComponent("BoundingBox");

        switch (bulletMotion.direction()) {
            case RIGHT:
                bulletBox.x += moveDist;
                break;
            case LEFT:
                bulletBox.x -= moveDist;
                break;
            case DOWN:
                bulletBox.y += moveDist;
                break;
            case UP:
                bulletBox.y -= moveDist;
                break;
        }
    }

    public int getPower() {
        return power;
    }

    void decPower(int dec) {
        if (power > dec) {
            power -= dec;
        } else {
            power = 0;
            setActive(false);
        }
    }

    public TankBulletLauncher getBulletLauncher() {
        return bulletLauncher;
    }

    public static Bullet createInstance(
            TankBulletLauncher launcher,
            int typeId) {

        Tank tank = launcher.getTank();
        Bullet bullet = new Bullet(tank.getLevelHandler(), typeId);

        //
        // 根据id 设置基本属性
        //
        switch (typeId) {
            case 2: // id = 2 的子弹附带减速效果
                bullet.appendBuff = new Buff<>(Buff.TYPE_SLOW_DOWN, 60 * 2, 0.8f);
                bullet.power = 1;
                break;
            case 3:
                bullet.power = 8;
                break;
            default:
                bullet.power = 1;
                break;
        }

        // 炮弹发射器也是组件之一
        bullet.addComponent(launcher);

        // 获取坦克的运动组件，进而得知坦克的朝向
        final TankMotion<? extends Tank> tankMotion = tank.getComponent("TankMotion");
        //
        // 添加炮弹运动组件
        // 坦克当前朝向将作为炮弹发射的方向
        //
        Direction bulletDir = tankMotion.getFacingDirection();
        BulletMotion bm = new BulletMotion(bulletDir);
        bullet.addComponent(bm);

        //
        // 添加碰撞监听器
        //
        CollisionListener bulletCollisionListener = new BulletCollisionListener(bullet);
        bullet.addListener(bulletCollisionListener);

        // 最后激活
        bullet.setActive(true);
        return bullet;
    }

    public Buff getAppendBuff() {
        return appendBuff;
    }

    private static class BulletCollisionListener extends CollisionListener<Bullet> {

        private final Bullet bullet;

        private BulletCollisionListener(Bullet bullet) {
            super(bullet);
            this.bullet = bullet;
        }

        @Override
        public void onTouchBound(Rectangle boundary) {
            if (!boundary.intersects(GameObjects.boundingBox(bullet)))
                bullet.setActive(false);
        }

        @Override
        public void onCollide(GameObject anotherObject) {
            if (!bullet.isActive()) {
                return;
            }

            // 比较 tag
            // 检查击中目标是不是发出子弹的的坦克或同一阵营的坦克
            // 如果是就不需要触发子弹撞击事件
            String tankTag = bullet.getBulletLauncher().getTank().getTag();
            String objTag = anotherObject.getTag();
            if (tankTag.equals(objTag)) {
                return;
            }

            // 获取 BulletHitListener，如果为空也不触发事件
            BulletHitListener bulletHitListener = anotherObject.getListener("BulletHitListener");
            if (null != bulletHitListener) {
                int damage = bulletHitListener.onBulletHit(bullet);
                bullet.decPower(damage); // 扣除子弹自身的威力
            }
        }
    }

    private static class BulletBulletHitListener extends BulletHitListener<Bullet> {

        private final Bullet bullet;

        BulletBulletHitListener(Bullet bullet) {
            super(bullet);
            this.bullet = bullet;
        }

        @Override
        public int onBulletHit(Bullet anoBullet) {
            // 子弹与子弹碰撞，相互削弱威力
            int damage = Math.min(bullet.getPower(), anoBullet.getPower());
            bullet.decPower(damage);
            return damage;
        }
    }
}
