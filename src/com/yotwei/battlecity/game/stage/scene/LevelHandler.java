package com.yotwei.battlecity.game.stage.scene;

import com.yotwei.battlecity.game.datastruct.DefaultGameObjectGroup;
import com.yotwei.battlecity.game.datastruct.IGameObjectGroup;
import com.yotwei.battlecity.game.engine.GameContext;
import com.yotwei.battlecity.game.object.GameObjectFactory;
import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.level.LevelPackage;
import com.yotwei.battlecity.game.object.block.AbstractBlock;
import com.yotwei.battlecity.util.Constant;
import com.yotwei.battlecity.util.GraphicUtil;

import java.awt.*;

/**
 * Created by YotWei on 2019/2/27.
 */
public class LevelHandler extends AbstractScene
        implements LevelContext {

    /*
     * frameTicker increases frame by frame
     * set to 0 when level switched
     */
    private int frameTicker;

    private IGameObjectGroup<AbstractBlock> blockGroup = new DefaultGameObjectGroup<>();

    public LevelHandler(GameContext context) {
        super(context);
    }

    @Override
    public void resetScene() {

        // level switched, reset frameTicker to 0
        frameTicker = 0;

        // get current handing level data
        LevelPackage.LevelData levelDatCurrent = getGameContext().getCurrentLevel();

        //
        // parse map into block group
        // consider that unit size is w*h
        // map[i][j] means block id at (j*w, i*h) is map[i][j]
        //
        int[][] mapArr2D = levelDatCurrent.getMap();
        int x, y, blockId;
        AbstractBlock block;

        for (int i = 0; i < mapArr2D.length; i++) {

            // the second dimension
            for (int j = 0; j < mapArr2D[i].length; j++) {

                // block id is map[i][j]
                blockId = mapArr2D[i][j];

                if (0 == blockId) {
                    continue;
                }

                // (x, y) is the coordinate of the block
                x = j * Constant.UNIT_SIZE.width;
                y = i * Constant.UNIT_SIZE.height;

                // get the block instance by using GameObjectFactory
                block = GameObjectFactory.createBlock(this, blockId, x, y);

                blockGroup.add(block);
            }
        }


    }

    @Override
    public void updateScene() {
        frameTicker++;
    }

    @Override
    public void drawScene(Graphics2D g) {
        drawBackground(g);
        drawGameObjects(g);
    }

    private void drawBackground(Graphics2D g) {
        GraphicUtil.clearScreen(g, Color.BLACK);
    }

    private void drawGameObjects(Graphics2D g) {
        blockGroup.each(ab -> ab.draw(g));
    }

    @Override
    public boolean isSceneFinished() {
        return false;
    }

    /**
     * method implements from {@link LevelContext}
     *
     * @return int value
     */
    @Override
    public int getFrameTicker() {
        return frameTicker;
    }
}
