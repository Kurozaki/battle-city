package com.yotwei.battlecity.game.object;

import com.yotwei.battlecity.game.object.block.*;
import com.yotwei.battlecity.game.object.bullet.AbstractBullet;
import com.yotwei.battlecity.game.object.special.TankCreator;
import com.yotwei.battlecity.game.object.tank.AbstractTank;
import com.yotwei.battlecity.game.object.tank.EnemyTank;
import com.yotwei.battlecity.game.object.tank.PlayerTank;

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

    static {

        //
        // init block id-class mapper
        //
        blockIdClassMapper = new HashMap<>();
        blockIdClassMapper.put(1, RedBrick.class);
        blockIdClassMapper.put(2, IronBlock.class);
        blockIdClassMapper.put(3, Grass.class);
        blockIdClassMapper.put(4, River.class);
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

    public static AbstractBullet createBullet(LevelContext lvlCtx, int typeId) {
        AbstractBullet bullet = new AbstractBullet(lvlCtx) {

        };
        bullet.setActive(true);
        return bullet;
    }
}
