package com.lastdefenders.game.model.actor.ai;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.game.helper.CollisionDetection;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.health.interfaces.PlatedArmor;
import com.lastdefenders.game.model.actor.interfaces.IRocket;

/**
 * Contains Enemy AI methods to find towers.
 *
 * @author Eric
 */
public class EnemyAI {

    /**
     * Finds a random tower in range.
     *
     * @return Tower
     */
    public static Tower findRandomTowerInRange(Enemy enemy, SnapshotArray<Tower> towers) {

        if (towers.size == 0) {
            return null;
        }

        SnapshotArray<Tower> towersInRange = new SnapshotArray<>();

        for (Tower tower : towers) {
            // Tower is active and not dead
            if (!tower.isDead() && tower.isActive()) {
                if (CollisionDetection
                    .shapesIntersect(tower.getBody(), enemy.getRangeShape())) {

                    // If the enemy is instanceof IRPG then it can
                    // attack plated towers.
                    if ((!(tower instanceof PlatedArmor)) || (enemy instanceof IRocket)) {
                        towersInRange.add(tower);
                    }

                }
            }
        }

        if(towersInRange.size > 0) {
            int rndIdx = MathUtils.random(towersInRange.size - 1);

            return towersInRange.get(rndIdx);
        } else {
            return null;
        }

    }

    /**
     * Finds the nearest tower relative to the enemy
     *
     * @return Tower
     */
    public static Tower findNearestTower(Enemy enemy, SnapshotArray<Tower> towers) {

        if (towers.size == 0) {
            return null;
        }
        float firstTowerDistance = Integer.MAX_VALUE;
        Tower firstTower = null;
        for (Tower tower : towers) {
            // Tower is active and not dead
            if (!tower.isDead() && tower.isActive()) {
                if (CollisionDetection
                    .shapesIntersect(tower.getBody(), enemy.getRangeShape())) {
                    if (tower.getPositionCenter().dst(enemy.getPositionCenter())
                        < firstTowerDistance) {
                        // If the enemy is instanceof IRPG then it can
                        // attack plated towers.
                        if ((!(tower instanceof PlatedArmor)) || (enemy instanceof IRocket)) {
                            firstTower = tower;
                            firstTowerDistance = tower.getPositionCenter()
                                .dst(enemy.getPositionCenter());
                        }
                    }
                }
            }
        }
        return firstTower;
    }
}

