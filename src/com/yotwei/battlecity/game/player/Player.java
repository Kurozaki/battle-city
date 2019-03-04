package com.yotwei.battlecity.game.player;

import java.awt.*;

/**
 * Created by YotWei on 2019/3/1.
 */
public class Player {

    private final String name;

    private int liveCount;
    private int score;

    private Point startCoord;

    public Player(String name, int liveCountInit) {
        this.name = name;
        this.liveCount = liveCountInit;
    }

    public void setStartCoord(Point startCoord) {
        this.startCoord = startCoord;
    }
}
