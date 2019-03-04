package com.yotwei.battlecity.game.stage;

import com.yotwei.battlecity.framework.IStageHandleContext;
import com.yotwei.battlecity.framework.stage.IStage;
import com.yotwei.battlecity.game.engine.GameContext;
import com.yotwei.battlecity.game.level.decoder.ILevelPackageDecoder;
import com.yotwei.battlecity.game.level.LevelPackage;
import com.yotwei.battlecity.game.stage.scene.*;
import com.yotwei.battlecity.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.util.Objects;

/**
 * Created by YotWei on 2019/2/26.
 */
public class GameStage extends GameContext
        implements IStage {

    private static final Logger logger = LoggerFactory.getLogger("GameStage");

    private AbstractScene[] scenesArray;
    private int sceneCursor;

    private boolean switchScene;

    @Override
    public void onInit(IStageHandleContext ctx) {

        if (logger.isDebugEnabled()) {
            logger.debug("{}.onInit()", getClass().getSimpleName());
        }

        //
        // decode level package
        // ensure that variable 'levelPackageName' has been put at HeadlineStage.update()
        //
        LevelPackage lp = decodeLevelPackage(ctx.getVar("levelPackageName"));
        setLevelPackage(lp);

        //
        // init scenes and reset the first scene
        //
        scenesArray = new AbstractScene[]{
                new LevelSwitcher(this),
                new LevelPreviewer(this),
                new LevelHandler(this),
                new LevelClearer(this)
        };

        scenesArray[sceneCursor].resetScene();
    }

    private LevelPackage decodeLevelPackage(String packageName) {

        if (logger.isDebugEnabled()) {
            logger.debug("loading level package '{}'", packageName);
        }

        File lpkgFile = new File(
                Objects.requireNonNull(Constant.RES_PATH).getFile(),
                "maps/" + packageName + ".lpkg");

        // get an ILevelPackageDecoder instance
        ILevelPackageDecoder decoder = Objects.requireNonNull(ILevelPackageDecoder.getDecoder());

        // decode level package and return
        return decoder.decode(lpkgFile);
    }

    @Override
    public void update(IStageHandleContext ctx) {

        // set switch scene flag to false
        switchScene = false;

        // get current handling scene
        AbstractScene scene = scenesArray[sceneCursor];

        // update scene
        scene.updateScene();

        if (scene.isSceneFinished()) {
            // set switch scene to true
            switchScene = true;
        }
    }

    @Override
    public void draw(Graphics2D g) {

        // draw current handling scene
        AbstractScene scene = scenesArray[sceneCursor];
        scene.drawScene(g);

        if (switchScene) {

            // get next scene cursor and reset next scene
            sceneCursor = (sceneCursor + 1) % scenesArray.length;
            scenesArray[sceneCursor].resetScene();
        }
    }

    @Override
    public void onFinished(IStageHandleContext ctx) {

    }

    @Override
    public IStage next() {
        return this;
    }

}
