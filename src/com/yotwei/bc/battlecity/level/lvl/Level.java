package com.yotwei.bc.battlecity.level.lvl;

import com.yotwei.bc.battlecity.core.Const;
import com.yotwei.bc.battlecity.datastructure.EntityList;
import com.yotwei.bc.battlecity.datastructure.FullQuadTree;
import com.yotwei.bc.battlecity.level.obj.EntityObject;
import com.yotwei.bc.battlecity.level.obj.block.Block;
import com.yotwei.bc.battlecity.level.obj.block.Eagle;
import com.yotwei.bc.battlecity.level.obj.bullet.Bullet;
import com.yotwei.bc.battlecity.level.obj.effect.Effect;
import com.yotwei.bc.battlecity.level.obj.property.Type;
import com.yotwei.bc.battlecity.level.obj.special.Special;
import com.yotwei.bc.battlecity.level.obj.special.TankGenerator;
import com.yotwei.bc.battlecity.level.obj.tank.AbstractTank;
import com.yotwei.bc.battlecity.level.obj.tank.EnemyTank;
import com.yotwei.bc.battlecity.level.obj.tank.PlayerTank;
import com.yotwei.bc.battlecity.sprite.Background;

import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Created by YotWei on 2018/6/13.
 */
public class Level {

    private static final Rectangle LEVEL_BOUND = new Rectangle(
            0,
            0,
            Const.SIZE_VIEWPORT.width,
            Const.SIZE_VIEWPORT.height);

    final EntityFactory entityFactory = new EntityFactory();

    /**
     * background of the level, it's full-black now
     */
    private final Background _background = new Background();

    /**
     * level entities
     */
    private final EntityList<Special> _specialEntityList = new EntityList<>();
    private final EntityList<AbstractTank> _tankList = new EntityList<>();
    private final EntityList<Bullet> _bulletList = new EntityList<>();
    private final EntityList<Effect> _effectList = new EntityList<>();
    private final FullQuadTree<Block> _blockTree = new FullQuadTree<>(new Rectangle(Const.SIZE_VIEWPORT));

    /**
     * store event queue, the level monitor will handle them
     */
    private final Queue<Event> _eventQueue = new LinkedList<>();

    private Level() {
    }

    // **** update level ***** //
    void updateLevel() {
        _tankList.updateEntities();
        _bulletList.updateEntities();
        _effectList.updateEntities();
        _specialEntityList.updateEntities();

        // add debug drawing line
//        Debugger.Draw.addLine("tank size: " + _tankList.size());
//        Debugger.Draw.addLine("bullet size: " + _bulletList.size());
    }

    // **** draw level **** //
    void drawLevel(Graphics2D g) {
        _background.draw(g);

        _blockTree.drawEntities(g);
        _bulletList.drawEntities(g);
        _tankList.drawEntities(g);
        _effectList.drawEntities(g);
        _specialEntityList.drawEntities(g);
    }

    Queue<Event> eventQueue() {
        return this._eventQueue;
    }

    // **** public methods could be access by entities objects****
    public boolean isInBound(Rectangle rect) {
        return LEVEL_BOUND.contains(rect);
    }

    public boolean isOutOfBound(Rectangle rect) {
        return !LEVEL_BOUND.intersects(rect);
    }

    /**
     * retrieve intersected entity objects
     *
     * @param retArea retrieve area
     * @return entity object set
     */
    public Set<EntityObject> retrieveIntersections(Rectangle retArea) {
        Set<EntityObject> retSet = new HashSet<>();

        // get intersect block-set
        for (Block b : _blockTree.retrieve(retArea)) {
            if (!b.isActivate())
                continue;
            if (b.intersects(retArea))
                retSet.add(b);
        }

        // get intersect tank(s)
        for (AbstractTank tank : _tankList.toArrayList()) {
            if (!tank.isActivate())
                continue;
            if (tank.intersects(retArea))
                retSet.add(tank);
        }

        // get intersect bullet(s)
        for (Bullet bullet : _bulletList.toArrayList()) {
            if (!bullet.isActivate())
                continue;
            if (bullet.intersects(retArea))
                retSet.add(bullet);
        }

        return retSet;
    }

    public void addEvent(Event ev) {
        this._eventQueue.add(ev);
    }

    public class EntityFactory {

        private EntityFactory() {
        }

        public void preparePlayerTank(int x, int y) {
            TankGenerator tg = TankGenerator.create(Type.PLAYER, 0, x, y, Level.this);
            _specialEntityList.add(tg);
        }

        public void createPlayer(int x, int y) {
            _tankList.add(PlayerTank.create(x, y, Level.this));
        }

        public void prepareEnemyTank(int modelId, int x, int y) {
            TankGenerator tg = TankGenerator.create(Type.ENEMY, modelId, x, y, Level.this);
            _specialEntityList.add(tg);
        }

        public void createEnemy(int modelId, int x, int y) {
            _tankList.add(EnemyTank.create(modelId, x, y, Level.this));
        }

        public void createBullet(int modelId, AbstractTank lchTank) {
            _bulletList.add(Bullet.create(modelId, lchTank, Level.this));
        }

        public void createEffect(int effectId, int x, int y, int aliveTime) {
            _effectList.add(Effect.create(effectId, x, y, aliveTime, Level.this));
        }

    }

    /**
     * using builder-pattern to build a level
     */
    public static class Builder {
        private Level _level;

        Builder() {
            this._level = new Level();
        }

        public void addBlockEntity(int modelId, int colIndex, int rowIndex) {
            Block block = new Block(modelId, this._level);
            block.setLocation(colIndex * Const.SIZE_UNIT.width, rowIndex * Const.SIZE_UNIT.height);
            this._level._blockTree.add(block);
        }

        public Level build() {

            // add eagle
            Eagle eagle = new Eagle(0, _level);
            eagle.setLocation(304, 480);
            _level._blockTree.add(eagle);

            return _level;
        }
    }
}

