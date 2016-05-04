package com.eric.mtd.game.model.actor.ai;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.eric.mtd.game.helper.CollisionDetection;
import com.eric.mtd.game.model.actor.combat.enemy.Enemy;
import com.eric.mtd.game.model.actor.combat.tower.Tower;
import com.eric.mtd.game.model.actor.health.interfaces.IPlatedArmor;
import com.eric.mtd.game.model.actor.projectile.interfaces.IAoe;

/**
 * Contains Enemy AI methods to find towers.
 * 
 * @author Eric
 *
 */
public class EnemyAI {
	/**
	 * Finds the nearest tower relative to the enemy
	 * 
	 * @param enemy
	 * @param towerGroup
	 * @return Tower
	 */
	public static Tower findNearestTower(Enemy enemy, SnapshotArray<Actor> towers) {
		if (towers.size == 0) {
			return null;
		}
		float firstTowerDistance = Integer.MAX_VALUE;
		Tower firstTower = null;

		for (Actor tower : towers) {
			if (tower instanceof Tower) {
				// Tower is active and not dead
				if (((Tower) tower).isDead() == false && ((Tower) tower).isActive()) {
					if (CollisionDetection.targetWithinRange(((Tower) tower).getBody(), enemy.getRangeShape())) {
						if (((Tower) tower).getPositionCenter().dst(enemy.getPositionCenter()) < firstTowerDistance) {
							// If the enemy is instanceof IRPG then it can
							// attack plated towers.
							if ((tower instanceof IPlatedArmor == false) || (enemy instanceof IAoe)) {
								firstTower = (Tower) tower;
								firstTowerDistance = ((Tower) tower).getPositionCenter().dst(enemy.getPositionCenter());
							}
						}
					}
				}
			}
		}
		return firstTower;
	}
}