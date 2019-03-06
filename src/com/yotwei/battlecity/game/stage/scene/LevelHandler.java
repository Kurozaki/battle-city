package com.yotwei.battlecity.game.stage.scene;

import com.yotwei.battlecity.game.datastruct.IGameObjectGroup;
import com.yotwei.battlecity.game.engine.GameContext;
import com.yotwei.battlecity.game.engine.ResourcePackage;
import com.yotwei.battlecity.game.object.GameObject;
import com.yotwei.battlecity.game.object.GameObjectFactory;
import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.level.LevelPackage;
import com.yotwei.battlecity.game.object.block.AbstractBlock;
import com.yotwei.battlecity.game.object.bullet.AbstractBullet;
import com.yotwei.battlecity.game.object.properties.DrawAble;
import com.yotwei.battlecity.game.object.properties.LifeCycle;
import com.yotwei.battlecity.game.object.special.SpecialObject;
import com.yotwei.battlecity.game.object.special.TankCreator;
import com.yotwei.battlecity.game.object.tank.AbstractTank;
import com.yotwei.battlecity.game.object.tank.EnemyTank;
import com.yotwei.battlecity.game.object.tank.PlayerTank;
import com.yotwei.battlecity.game.player.Player;
import com.yotwei.battlecity.util.Constant;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Created by YotWei on 2019/2/27.
 */
public class LevelHandler extends AbstractScene
        implements LevelContext {

    /*
     * --------------------------------------------------------
     *
     * members for enemy control
     *
     * --------------------------------------------------------
     */
    private static final int ENEMY_DEQUEUE_PERIOD = 120;

    private Queue<TankCreator<EnemyTank>> enemyCreatorQueue;

    private int enemyOnScreenCount;
    private int enemyOnScreenCountMax;
    private int enemyOnceDequeueCountMax;

    private int enemyDequeueTicker;

    /*
     * --------------------------------------------------------
     *
     * members for control level global status
     *
     * --------------------------------------------------------
     */
    private Queue<Event> eventQueue;

    /**
     * frameTicker increases frame by frame
     * set to 0 when level switched
     */
    private int frameTicker;

    /**
     * a Rectangle instance
     * stands for the bound of level
     */
    private final Rectangle levelBoundRect = new Rectangle(Constant.WND_SIZE);


    /**
     * background image of level
     * it is tiled by a source image
     */
    private BufferedImage backgroundImage;

    /*
     * --------------------------------------------------------
     *
     * members for storing game objects
     *
     * --------------------------------------------------------
     */
    private Map<String, IGameObjectGroup<? extends GameObject>> gameObjectGroups;

    private IGameObjectGroup<AbstractBlock> blockGroup;
    private IGameObjectGroup<AbstractTank> tankGroup;
    private IGameObjectGroup<AbstractBullet> bulletGroup;
    private IGameObjectGroup<SpecialObject> specialObjectGroup;

    public LevelHandler(GameContext context) {
        super(context);
    }

    /*
     * -------------------------------------------------------
     *
     * methods implements from {@link AbstractScene}
     *
     * -------------------------------------------------------
     */
    @Override
    public void resetScene() {

        frameTicker = 0;

        eventQueue = new LinkedList<>();
        enemyDequeueTicker = ENEMY_DEQUEUE_PERIOD;

        //
        // re initialize all game object group
        //
        gameObjectGroups = new HashMap<>();
        tankGroup = IGameObjectGroup.create("QuadTree");
        blockGroup = IGameObjectGroup.create("Grid");
        bulletGroup = IGameObjectGroup.create("QuadTree");
        specialObjectGroup = IGameObjectGroup.create("Default");


        // get current handing level data
        LevelPackage.LevelData levelDatCurrent = getGameContext().getCurrentLevel();

        //
        // fill backgroundImage
        // the backgroundImage is tiled by source image
        //
        backgroundImage = new BufferedImage(Constant.WND_SIZE.width, Constant.WND_SIZE.height,
                BufferedImage.TYPE_INT_RGB);
        BufferedImage imgScr = ResourcePackage.getImage(levelDatCurrent.getBgpName());

        for (int dx = 0, sx = 0; dx < backgroundImage.getWidth(); dx++, sx++) {
            // ensure sx inbound
            if (sx == imgScr.getWidth()) sx = 0;

            for (int dy = 0, sy = 0; dy < backgroundImage.getHeight(); dy++, sy++) {
                // ensure sy inbound
                if (sy == imgScr.getHeight()) sy = 0;

                // set RGB value
                backgroundImage.setRGB(dx, dy, imgScr.getRGB(sx, sy));
            }
        }

        //
        // parse map into block group
        // consider that unit size is w*h
        // map[i][j] means block id at (j*w, i*h) is map[i][j]
        //
        int[][] mapArr2D = levelDatCurrent.getMap();
        int x, y, blockId;
        AbstractBlock block;
        Dimension unitSize = Constant.UNIT_SIZE;

        for (int i = 0; i < mapArr2D.length; i++) {

            // the second dimension
            for (int j = 0; j < mapArr2D[i].length; j++) {

                // block id is map[i][j]
                blockId = mapArr2D[i][j];

                if (0 == blockId) {
                    continue;
                }

                // (x, y) is the coordinate of the block
                x = j * unitSize.width;
                y = i * unitSize.height;

                // create a block by using GameObjectFactory
                block = GameObjectFactory.createBlock(this, blockId, x, y);

                blockGroup.add(block);
            }
        }

        for (Map.Entry<String, Point> entry :
                levelDatCurrent.getPlayersStartCoord().entrySet()) {

            String pname = entry.getKey();
            Point point = entry.getValue();

            Player player = getGameContext().getPlayer(pname);
            PlayerTank tank = GameObjectFactory.createPlayerTank(this, player.getId());

            TankCreator<PlayerTank> tankCreator =
                    GameObjectFactory.createTankCreator(this, point.x, point.y, tank);

            specialObjectGroup.add(tankCreator);
        }


        enemyOnScreenCountMax = levelDatCurrent.getEnemyOnScreenCountMax();

        //
        // fill enemyCreatorQueue
        //
        enemyCreatorQueue = new LinkedList<>();
        int selectedIndex = 0;
        List<Point> enemyCreatorSelectablePointList = Arrays.asList(
                new Point(0, 0),
                new Point(304, 0),
                new Point(608, 0)
        );

        enemyOnceDequeueCountMax = enemyCreatorSelectablePointList.size();

        for (Map.Entry<Integer, Integer> entry :
                levelDatCurrent.getEnemyTypesCount().entrySet()) {

            Integer id = entry.getKey();
            Integer count = entry.getValue();

            for (int i = 0; i < count; i++) {

                EnemyTank enemyTank = GameObjectFactory.createEnemyTank(this, id);
                Point selectedPoint = enemyCreatorSelectablePointList.get(selectedIndex);

                TankCreator<EnemyTank> anEnemyCreator = GameObjectFactory.createTankCreator(
                        this,
                        selectedPoint.x,
                        selectedPoint.y,
                        enemyTank);
                enemyCreatorQueue.add(anEnemyCreator);

                if (++selectedIndex == enemyCreatorSelectablePointList.size()) {
                    Collections.shuffle(enemyCreatorSelectablePointList);
                    selectedIndex = 0;
                }
            }
        }

        //
        // initialize GameObject Groups
        // then register all IGameObjectGroup instance to gameObjectGroup
        //
        gameObjectGroups = new HashMap<>();
        gameObjectGroups.put("blockGroup", blockGroup);
        gameObjectGroups.put("tankGroup", tankGroup);
        gameObjectGroups.put("bulletGroup", bulletGroup);
        gameObjectGroups.put("specialObjectGroup", specialObjectGroup);
    }

    @Override
    public void updateScene() {

        frameTicker++;

        updateGameObjectsGroupBuGroup();
        handleGameObjectsCollision();

        handleEnemyCreators();

        handleEventQueue();
    }


    @Override
    public void drawScene(Graphics2D g) {

        // draw background
        g.drawImage(backgroundImage, 0, 0, null);

        // draw game objects
        drawGameObjects(g);
    }


    @Override
    public boolean isSceneFinished() {
        return false;
    }

    /*
     * -------------------------------------------------------
     *
     * method implements from {@link LevelContext}
     *
     * -------------------------------------------------------
     */
    @Override
    public int getFrameTicker() {
        return frameTicker;
    }

    @Override
    public Rectangle getLevelBound() {
        return levelBoundRect;
    }

    @Override
    public Set<GameObject> retrieveGameObject(
            Rectangle retrieveArea,
            Set<String> retrieveGroupNames,
            RetrieveFilter<GameObject> filter) {

        Set<GameObject> resultSet = new HashSet<>();

        if (retrieveGroupNames == null) {
            retrieveGroupNames = gameObjectGroups.keySet();

        }
        for (String groupName : retrieveGroupNames) {
            Set<? extends GameObject> retSet =
                    gameObjectGroups.get(groupName).retrieve(retrieveArea);
            for (GameObject anObject : retSet) {
                if (filter.filter(anObject))
                    resultSet.add(anObject);
            }
        }
        return resultSet;
    }

    @Override
    public void triggerEvent(Event ev) {
        eventQueue.add(ev);
    }

    /*
     * -------------------------------------------------------
     *
     * methods for handing event queue
     *
     * -------------------------------------------------------
     */
    private void handleEventQueue() {
        while (!eventQueue.isEmpty()) {
            Event ev = eventQueue.poll();

            switch (ev.evTag) {
                case "addObject":
                    handleAddObjectEvent((GameObject) ev.dat[0]);
                    break;

                case "enemyDeath":
                    handleEnemyDeathEvent();
                    break;

                default:
            }
        }
    }

    private void handleAddObjectEvent(GameObject anObject) {
        if (anObject instanceof AbstractTank) {
            tankGroup.add((AbstractTank) anObject);
        } else if (anObject instanceof AbstractBullet) {
            bulletGroup.add(((AbstractBullet) anObject));
        }
    }

    private void handleEnemyDeathEvent() {
        enemyOnScreenCount--;
    }

    /*
     * -------------------------------------------------------
     *
     * assistant methods for updateScene()
     *
     * -------------------------------------------------------
     */

    private void updateGameObjectsGroupBuGroup() {
        for (Map.Entry<String, IGameObjectGroup<? extends GameObject>> entry :
                gameObjectGroups.entrySet()) {
            IGameObjectGroup<? extends GameObject> objectGroup = entry.getValue();
            objectGroup.each(LifeCycle::update);
        }
    }

    private void handleGameObjectsCollision() {
        //
        // tank in bound check
        //
        tankGroup.each(tank -> {
            if (!levelBoundRect.contains(tank.getHitbox()))
                tank.onTouchBound(levelBoundRect);
        });

        //
        // check tank collide with blocks
        //
        tankGroup.each(tank -> {

            // retrieve blocks collide with tank
            Set<AbstractBlock> retSet = blockGroup.retrieve(tank.getHitbox());
            if (retSet.isEmpty()) return;

            // get the first element in retrieve set
            for (AbstractBlock aBlock : retSet) {

                // invoke block and tank's onCollide() method
                aBlock.onCollide(tank);
                tank.onCollide(aBlock);
            }
        });

        //
        // check tank collide with tank
        //
        tankGroup.each(tank -> {

            // retrieve tank set collide with checking tank
            Set<AbstractTank> collideTankSet = tankGroup.retrieve(tank.getHitbox());

            for (AbstractTank anotherTank : collideTankSet) {
                if (anotherTank == tank)
                    continue;

                // invoke onCollide() method
                tank.onCollide(anotherTank);
            }
        });

    }

    private void handleEnemyCreators() {
        if (enemyDequeueTicker++ < ENEMY_DEQUEUE_PERIOD)
            return;

        enemyDequeueTicker = 0;

        //
        // dequeue enemy creators in queue
        //
        int dequeueCount = Math.min(
                enemyOnScreenCountMax - enemyOnScreenCount,
                enemyOnceDequeueCountMax);

        for (int i = 0; i < dequeueCount; i++) {
            TankCreator<EnemyTank> creator = enemyCreatorQueue.poll();
            if (creator == null) {
                // TODO: 2019/3/6 handle all enemy clear
                break;
            }
            specialObjectGroup.add(creator);
            enemyOnScreenCount++;
        }
    }

    /*
     * -------------------------------------------------------
     *
     * assistant methods for drawScene()
     *
     * -------------------------------------------------------
     */

    /**
     * draw according to the drawPriority of GameObject
     * using {@link PriorityQueue} is convenient
     */
    private void drawGameObjects(Graphics2D g) {

        Queue<DrawAble> drawQueue = new PriorityQueue<>(DrawAble.DrawPriorityComparator);

        for (Map.Entry<String, IGameObjectGroup<? extends GameObject>> entry :
                gameObjectGroups.entrySet()) {

            // add every GameObject to priority queue
            entry.getValue().each(drawQueue::add);
        }

        while (!drawQueue.isEmpty()) {
            drawQueue.poll().draw(g);
        }
    }
}
