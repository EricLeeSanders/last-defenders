package com.foxholedefense.game.model.actor.ai;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.helper.CollisionDetection;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.health.interfaces.IPlatedArmor;
import com.foxholedefense.game.model.actor.interfaces.IRpg;

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
	 * @param towers
	 * @return Tower
	 */
	public static Tower findNearestTower(Enemy enemy, SnapshotArray<Actor> towers) {
		if (towers.size == 0) {
			return null;
		}
		float firstTowerDistance = Integer.MAX_VALUE;
		Tower firstTower = null;
		for (Actor actor : towers) {
			if(actor instanceof Tower){
				Tower tower = (Tower)actor;
				// Tower is active and not dead
				if (tower.isDead() == false && tower.isActive()) {
					if (CollisionDetection.targetWithinRange(tower.getBody(), enemy.getRangeShape())) {
						if (tower.getPositionCenter().dst(enemy.getPositionCenter()) < firstTowerDistance) {
							// If the enemy is instanceof IRPG then it can
							// attack plated towers.
							if ((tower instanceof IPlatedArmor == false) || (enemy instanceof IRpg)) {
								firstTower = tower;
								firstTowerDistance = tower.getPositionCenter().dst(enemy.getPositionCenter());
							}
						}
					}
				}
			}
		}
		return firstTower;
	}
}