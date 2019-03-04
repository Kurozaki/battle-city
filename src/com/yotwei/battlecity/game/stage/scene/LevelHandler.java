package com.yotwei.battlecity.game.stage.scene;

import com.yotwei.battlecity.game.datastruct.IGameObjectGroup;
import com.yotwei.battlecity.game.engine.GameContext;
import com.yotwei.battlecity.game.engine.ResourcePackage;
import com.yotwei.battlecity.game.object.GameObject;
import com.yotwei.battlecity.game.object.GameObjectFactory;
import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.level.LevelPackage;
import com.yotwei.battlecity.game.object.block.AbstractBlock;
import com.yotwei.battlecity.game.object.properties.DrawAble;
import com.yotwei.battlecity.game.object.properties.LifeCycle;
import com.yotwei.battlecity.game.object.tank.AbstractTank;
import com.yotwei.battlecity.game.object.tank.EnemyTank;
import com.yotwei.battlecity.game.object.tank.PlayerTank;
import com.yotwei.battlecity.util.Constant;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * Created by YotWei on 2019/2/27.
 */
public class LevelHandler extends AbstractScene
        implements LevelContext {

    /*
     * a Rectangle instance
     * stands for the bound of level
     */
    private final Rectangle levelBoundRect = new Rectangle(Constant.WND_SIZE);

    /*
     * frameTicker increases frame by frame
     * set to 0 when level switched
     */
    private int frameTicker;

    private Map<String, IGameObjectGroup<? extends GameObject>> gameObjectGroups;

    /*
     * background image of level
     * it is tiled by a source image
     */
    private BufferedImage backgroundImage;

    private IGameObjectGroup<AbstractBlock> blockGroup = IGameObjectGroup.create("Grid1");
    private IGameObjectGroup<AbstractTank> tankGroup = IGameObjectGroup.create("QuadTree");

    public LevelHandler(GameContext context) {
        super(context);
    }

    @Override
    public void resetScene() {

        // set frameTicker to 0
        frameTicker = 0;

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
        // TODO: 2019/3/4 test code, delete later
        //
        PlayerTank tank = new PlayerTank(this, 1);
        tank.setControlKeys(new PlayerTank.ControlKeys(
                KeyEvent.VK_W,
                KeyEvent.VK_S,
                KeyEvent.VK_A,
                KeyEvent.VK_D,
                0,
                0
        ));
        tank.getHitbox().setLocation(0, 32);
        tank.onActive();

        tankGroup.add(tank);

        EnemyTank enemyTank = new EnemyTank(this, 2);
        enemyTank.getHitbox().setLocation(32, 0);
        enemyTank.onActive();
        tankGroup.add(enemyTank);


        //
        // init GameObject Groups
        // register all IGameObjectGroup instance to gameObjectGroup
        //
        gameObjectGroups = new HashMap<>();
        gameObjectGroups.put("blockGroup", blockGroup);
        gameObjectGroups.put("tankGroup", tankGroup);
    }

    @Override
    public void updateScene() {

        frameTicker++;

        //
        // update game objects group by group
        //
        for (Map.Entry<String, IGameObjectGroup<? extends GameObject>> entry :
                gameObjectGroups.entrySet()) {
            IGameObjectGroup<? extends GameObject> objectGroup = entry.getValue();
            objectGroup.each(LifeCycle::update);
        }

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
                anotherTank.onCollide(tank);
            }
        });

        // TODO: 2019/3/4 check bullet collide with tanks and blocks

    }

    @Override
    public void drawScene(Graphics2D g) {

        // draw background
        g.drawImage(backgroundImage, 0, 0, null);

        // draw game objects
        drawGameObjects(g);
    }

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

    @Override
    public boolean isSceneFinished() {
        return false;
    }

    /**
     * -------------------------------------------------------
     * <p>
     * method implements from {@link LevelContext}
     * <p>
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
}
