package com.yotwei.battlecity.game.object.tank.behavior;

import com.yotwei.battlecity.game.object.GameObject;
import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.properties.Direction;
import com.yotwei.battlecity.game.object.tank.EnemyTank;
import com.yotwei.battlecity.util.Constant;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by YotWei on 2019/3/4.
 */

@SuppressWarnings("WeakerAccess")
public class EnemyTankMovement extends AbstractTankMovement<EnemyTank> {

    private Map<Direction, Rectangle> detectBoxes;

    private LevelContext.RetrieveFilter<GameObject> retrieveFilter;
    private Set<String> retrieveGroupNames;

    private Random rand;
    private int ticker;

    protected EnemyTankMovement(EnemyTank tank) {
        super(tank);

        //
        // initialize detect boxes
        //
        detectBoxes = new EnumMap<>(Direction.class);
        for (Direction dir : Direction.values()) {
            Rectangle box;
            if (dir == Direction.LEFT
                    || dir == Direction.RIGHT) {
                box = new Rectangle(2, Constant.UNIT_SIZE.height + 2);
            } else {
                box = new Rectangle(Constant.UNIT_SIZE.width + 2, 2);
            }
            detectBoxes.put(dir, box);
        }

        rand = new Random();
        retrieveFilter = anObject -> true;

        retrieveGroupNames = new HashSet<>();
        retrieveGroupNames.add("blockGroup");
        retrieveGroupNames.add("tankGroup");
    }

    private void updateDetectBoxes(Rectangle tankHitbox) {

        for (Map.Entry<Direction, Rectangle> entry : detectBoxes.entrySet()) {

            Rectangle detBox = entry.getValue();

            switch (entry.getKey()) {
                case UP:
                    detBox.setLocation(
                            tankHitbox.x + ((tankHitbox.width - detBox.width) >> 1),
                            tankHitbox.y - detBox.height
                    );
                    break;

                case DOWN:
                    detBox.setLocation(
                            tankHitbox.x + ((tankHitbox.width - detBox.width) >> 1),
                            tankHitbox.y + tankHitbox.height
                    );
                    break;

                case LEFT:
                    detBox.setLocation(
                            tankHitbox.x - detBox.width,
                            tankHitbox.y + ((tankHitbox.height - detBox.height) >> 1)
                    );
                    break;

                case RIGHT:
                    detBox.setLocation(
                            tankHitbox.x + tankHitbox.width,
                            tankHitbox.y + ((tankHitbox.height - detBox.height) >> 1)
                    );
                    break;
            }
        }
    }

    @Override
    public Direction nextMoveDirection() {

        EnemyTank tank = getAccessTank();

        updateDetectBoxes(tank.getHitbox());

        if (ticker-- > 0) {
            return tank.getDirection();
        }

        ticker = 16 + rand.nextInt(16);

        //
        // find available moving direction
        // the opposite direction has lowest priority to be selected
        // it only will be selected when the other 3 direction is unavailable
        //
        Direction nextDirection, oppositeDirection = null;
        List<Direction> selectableDirections = new ArrayList<>(4);

        for (Map.Entry<Direction, Rectangle> entry : detectBoxes.entrySet()) {
            Direction dir = entry.getKey();

            if (Direction.isOpposite(dir, tank.getDirection())) {
                // set opposite direction
                oppositeDirection = dir;

            } else {

                Rectangle detBox = entry.getValue();
                LevelContext lc = tank.getLevelContext();

                // check detect box
                if (lc.getLevelBound().contains(detBox) &&
                        lc.retrieveGameObject(detBox, retrieveGroupNames, retrieveFilter).isEmpty()) {

                    // detect box not meet any block or out of level bound
                    // so the next direction is available, add to selectable direction list
                    selectableDirections.add(dir);
                }
            }
        }

        if (selectableDirections.isEmpty()) {
            // because the other 3 direction is unavailable
            // select opposite direction as next direction
            nextDirection = oppositeDirection;
        } else {
            // select an direction randomly
            nextDirection = selectableDirections
                    .get(rand.nextInt(selectableDirections.size()));
        }

        return nextDirection;
    }
}
