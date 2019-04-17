package com.yotwei.battlecity.game;

import com.yotwei.battlecity.game.beans.LevelBean;
import com.yotwei.battlecity.game.components.GOBoundingBox;
import com.yotwei.battlecity.game.components.listeners.CollisionListener;
import com.yotwei.battlecity.game.datastruct.DefaultGameObjectGroup;
import com.yotwei.battlecity.game.datastruct.GridGameObjectGroup;
import com.yotwei.battlecity.game.datastruct.IGameObjectGroup;
import com.yotwei.battlecity.game.objects.GameObject;
import com.yotwei.battlecity.game.objects.prefabs.Item;
import com.yotwei.battlecity.game.objects.prefabs.tanks.EnemyTank;
import com.yotwei.battlecity.game.objects.prefabs.tanks.PlayerTank;
import com.yotwei.battlecity.game.objects.prefabs.tanks.Tank;
import com.yotwei.battlecity.game.objects.prefabs.tankspawn.TankSpawnPoint;
import com.yotwei.battlecity.game.objects.prefabs.tiles.Eagle;
import com.yotwei.battlecity.game.objects.prefabs.tiles.Tile;
import com.yotwei.battlecity.game.ui.TextSprite;
import com.yotwei.battlecity.game.ui.UIObject;
import com.yotwei.battlecity.util.Bundle;
import com.yotwei.battlecity.util.Constant;
import com.yotwei.battlecity.util.GameObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * Created by YotWei on 2019/3/16.
 */
public abstract class LevelHandler {

    private static final Logger logger = LoggerFactory.getLogger(LevelHandler.class);

    private PlayerMonitor playerMonitor;
    private EnemiesMonitor enemiesMonitor;
    private ItemCreator itemCreator;

    private int enemyCountOnScreen;

    protected LState levelState;

    /**
     * 计时器，每一帧+1
     */
    private int frameTicker;

    private Rectangle boundary;

    /**
     * 背景图，保证是一张 640x480 尺寸的图片
     */
    private BufferedImage backgroundImage;

    /**
     * 对不同类型的物体进行分组
     */
    private Map<String, IGameObjectGroup> objectGroups;

    /**
     * 基地
     */
    private Eagle eagle;

    /**
     * 该队列作用见 addObject() 方法
     */
    private Queue<Bundle> objectBufferQueue;

    /**
     * 存放 UI 物体
     */
    private Map<String, UIObject> uiObjectMap;

    protected LevelHandler() {
        levelState = LState.NORMAL;
    }

    /**
     * 添加新的物体组
     */
    private void registerGameObjectGroup(String name, IGameObjectGroup group) {
        if (objectGroups.containsKey(name))
            throw new RuntimeException("Duplicate group name: " + name);
        objectGroups.put(name, group);
    }

    /**
     * 初始化关卡
     */
    protected void initLevel(LevelBean level) {

        playerMonitor = PlayerMonitor.getInstance();
        enemiesMonitor = EnemiesMonitor.createInstance(level.getEnemyCounts());
        itemCreator = new ItemCreator(this);

        boundary = new Rectangle(Constant.WND_SIZE); // 设置关卡的边界
        backgroundImage = level.getBackground(); // 获取背景

        objectBufferQueue = new LinkedList<>();

        IGameObjectGroup tileGroup = new GridGameObjectGroup(boundary, 8, 8);
        IGameObjectGroup tankGroup = new DefaultGameObjectGroup();
        IGameObjectGroup bulletGroup = new DefaultGameObjectGroup();
        IGameObjectGroup tankSpawnPointGroup = new DefaultGameObjectGroup();
        IGameObjectGroup effectGroup = new DefaultGameObjectGroup();
        IGameObjectGroup itemGroup = new DefaultGameObjectGroup();

        //
        // 初始化 tilemap
        // 砖块是静态的，用格子结构存储砖块
        //
        int[][] tilemap = level.getTilemap();

        for (int i = 0; i < tilemap.length; i++) {
            for (int j = 0; j < tilemap[i].length; j++) {
                Tile tile = Tile.createInstance(
                        this,
                        tilemap[i][j],
                        j * Constant.UNIT_SIZE.width,
                        i * Constant.UNIT_SIZE.height);

                if (Objects.isNull(tile)) continue;
                tileGroup.add(tile);
            }
        }
        eagle = Tile.createEagle(this, 304, 448);
        tileGroup.add(eagle);

        //
        // 初始化敌人和玩家坦克
        //
        enemyCountOnScreen = level.getEnemyOnScreenMax();
        for (int i = 0; i < enemyCountOnScreen; i++) {
            Bundle spawnInfo = enemiesMonitor.nextEnemySpawnInfo();
            if (spawnInfo == null) {
                enemyCountOnScreen = i;
                break;
            }
            EnemyTank enemyTank = Tank.createEnemyInstance(
                    this,
                    spawnInfo.get("id"),
                    spawnInfo.get("x"),
                    spawnInfo.get("y"));
            TankSpawnPoint<EnemyTank> sp = TankSpawnPoint.createInstance(enemyTank);
            tankSpawnPointGroup.add(sp);
        }

        PlayerTank tank = Tank.createPlayerTankInstance(
                this,
                playerMonitor.spawnCoord.x,
                playerMonitor.spawnCoord.y);
        TankSpawnPoint<PlayerTank> tsp = TankSpawnPoint.createInstance(tank);
        tankSpawnPointGroup.add(tsp);

        //
        // 注册物体组
        //
        objectGroups = new HashMap<>();
        registerGameObjectGroup("tiles", tileGroup);
        registerGameObjectGroup("tanks", tankGroup);
        registerGameObjectGroup("bullets", bulletGroup);
        registerGameObjectGroup("tankSpawnPoints", tankSpawnPointGroup);
        registerGameObjectGroup("effects", effectGroup);
        registerGameObjectGroup("items", itemGroup);

        //
        // UI 部件
        //
        uiObjectMap = new HashMap<>();

        // 居中文本UI，用于显示 'Battle Finished' 和 'Game Over'
        TextSprite lStateText = TextSprite.createOutlineTextSprite(
                new Font("Consolas", Font.BOLD, 48),
                Color.WHITE,
                Color.GRAY);
        lStateText.setDrawCoordinate(TextSprite.DRAW_COORD_CENTER, TextSprite.DRAW_COORD_CENTER);
        uiObjectMap.put("LStateText", lStateText);

        TextSprite playerCountText = TextSprite.createOutlineTextSprite(
                new Font("Consolas", Font.BOLD, 18),
                new Color(255, 255, 255, 128),
                new Color(128, 128, 128, 128)
        );
        playerCountText.setDrawCoordinate(16, 464);
        playerCountText.setText("Player:" + playerMonitor.getRemainLives());
        uiObjectMap.put("PlayerCountText", playerCountText);
    }

    /**
     * 获取 UI 组件
     */
    @SuppressWarnings("unchecked")
    private <T extends UIObject> T getUIObject(String name) {
        return (T) uiObjectMap.get(name);
    }

    private void setLevelState(LState state) {
        if (levelState.index < state.index) {
            levelState = state;

            TextSprite text = getUIObject("LStateText");
            text.setText(levelState.toString());
        }
    }

    // ------------------------------------------
    //
    // 更新关卡内容
    //
    // ------------------------------------------

    protected void clearObjectBufferQueue() {
        while (!objectBufferQueue.isEmpty()) {

            Bundle bundle = objectBufferQueue.poll();
            // 获取组名和物体
            String groupName = bundle.get("groupName");
            GameObject gameObject = bundle.get("gameObject");

            objectGroups.get(groupName).add(gameObject);
        }
    }

    protected void updateFrameTicker() {
        frameTicker++;
    }

    protected void updateGameObjects() {
        // 道具
        itemCreator.update();

        // 所有物体
        for (IGameObjectGroup objectGroup : objectGroups.values()) {
            objectGroup.each(GameObject::onUpdate);
        }
    }

    /**
     * 碰撞检测
     */
    protected void handleObjectCollision() {

        final IGameObjectGroup tankGroup = objectGroups.get("tanks");
        final IGameObjectGroup bulletGroup = objectGroups.get("bullets");
        final IGameObjectGroup tileGroup = objectGroups.get("tiles");
        final IGameObjectGroup itemGroup = objectGroups.get("items");

        //
        // 对坦克进行碰撞和边界检查
        //
        tankGroup.each(tank -> {

            /*
             * 获取坦克的包围盒以及碰撞监听器
             */
            final GOBoundingBox tankBox = tank.getComponent("BoundingBox");
            final CollisionListener tankCollisionListener = tank.getListener("CollisionListener");

            if (null == tankCollisionListener)
                throw new RuntimeException("Tank must have a CollisionListener");

            /*
             * 坦克在地图边界上的检查
             */
            if (!boundary.contains(tankBox)) {
                // 超出边界，触发事件
                tankCollisionListener.onTouchBound(boundary);
            }

            /*
             * 坦克与砖块碰撞
             */
            tileGroup.retrieve(tankBox).forEach(tile -> {
                if (tile.getListener("CollisionListener") != null)
                    tankCollisionListener.onCollide(tile);
            });

            /*
             * 坦克碰撞检查
             */
            for (GameObject anotherTank : tankGroup.retrieve(tankBox)) {
                if (anotherTank == tank) continue;

                // 与坦克碰撞，触发事件
                tankCollisionListener.onCollide(anotherTank);
            }
        });

        /*
         * 炮弹与各个物体的碰撞
         */
        bulletGroup.each(bullet -> {

            final GOBoundingBox bulletBox = bullet.getComponent("BoundingBox");
            final CollisionListener bulletCollisionListener = bullet.getListener("CollisionListener");

            // 边界检测
            if (!boundary.contains(bulletBox)) {
                bulletCollisionListener.onTouchBound(boundary);
            }

            //
            // 与砖块碰撞
            //
            for (GameObject tile : tileGroup.retrieve(bulletBox)) {
                // 触发碰撞事件
                bulletCollisionListener.onCollide(tile);
            }

            for (GameObject tank : tankGroup.retrieve(bulletBox)) {
                // 与坦克碰撞
                bulletCollisionListener.onCollide(tank);
            }

            for (GameObject anoBullet : bulletGroup.retrieve(bulletBox)) {
                if (anoBullet == bullet) continue;  // 不需要与自己碰撞
                bulletCollisionListener.onCollide(anoBullet);
            }
        });

        /*
         * 检查道具碰撞
         */
        itemGroup.each(item -> {
            for (GameObject tank : tankGroup.retrieve(GameObjects.boundingBox(item))) {
                // 道具已经失效
                if (!item.isActive()) break;

                final CollisionListener<Item> collisionListener = item.getListener("CollisionListener");
                if (null == collisionListener)
                    continue;

                collisionListener.onCollide(tank);
            }
        });
    }

    /**
     * 检查基地是否被毁
     */
    protected void checkEagle() {
        if (levelState == LState.NORMAL && eagle.isDestroyed()) {
            GameController.INST.notifyGameOver();
            setLevelState(LState.GAME_OVER);
        }
    }

    // ------------------------------------------
    //
    // 绘制关卡的内容
    //
    // ------------------------------------------

    /**
     * 绘制背景
     */
    protected void drawBackground(Graphics2D g) {
        g.drawImage(backgroundImage, 0, 0, null);
    }

    /**
     * 绘制游戏物体
     */
    protected void drawGameObjects(Graphics2D g) {

        // 优先级队列，优先级比较的依据是物体的绘制优先级
        Queue<GameObject> drawQueue = new PriorityQueue<>(GameObjects.DRAW_PRIORITY_COMPARATOR);

        for (IGameObjectGroup objectGroup : objectGroups.values()) {
            objectGroup.each(drawQueue::add);
        }

        while (!drawQueue.isEmpty()) {
            GameObject go = drawQueue.poll();
            go.onDraw(g);
        }
    }

    /**
     * UI 绘制
     */
    protected void drawUIs(Graphics2D g) {
        for (UIObject ui : uiObjectMap.values()) {
            ui.draw(g);
        }
    }

    // ------------------------------------------
    //
    // 给 GameObject 提供使用的接口
    //
    // ------------------------------------------

    public int getFrameTicker() {
        return frameTicker;
    }

    /**
     * 将新创建的物体加入缓冲队列
     * 直接向 objectGroups 内的物体组添加物体有可能引发异常 {@link ConcurrentModificationException}
     * 因此扔入缓冲队列，然后最后做统一处理
     *
     * @param groupName 需要添加的物体组名
     * @param object    向物体组添加的物体
     */
    public void addObject(String groupName, GameObject object) {
        Bundle bundle = new Bundle();
        bundle.set("groupName", groupName);
        bundle.set("gameObject", object);
        objectBufferQueue.add(bundle);
    }

    /**
     * 检索指定物体组在特定矩形区域的物体集
     *
     * @param groupName 物体组名
     * @param retArea   检索的矩形区域
     * @return 检索到的物体集
     */
    public Set<GameObject> retrieve(String groupName, Rectangle retArea) {
        IGameObjectGroup group = objectGroups.get(groupName);
        if (group == null) {
            throw new RuntimeException("GameObjectGroup: " + groupName + " doesn't exist");
        }
        return group.retrieve(retArea);
    }

    public void triggerPlayerDeath(PlayerTank tank) {
        playerMonitor.decLive();
        if (playerMonitor.getRemainLives() > 0) {
            //
            // 玩家剩余生命不为 0，
            // 扣除生命并重新生成坦克
            //
            PlayerTank newTank = PlayerTank.createPlayerTankInstance(
                    this,
                    playerMonitor.spawnCoord.x,
                    playerMonitor.spawnCoord.y);
            TankSpawnPoint<PlayerTank> spawnPoint = TankSpawnPoint.createInstance(newTank);
            addObject("tankSpawnPoints", spawnPoint);

            // 重新设置 UI 文本
            TextSprite playerCountText = getUIObject("PlayerCountText");
            playerCountText.setText("Player:" + playerMonitor.getRemainLives());
        } else {
            // 玩家无剩余命数
            setLevelState(LState.GAME_OVER);
        }
    }

    public void triggerEnemyDeath(EnemyTank tank) {
        Bundle info = enemiesMonitor.nextEnemySpawnInfo();
        if (info == null) {
            if (enemyCountOnScreen > 0) enemyCountOnScreen--;
            if (enemyCountOnScreen == 0) {
                if (logger.isInfoEnabled()) {
                    logger.info("Enemy Clear.");
                }
                setLevelState(LState.BATTLE_FINISHED);
            }
        } else {
            EnemyTank newTank = EnemyTank.createEnemyInstance(
                    this,
                    info.get("id"),
                    info.get("x"),
                    info.get("y"));
            TankSpawnPoint<EnemyTank> spawnPoint = TankSpawnPoint.createInstance(newTank);
            addObject("tankSpawnPoints", spawnPoint);
        }
    }

    public void incPlayerLive(PlayerTank tank) {
        playerMonitor.incLive();

        // 重新设置 UI
        TextSprite playerCount = getUIObject("PlayerCountText");
        playerCount.setText("Player:" + playerMonitor.getRemainLives());
    }

    public Rectangle getBoundary() {
        return boundary;
    }

    public Eagle getEagle() {
        return eagle;
    }

    /**
     * 关卡状态
     */
    protected enum LState {

        NORMAL(0, ""),              // 普通状态
        GAME_OVER(2, "Game Over"),           // 玩家没有剩余的生命或基地被摧毁，触发 GameOver
        BATTLE_FINISHED(1, "Battle Finished");     // 关卡完成

        /**
         * index 表示状态优先级，作用在于避免覆盖，
         * 例如关卡 Game Over 状态不能转换到 Battle Finished
         */
        final int index;
        String string;

        LState(int index, String string) {
            this.index = index;
            this.string = string;
        }

        @Override
        public String toString() {
            return string;
        }
    }

    private static class ItemCreator {
        final LevelHandler lh;
        final Random rand;

        int ticker = 10 * 60;

        ItemCreator(LevelHandler lh) {
            this.lh = lh;
            this.rand = new Random();
        }

        void update() {
            ticker--;
            if (ticker == 0) {
                // 随机生成道具
                Dimension us = Constant.UNIT_SIZE;
                int c = rand.nextInt(lh.boundary.width / us.width - 1);
                int r = rand.nextInt(lh.boundary.height / us.height - 2);
                Item item = Item.randomInstance(
                        lh,
                        c * us.width + us.width / 2,
                        r * us.width + us.height / 2);
                lh.addObject("items", item);

                // 重置计数器
                ticker = rand.nextInt(2 * 60) + Item.ITEM_ALIVE_TICK;
            }
        }
    }
}