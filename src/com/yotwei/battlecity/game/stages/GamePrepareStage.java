package com.yotwei.battlecity.game.stages;

import com.yotwei.battlecity.framework.IStageHandleContext;
import com.yotwei.battlecity.framework.stage.IStage;
import com.yotwei.battlecity.game.GameController;
import com.yotwei.battlecity.game.beans.LevelBean;
import com.yotwei.battlecity.util.Constant;
import com.yotwei.battlecity.util.FileUtil;
import com.yotwei.battlecity.util.GraphicUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.util.Objects;

/**
 * Created by YotWei on 2019/3/15.
 */
public class GamePrepareStage implements IStage {

    private static final Logger logger = LoggerFactory.getLogger(GamePrepareStage.class);

    @Override
    public void onInit(IStageHandleContext ctx) {
        LevelBean[] levels = prepareLevels(ctx);

        // 重置游戏状态
        GameController.INST.reset(levels);
    }

    /**
     * 方法用于从文件夹下加载关卡文件
     */
    private LevelBean[] prepareLevels(IStageHandleContext ctx) {
        File levelDir = new File(
                Objects.requireNonNull(Constant.RES_PATH).getFile(),
                "maps/" + ctx.getVar("_LevelDir")  // 从 HeadlineStage 传来的变量
        );

        if (logger.isInfoEnabled()) {
            logger.info("Start to load levels from directory {}", levelDir.getPath());
        }

        File[] levelFiles = Objects.requireNonNull(levelDir.listFiles());
        LevelBean[] levels = new LevelBean[levelFiles.length];  // 约定目录下所有文件都是关卡文件

        try {

            // 遍历各个关卡文件，读取json数据
            for (File file : levelFiles) {
                JSONObject levelJson = FileUtil.createJsonObjectFromFile(file);

                int index = levelJson.getInt("index");

                // 下标检查
                if (0 > index || index >= levels.length) {
                    throw new RuntimeException("Illegal level index: " + index);
                }

                // 不允许有重复的关卡索引
                if (levels[index] != null) {
                    throw new RuntimeException("duplicate level index(" + index
                            + ") for level" + levelJson.getString("caption"));
                }
                levels[index] = LevelBean.parseJson(levelJson);
            }

            return levels;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception catch when loading levels");
        }
    }

    @Override
    public void update(IStageHandleContext ctx) {

    }

    @Override
    public void draw(Graphics2D g) {
        GraphicUtil.clearScreen(g, Color.BLACK);
    }

    @Override
    public void onFinished(IStageHandleContext ctx) {

    }

    @Override
    public IStage next() {
        return new LevelPreviewStage();
    }
}
