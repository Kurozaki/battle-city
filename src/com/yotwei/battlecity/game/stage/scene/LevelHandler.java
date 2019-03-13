package com.yotwei.battlecity.game.stage.scene;

import com.yotwei.battlecity.game.datastruct.IGameObjectGroup;
import com.yotwei.battlecity.game.engine.GameContext;
import com.yotwei.battlecity.game.engine.ResourcePackage;
import com.yotwei.battlecity.game.object.GameObject;
import com.yotwei.battlecity.game.object.GameObjectFactory;
import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.level.LevelPackage;
import com.yotwei.battlecity.game.object.block.AbstractBlock;
import com.yotwei.battlecity.game.object.block.Eagle;
import com.yotwei.battlecity.game.object.bullet.AbstractBullet;
import com.yotwei.battlecity.game.object.effect.Effect;
import com.yotwei.battlecity.game.object.item.*;
import com.yotwei.battlecity.game.object.properties.DrawAble;
import com.yotwei.battlecity.game.object.properties.LifeCycle;
import com.yotwei.battlecity.game.object.special.SpecialObject;
import com.yotwei.battlecity.game.object.special.TankCreator;
import com.yotwei.battlecity.game.object.tank.AbstractTank;
import com.yotwei.battlecity.game.object.tank.EnemyTank;
import com.yotwei.battlecity.game.object.tank.PlayerTank;
import com.yotwei.battlecity.game.player.Player;
import com.yotwei.battlecity.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Created by YotWei on 2019/2/27.
 */
public class LevelHandler extends AbstractScene
        implements LevelContext {

    private static final Logger logger = LoggerFactory.getLogger("LevelHandler");
    private static final int ENEMY_DEQUEUE_PERIOD = 120;
    private static final int LEVEL_FINISHED_STAY_TICK = 120;
    private static final Point EAGLE_POINT = new Point(304, 448);

    private int itemTicker = 5 * 60;

    /*
     * members for player control
     */
    private Map<Integer, Player> players;

    /*
     * --------------------------------------------------------
     *
     * members for enemy control
     *
     * --------------------------------------------------------
     */

    private Queue<TankCreator<EnemyTank>> enemyCreatorQueue;

    private int enemyRemainCount;
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

    private int levelFinishedTicker;

    private boolean isLevelFinished;

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

    private Eagle eagle;

    private IGameObjectGroup<AbstractBlock> blockGroup;
    private IGameObjectGroup<AbstractTank> tankGroup;
    private IGameObjectGroup<AbstractBullet> bulletGroup;
    private IGameObjectGroup<Effect> effectGroup;
    private IGameObjectGroup<AbstractItem> itemGroup;
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

        enemyRemainCount = 0;
        enemyDequeueTicker = ENEMY_DEQUEUE_PERIOD;

        levelFinishedTicker = LEVEL_FINISHED_STAY_TICK;
        isLevelFinished = false;

        players = new HashMap<>();
        eventQueue = new LinkedList<>();

        //
        // re initialize all game object group
        //
        gameObjectGroups = new HashMap<>();
        tankGroup = IGameObjectGroup.create("QuadTree");
        blockGroup = IGameObjectGroup.create("Grid");
        bulletGroup = IGameObjectGroup.create("QuadTree");
        specialObjectGroup = IGameObjectGroup.create("Default");
        effectGroup = IGameObjectGroup.create("Default");
        itemGroup = IGameObjectGroup.create("Default");

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

        //
        // add eagle into block group
        //
        eagle = new Eagle(this);
        eagle.setActive(true);
        eagle.getHitbox().setLocation(EAGLE_POINT);
        blockGroup.add(eagle);

        for (Map.Entry<String, Point> entry :
                levelDatCurrent.getPlayersStartCoord().entrySet()) {

            // reset players' start point
            Player player = getGameContext().getPlayer(entry.getKey());
            players.put(player.getId(), player);
            player.setStartPoint(entry.getValue());
        }

        // add player tank creators
        for (Player player : players.values()) {
            PlayerTank playerTank =
                    GameObjectFactory.createPlayerTank(this, player.getId());
            TankCreator<PlayerTank> tankCreator = GameObjectFactory.createTankCreator(
                    this,
                    player.getStartPoint().x,
                    player.getStartPoint().y,
                    playerTank);
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
                enemyRemainCount++;

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
        gameObjectGroups.put("effectGroup", effectGroup);
        gameObjectGroups.put("itemGroup", itemGroup);
        gameObjectGroups.put("specialObjectGroup", specialObjectGroup);
    }

    @Override
    public void updateScene() {

        frameTicker++;

        updateGameObjectsGroupBuGroup();
        handleGameObjectsCollision();

        handleEnemyCreators();

        handleEventQueue();

        handleItem();

        checkLevelStatus();
    }


    @Override
    public void drawScene(Graphics2D g) {

        // draw background
        g.drawImage(backgroundImage, 0, 0, null);

        // draw game objects
        drawGameObjects(g);

        drawHUD(g);
    }


    @Override
    public boolean isSceneFinished() {
        return levelFinishedTicker == 0;
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

        Queue<Event> newEventQueue = new LinkedList<>();

        while (!eventQueue.isEmpty()) {
            Event ev = eventQueue.poll();

            switch (ev.evTag) {
                case "addObject":
                    handleAddObjectEvent((GameObject) ev.dat[0]);
                    break;

                case "enemyDeath":
                    handleEnemyDeathEvent();
                    break;

                case "playerDeath":
                    handlePlayerDeathEvent((int) ev.dat[0]);
                    break;

                case "eagleDestroyed":
                    isLevelFinished = true;
                    getGameContext().notifyGameOver();
                    break;

                case "clearEnemyOnScreen":
                    clearEnemyOnScreen();
                    break;

                case "strengthenBlocksAroundEagle":
                    changeBlockAroundEagle(true);
                    // create a delay trigger event
                    Event delayEv = Event.wrap(
                            "delay",
                            60 * 20,    /* delay time (frame) */
                            Event.wrap("weakenBlocksAroundEagle") /* delat event */
                    );
                    newEventQueue.add(delayEv);
                    break;

                case "weakenBlocksAroundEagle":
                    changeBlockAroundEagle(false);
                    break;

                case "addPlayerLiveCount":
                    int playerId = (int) ev.dat[0];
                    players.get(playerId).incLiveCount();
                    break;

                case "freezeEnemies":
                    handleFreezeEnemiesEvent();
                    break;

                case "delay":
                    int remainTime = (int) ev.dat[0];
                    if (remainTime == 0) {
                        ev = (Event) ev.dat[1];
                    } else {
                        ev.dat[0] = remainTime - 1;
                    }
                    newEventQueue.add(ev);
                    break;

                default:
            }
        }

        while (!newEventQueue.isEmpty()) {
            eventQueue.add(newEventQueue.poll());
        }
    }

    private void handleFreezeEnemiesEvent() {
        tankGroup.each(tank -> {
            if (!tank.getTag().equals("Tank-Enemy"))
                return;
            tank.getExtra().put("freeze", 60 * 6 /* freeze for 5s */);
        });
    }

    private void handleAddObjectEvent(GameObject anObject) {
        if (anObject instanceof AbstractTank) {
            tankGroup.add((AbstractTank) anObject);
        } else if (anObject instanceof AbstractBullet) {
            bulletGroup.add(((AbstractBullet) anObject));
        } else if (anObject instanceof Effect) {
            effectGroup.add((Effect) anObject);
        }
    }

    private void handleEnemyDeathEvent() {
        enemyOnScreenCount--;
        enemyRemainCount--;

        if (logger.isInfoEnabled()) {
            logger.info("Enemy Remain: {}", enemyRemainCount);
        }

        if (enemyRemainCount == 0) {
            isLevelFinished = true;
        }
    }

    private void handlePlayerDeathEvent(int playerId) {
        if (logger.isInfoEnabled()) {
            logger.info("kill player{ id={} }", playerId);
        }

        Player player = players.get(playerId);

        if (player.decLiveCount() == 0) {

            if (logger.isInfoEnabled()) {
                logger.info("Player{ id={} } death", playerId);
            }
            isLevelFinished = true;
            getGameContext().notifyGameOver();
            return;
        }

        PlayerTank tank = GameObjectFactory.createPlayerTank(this, player.getId());
        TankCreator<PlayerTank> creator = GameObjectFactory.createTankCreator(
                this,
                player.getStartPoint().x,
                player.getStartPoint().y,
                tank
        );
        specialObjectGroup.add(creator);
    }

    private void clearEnemyOnScreen() {
        tankGroup.each(tank -> {
            if (tank.getTag().equals("Tank-Enemy")) {
                tank.setActive(false);
            }
        });
    }

    private void changeBlockAroundEagle(boolean isStrengthen) {

        Rectangle eagleHitbox = eagle.getHitbox();
        Rectangle retArea = new Rectangle(eagleHitbox.width << 1, eagleHitbox.height << 1);
        retArea.setLocation(
                eagleHitbox.x + (eagleHitbox.width - retArea.width >> 1),
                eagleHitbox.y + (eagleHitbox.height - retArea.height >> 1)
        );

        for (AbstractBlock block : blockGroup.retrieve(retArea)) {

            if (isStrengthen) {
                switch (block.getTag()) {
                    case "Block-RedBrick":
                        //
                        // replace RedBrick with IronBlock
                        //
                        AbstractBlock newBlock = GameObjectFactory.createBlock(
                                this,
                                2,  // id of iron block is 2
                                block.getHitbox().x,
                                block.getHitbox().y
                        );

                        blockGroup.add(newBlock);
                        blockGroup.remove(block);
                        break;
                }
            } else {

                switch (block.getTag()) {
                    case "Block-IronBlock":
                        //
                        // replace RedBrick with IronBlock
                        //
                        AbstractBlock newBlock = GameObjectFactory.createBlock(
                                this,
                                1,
                                block.getHitbox().x,
                                block.getHitbox().y
                        );
                        blockGroup.add(newBlock);
                        blockGroup.remove(block);

                        break;
                }
            }
        }
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

        //
        // tank with items
        //
        tankGroup.each(tank -> {
            Set<AbstractItem> itemSet = itemGroup.retrieve(tank.getHitbox());
            for (AbstractItem item : itemSet) {
                item.onCollide(tank);
            }
        });

        //
        // check bullet inbound
        //
        bulletGroup.each(bullet -> {
            if (!levelBoundRect.contains(bullet.getHitbox())) {
                bullet.onTouchBound(levelBoundRect);
            }
        });

        //
        // bullet collide with blocks
        //
        bulletGroup.each(bullet -> {

            // get retrieve set that collide with bullet
            Set<AbstractBlock> retBlockSet = blockGroup.retrieve(bullet.getHitbox());

            // check collide with every block
            for (AbstractBlock aBlock : retBlockSet) {
                bullet.onCollide(aBlock);

                if (!bullet.isActive()) break;
            }
        });

        //
        // bullet collide with tank
        //
        bulletGroup.each(bullet -> {

            // retrieve collided tank set
            Set<AbstractTank> retTankSet = tankGroup.retrieve(bullet.getHitbox());

            for (AbstractTank aTank : retTankSet) {
                bullet.onCollide(aTank);

                if (!bullet.isActive()) {
                    break;
                }
            }
        });

        //
        // bullet collide with bullet
        //
        bulletGroup.each(bullet -> {

            Set<AbstractBullet> retBulletSet = bulletGroup.retrieve(bullet.getHitbox());

            for (AbstractBullet anotherBullet : retBulletSet) {
                if (anotherBullet == bullet) continue;

                bullet.onCollide(anotherBullet);

                if (!bullet.isActive()) {
                    return;
                }
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
                // enemy clear
                break;
            }

            specialObjectGroup.add(creator);
            enemyOnScreenCount++;
        }
    }


    private void checkLevelStatus() {
        if (isLevelFinished) {
            levelFinishedTicker--;
        }
    }

    private void handleItem() {
        if (itemTicker-- <= 0) {
            Random rand = new Random();

            int id = rand.nextInt(7) + 1;
            int x = rand.nextInt(39) * 16 + 8;
            int y = rand.nextInt(29) * 16 + 8;

            AbstractItem item = GameObjectFactory.createItem(this, id, x, y);
            itemGroup.add(item);

            itemTicker = 15 * 60;
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

    private void drawHUD(Graphics2D g) {
        // TODO: 2019/3/8 完善UI绘制
        g.setColor(Color.WHITE);
        for (Player player : players.values()) {
            g.drawString(String.format("%s lives: %d", player.getName(), player.getLiveCount()), 10, 20);
        }
    }
}
