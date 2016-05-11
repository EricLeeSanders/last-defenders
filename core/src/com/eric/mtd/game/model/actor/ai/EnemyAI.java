package com.eric.mtd.game.model.actor.ai;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.eric.mtd.game.helper.CollisionDetection;
import com.eric.mtd.game.model.actor.combat.enemy.Enemy;
import com.eric.mtd.game.model.actor.combat.tower.Tower;
import com.eric.mtd.game.model.actor.health.interfaces.IPlatedArmor;
import com.eric.mtd.game.model.actor.interfaces.IRpg;

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
		Tower tempTower = null;
		for (Actor actor : towers) {
			if(actor instanceof Tower){
				tempTower = (Tower)actor;
				// Tower is active and not dead
				if (tempTower.isDead() == false && tempTower.isActive()) {
					if (CollisionDetection.targetWithinRange(tempTower.getBody(), enemy.getRangeShape())) {
						if (tempTower.getPositionCenter().dst(enemy.getPositionCenter()) < firstTowerDistance) {
							// If the enemy is instanceof IRPG then it can
							// attack plated towers.
							if ((tempTower instanceof IPlatedArmor == false) || (enemy instanceof IRpg)) {
								firstTower = tempTower;
								firstTowerDistance = tempTower.getPositionCenter().dst(enemy.getPositionCenter());
							}
						}
					}
				}
			}
		}
		return firstTower;
	}
}