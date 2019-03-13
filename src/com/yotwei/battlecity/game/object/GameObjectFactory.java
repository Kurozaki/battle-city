package com.yotwei.battlecity.game.object;

import com.yotwei.battlecity.game.object.block.*;
import com.yotwei.battlecity.game.object.bullet.*;
import com.yotwei.battlecity.game.object.effect.Effect;
import com.yotwei.battlecity.game.object.item.*;
import com.yotwei.battlecity.game.object.special.TankCreator;
import com.yotwei.battlecity.game.object.tank.AbstractTank;
import com.yotwei.battlecity.game.object.tank.EnemyTank;
import com.yotwei.battlecity.game.object.tank.PlayerTank;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YotWei on 2019/2/28.
 */
public class GameObjectFactory {

    /*
     * blockIdClassMapper storing mapping from blockId to AbstractBlock subclass
     * it is used in findBlockClassById() method
     */
    private static Map<Integer, Class<? extends AbstractBlock>> blockIdClassMapper;

    /*
     * blockIdClassMapper storing mapping from bulletId to AbstractBullet subclass
     * it is used in findBulletClassById() method
     */
    private static Map<Integer, Class<? extends AbstractBullet>> bulletIdClassMapper;

    private static Map<Integer, Class<? extends AbstractItem>> itemIdClassMapper;

    static {

        //
        // init block id-class mapper
        //
        blockIdClassMapper = new HashMap<>();
        blockIdClassMapper.put(1, RedBrick.class);
        blockIdClassMapper.put(2, IronBlock.class);
        blockIdClassMapper.put(3, Grass.class);
        blockIdClassMapper.put(4, River.class);

        bulletIdClassMapper = new HashMap<>();
        bulletIdClassMapper.put(1, GenericBullet.class);
        bulletIdClassMapper.put(2, FrozenBullet.class);
        bulletIdClassMapper.put(3, ArmourPiercingBullet.class);
        bulletIdClassMapper.put(4, BurstBullet.class);

        itemIdClassMapper = new HashMap<>();
        itemIdClassMapper.put(1, AddPlayerLiveItem.class);
        itemIdClassMapper.put(2, AddTankBulletSlotItem.class);
        itemIdClassMapper.put(3, ClearEnemyItem.class);
        itemIdClassMapper.put(4, FreezeEnemyItem.class);
        itemIdClassMapper.put(5, InvincibleItem.class);
        itemIdClassMapper.put(6, StrengthenEagleAroundBlockItem.class);
        itemIdClassMapper.put(7, TankSpeedUpItem.class);
    }

    private GameObjectFactory() {
    }

    /**
     * create a {@link AbstractBlock} instance by block id
     * and set block location
     *
     * @param lvlCtx  {@link LevelContext} instance
     * @param blockId id of the block
     * @param x       block's x-position
     * @param y       block's y-position
     * @return an AbstractBlock instance
     */
    public static AbstractBlock createBlock(LevelContext lvlCtx, int blockId, int x, int y) {
        AbstractBlock block;

        try {
            // get block class by using blockId
            Class<? extends AbstractBlock> blockClass = findBlockClassById(blockId);

            //
            // get constructor through reflection
            // the constructor declaration must be [CLASS_NAME] (LevelContext lvlCtx, int typeId) {}
            //
            Constructor<? extends AbstractBlock> constructor =
                    blockClass.getDeclaredConstructor(LevelContext.class, int.class);
            constructor.setAccessible(true);

            // create an instance through reflection
            block = constructor.newInstance(lvlCtx, blockId);

        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("Error at create block: id=" + blockId);
        }

        // don't forget to set block's location
        block.getHitbox().setLocation(x, y);

        // active the block
        block.setActive(true);

        return block;
    }

    /**
     * create a {@link AbstractBullet} subclass instance
     * then init bullet's coordinate and direction
     */
    public static AbstractBullet createBullet(AbstractTank associateTank, int typeId) {
        AbstractBullet bullet;

        try {
            // get bullet class
            Class<? extends AbstractBullet> bulletClass = findBulletClassById(typeId);

            // get constructor through reflection
            Constructor<? extends AbstractBullet> constructor =
                    bulletClass.getDeclaredConstructor(LevelContext.class, int.class, AbstractTank.class);
            constructor.setAccessible(true);

            // create a bullet instance
            bullet = constructor
                    .newInstance(associateTank.getLevelContext(), typeId, associateTank);

        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("Error at create bullet: id=" + typeId);
        }

        bullet.setDirection(associateTank.getDirection());

        // active bullet
        bullet.setActive(true);

        return bullet;
    }

    /**
     * get a class-type instance that class-type extends {@link AbstractBlock}
     * if id is illegal, throw {@link RuntimeException}
     *
     * @param id the id of the block
     * @return a class-type instance
     */
    private static Class<? extends AbstractBlock> findBlockClassById(int id) {
        Class<? extends AbstractBlock> blockClass = blockIdClassMapper.get(id);
        if (blockClass == null) {
            throw new RuntimeException("Illegal block id: " + id);
        }
        return blockClass;
    }

    /**
     * get a class-type instance that class-type extends {@link AbstractBullet}
     * if id is illegal, throw RuntimeException
     *
     * @param id the id of bullet
     * @return class-type instance
     */
    private static Class<? extends AbstractBullet> findBulletClassById(int id) {
        Class<? extends AbstractBullet> bulletClass = bulletIdClassMapper.get(id);
        if (bulletClass == null) {
            throw new RuntimeException("Illegal bullet id: " + id);
        }
        return bulletClass;
    }

    public static PlayerTank createPlayerTank(LevelContext lvlCtx, int playerId) {

        PlayerTank playerTank = new PlayerTank(lvlCtx, playerId);
        playerTank.setControlKeys(PlayerTank.ControlKeys.createByPlayerId(playerId));

        // active tank
        playerTank.setActive(true);

        return playerTank;
    }

    public static EnemyTank createEnemyTank(LevelContext lvlCtx, int enemyId) {

        EnemyTank enemyTank = new EnemyTank(lvlCtx, enemyId);
        enemyTank.setActive(true);

        return enemyTank;
    }

    public static <_TankType extends AbstractTank> TankCreator<_TankType> createTankCreator(
            LevelContext lvlCtx,
            int x,
            int y,
            _TankType tank) {

        TankCreator<_TankType> creator = new TankCreator<>(lvlCtx, tank);
        creator.getHitbox().setLocation(x, y);

        // active the tank creator
        creator.setActive(true);

        return creator;
    }

    public static Effect createEffect(LevelContext lvlCtx, int id) {

        Effect e = new Effect(lvlCtx);
        e.setId(id);

        switch (id) {
            case 1:
                e.setFrameSize(16, 16);
                e.setFrameCount(8);
                e.setFrameInterval(4);
                break;

            case 2:
                e.setFrameSize(32, 32);
                e.setFrameCount(8);
                e.setFrameInterval(4);
                break;

            case 3:
                e.setFrameSize(16, 16);
                e.setFrameCount(4);
                e.setFrameInterval(2);
                break;

            default:
                throw new RuntimeException("Illegal effect id: " + id);
        }

        e.setActive(true);

        return e;
    }

    public static AbstractItem createItem(LevelContext lvlCtx, int id, int x, int y) {
        AbstractItem item = null;

        Class<? extends AbstractItem> itemClass = findItemClassById(id);

        try {
            Constructor<? extends AbstractItem> constructor = itemClass.getDeclaredConstructor(LevelContext.class, int.class);
            constructor.setAccessible(true);
            item = constructor.newInstance(lvlCtx, id);
            item.setActive(true);
            item.getHitbox().setLocation(x, y);


        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return item;
    }

    private static Class<? extends AbstractItem> findItemClassById(int id) {
        Class<? extends AbstractItem> itemClass = itemIdClassMapper.get(id);
        if (itemClass == null) {
            throw new RuntimeException("bad item id: " + id);
        }
        return itemClass;
    }
}
