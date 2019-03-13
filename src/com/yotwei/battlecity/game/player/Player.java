package com.yotwei.battlecity.game.player;

import java.awt.*;

/**
 * Created by YotWei on 2019/3/1.
 */
public class Player {

//    private static int playerId = 0;

    private final int pid;
    private final String name;

    private int liveCount;
    private int score;

    private Point startPoint;

    public Player(String name, int liveCountInit) {
        this.pid = 1;
        this.name = name;
        this.liveCount = liveCountInit;
    }

    public int getId() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public int incLiveCount() {
        return ++liveCount;
    }

    public int decLiveCount() {
        return --liveCount;
    }

    public int getLiveCount() {
        return liveCount;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int addScore) {
        this.score += addScore;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Point getStartPoint() {
        return startPoint;
    }
}
