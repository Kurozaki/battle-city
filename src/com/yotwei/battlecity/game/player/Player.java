package com.yotwei.battlecity.game.player;

/**
 * Created by YotWei on 2019/3/1.
 */
public class Player {

    private static int playerId = 0;

    private final int pid;

    private final String name;

    private int liveCount;
    private int score;


    public Player(String name, int liveCountInit) {
        this.pid = ++playerId;
        this.name = name;
        this.liveCount = liveCountInit;
    }

    public int getId() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public int getLiveCount() {
        return liveCount;
    }

    public int getScore() {
        return score;
    }
}
