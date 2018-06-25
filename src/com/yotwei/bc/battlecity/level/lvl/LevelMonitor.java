package com.yotwei.bc.battlecity.level.lvl;

import com.yotwei.bc.battlecity.level.obj.tank.AbstractTank;
import com.yotwei.bc.battlecity.sprite.TextSprite;
import com.yotwei.bc.battlecity.stage.GameOverStage;
import com.yotwei.bc.battlecity.stage.LevelTransformStage;
import com.yotwei.bc.battlecity.stage.StageHolder;
import com.yotwei.bc.battlecity.util.Debugger;
import com.yotwei.bc.battlecity.util.HashMapBuilder;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by YotWei on 2018/6/14.
 */
public class LevelMonitor {

    enum Status {

        NORMAL, BATTLE_FINISHED, GAME_OVER;

        private static final String[] ss = {
                "",
                "Battle Finished",  // BATTLE_FINISHED
                "Game Over"};       // GAME_OVER

        public String string() {
            return ss[this.ordinal()];
        }
    }

    private static final int PLAYER_DEFAULT_LIVES_COUNT = 2;
    private static final int TANK_GENERATE_DELAY = 30;
    private static final int LEVEL_OVER_DELAY = 240;

    /*
     * the monitoring level
     */
    private Level _level;

    private Status _status = Status.NORMAL;
    private boolean _isActivate = true;

    /*
     * about player
     *
     * player lives count and player score
     */
    private static int playerLivesCount = PLAYER_DEFAULT_LIVES_COUNT;
    private Point playerGeneratePoint;

    private List<Point> enemiesGeneratePoints;
    private int enemiesRemainingCount;
    private int undestroyedEnemiesCount;

    /*
     * event handlers
     */
    private HashMap<Integer, EventHandler> eventHandlers = new HashMapBuilder<Integer, EventHandler>()
            .put(Event.TYPE_LEVEL_INIT, bundleMap -> {

                /* initial test */

                // prepare player
                _level.addEvent(Event.playerPrepare(TANK_GENERATE_DELAY, playerGeneratePoint.x, playerGeneratePoint.y));

                // prepare enemies
                for (Point point : enemiesGeneratePoints) {
                    _level.addEvent(Event.enemyPrepare(TANK_GENERATE_DELAY, 1, point.x, point.y));
                }
            })
            .put(Event.TYPE_PLAYER_DEATH, bundleMap -> {

                /* player death */

                // check remaining lives count
                if (playerLivesCount <= 0) {
                    /*
                     * player has no remaining lives count
                     * do Game Over
                     */
                    _level.addEvent(Event.gameOver());
                    return;
                }

                // recreate player tank
                _level.addEvent(Event.playerPrepare(TANK_GENERATE_DELAY, playerGeneratePoint.x, playerGeneratePoint.y));
            })
            .put(Event.TYPE_ENEMY_DEATH, bundleMap -> {

                /* enemy death event */

                undestroyedEnemiesCount--;
                if (undestroyedEnemiesCount <= 0) {
                    // do enemy clear
                    _level.addEvent(Event.battleFinished());
                    return;
                }

                if (this.enemiesRemainingCount > 0) {
                    // get generate point randomly
                    int idx = new Random().nextInt(this.enemiesGeneratePoints.size());
                    Point point = this.enemiesGeneratePoints.get(idx);

                    // add new enemy
                    _level.addEvent(Event.enemyPrepare(TANK_GENERATE_DELAY, 1, point.x, point.y));
                }
            })
            .put(Event.TYPE_BULLET_PROJECT, bundleMap -> {

                /* project bullet */

                AbstractTank launcher = (AbstractTank) bundleMap.get("launcher");
                _level.entityFactory.createBullet(1, launcher);
            })
            .put(Event.TYPE_PLAYER_PREPARE, bundleMap -> {

                /* prepare player generate */

                // decrease player lives count first
                playerLivesCount--;

                Point p = (Point) bundleMap.get("point");
                _level.entityFactory.preparePlayerTank(p.x, p.y);
            })
            .put(Event.TYPE_PLAYER_CREATE, bundleMap -> {

                /* player tank create (recreate) */

                Point p = (Point) bundleMap.get("point");
                _level.entityFactory.createPlayer(p.x, p.y);
            })
            .put(Event.TYPE_NEW_ENEMY_PREPARE, bundleMap -> {

                /* prepare enemy generate */

                // check if able to generate new enemy
                if (this.enemiesRemainingCount <= 0)
                    return;

                // decrease enemy remaining count
                this.enemiesRemainingCount--;

                Point p = (Point) bundleMap.get("point");
                _level.entityFactory.prepareEnemyTank((Integer) bundleMap.get("modelId"), p.x, p.y);
            })
            .put(Event.TYPE_NEW_ENEMY_CREATE, bundleMap -> {

                /* new enemy create */

                Point p = (Point) bundleMap.get("point");
                _level.entityFactory.createEnemy((Integer) bundleMap.get("modelId"), p.x, p.y);
            })
            .put(Event.TYPE_BATTLE_FINISHED, bundleMap -> {

                /* battle finished */

                // change level status
                changeStatus(Status.BATTLE_FINISHED);

                // send delay event to end level-handle stage
                _level.addEvent(Event.levelOver(LEVEL_OVER_DELAY));
            })
            .put(Event.TYPE_GAME_OVER, bundleMap -> {

                /* game over */

                // change level status
                changeStatus(Status.GAME_OVER);

                // send delay event to end level-handle stage
                _level.addEvent(Event.levelOver(LEVEL_OVER_DELAY));
            })
            .put(Event.TYPE_LEVEL_OVER, bundleMap -> {

                /* handle level finished */

                if (_status == Status.GAME_OVER) {
                    StageHolder.INSTANCE.addStage(new GameOverStage());
                } else {
                    StageHolder.INSTANCE.addStage(new LevelTransformStage());
                }
                _isActivate = false;
            })
            .put(Event.TYPE_CREATE_EFFECT, bundleMap -> {
                _level.entityFactory.createEffect(
                        (Integer) bundleMap.get("effectId"),
                        (Integer) bundleMap.get("x"),
                        (Integer) bundleMap.get("y"),
                        (Integer) bundleMap.get("aliveTime"));
            })
            .build();

    public LevelMonitor(Level _Level, InitialParameters ip) {
        this._level = _Level;

        // set player generate point
        this.playerGeneratePoint = ip.getPlayerGeneratePoint();

        // set enemies remaining count and enemies generate points
        this.enemiesRemainingCount = ip.getEnemiesCount();
        this.undestroyedEnemiesCount = enemiesRemainingCount;
        this.enemiesGeneratePoints = ip.getEnemiesGeneratePoints();

        // add initial event to level
        this._level.addEvent(Event.levelInit());
    }

    public void update() {
        this._level.updateLevel();

        // handle events
        handleEvents();
    }

    public void draw(Graphics2D g) {

        // draw level's entities
        this._level.drawLevel(g);

        // draw debug info
        Debugger.Draw.drawLines(g);

        if (_status != Status.NORMAL) {
            TextSprite.NORMAL_TEXT.draw(_status.string(), TextSprite.DRAW_ALIGN_CENTER, 0, 0, g);
        }
    }

    public boolean isActivate() {
        return _isActivate;
    }

    /**
     * change level status
     * enum's ordinal is the priority of the status
     *
     * @param newStatus new level status
     */
    private void changeStatus(Status newStatus) {
        if (newStatus.ordinal() > _status.ordinal())
            _status = newStatus;
    }

    private void handleEvents() {
        Queue<Event> eventQueue = _level.eventQueue();
        Queue<Event> unhandledQueue = new LinkedList<>();
        while (!eventQueue.isEmpty()) {
            Event ev = eventQueue.poll();
            if (!ev.isReady()) {
                unhandledQueue.add(ev);
                continue;
            }

            EventHandler eh = eventHandlers.get(ev.getType());
            if (eh != null) eh.handle(ev.getDataBundle());
        }
        eventQueue.addAll(unhandledQueue);
    }

    /**
     * reset player info, such lives count and so on
     */
    public static void reset() {
        playerLivesCount = PLAYER_DEFAULT_LIVES_COUNT;
    }

    /*
     * event handler
     */
    interface EventHandler {
        void handle(Map<String, Object> bundleMap);
    }
}
