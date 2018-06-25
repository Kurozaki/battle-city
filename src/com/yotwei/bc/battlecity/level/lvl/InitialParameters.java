package com.yotwei.bc.battlecity.level.lvl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YotWei on 2018/6/18.
 */
public class InitialParameters {

    private String levelName;
    private Point playerGeneratePoint;
    private int enemiesCount;
    private List<Point> enemiesGeneratePoints;

    private InitialParameters() {

    }

    public int getEnemiesCount() {
        return enemiesCount;
    }


    public List<Point> getEnemiesGeneratePoints() {
        return enemiesGeneratePoints;
    }

    public String getLevelName() {
        return levelName;
    }

    public Point getPlayerGeneratePoint() {
        return playerGeneratePoint;
    }

    @Override
    public String toString() {
        return "InitialParameters{" +
                "levelName='" + levelName + '\'' +
                ", playerGeneratePoint=" + playerGeneratePoint +
                ", enemiesCount=" + enemiesCount +
                ", enemiesGeneratePoints=" + enemiesGeneratePoints +
                '}';
    }

    public static InitialParameters fromJson(JSONObject json) {
        InitialParameters parameters = new InitialParameters();

        parameters.levelName = json.getString("name");
        parameters.playerGeneratePoint = new Point(
                json.getJSONObject("playerGeneratePoint").getInt("x"),
                json.getJSONObject("playerGeneratePoint").getInt("y"));
        parameters.enemiesCount = json.getInt("enemiesCount");

        parameters.enemiesGeneratePoints = new ArrayList<>();
        JSONArray jparr = json.getJSONArray("enemiesGeneratePoints");
        for (int i = 0; i < jparr.length(); i++) {
            JSONObject jp = jparr.getJSONObject(i);
            parameters.getEnemiesGeneratePoints()
                    .add(new Point(jp.getInt("x"), jp.getInt("y")));
        }

        return parameters;
    }
}
