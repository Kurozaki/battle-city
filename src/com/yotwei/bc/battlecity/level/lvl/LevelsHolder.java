package com.yotwei.bc.battlecity.level.lvl;

import com.yotwei.bc.battlecity.core.Const;

import java.io.File;
import java.util.*;

/**
 * Created by YotWei on 2018/6/13.
 */
public enum LevelsHolder {

    INSTANCE;

    private List<File> levelFilesQueue = new ArrayList<>();
    private int cursor = 0;

    public File next() {
        if (0 <= cursor && cursor < levelFilesQueue.size())
            return levelFilesQueue.get(cursor++);
        return null;
    }

    public void init() {
        if (!levelFilesQueue.isEmpty())
            throw new RuntimeException("levels holder has been initial");
        loadLevelSequence();
    }

    /**
     * reset level position
     */
    public void reset() {
        cursor = 0;
    }

    private void loadLevelSequence() {
        File levelDir = new File(Const.ROOT_DIR_URL.getFile(), "level/");
        File[] list = levelDir.listFiles();
        if (list != null) {
            levelFilesQueue.addAll(Arrays.asList(list));
        }
    }
}
