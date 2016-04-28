package com.eric.mtd.game.model.actor.ai;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.eric.mtd.game.helper.CollisionDetection;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.combat.enemy.Enemy;
import com.eric.mtd.game.model.actor.combat.tower.Tower;
import com.eric.mtd.game.model.actor.health.interfaces.IPlatedArmor;
import com.eric.mtd.game.model.actor.projectile.interfaces.IRPG;

/**
 * Contains Tower AI methods to find enemies.
 * 
 * @author Eric
 *
 */
public final class TowerAI {
	/**
	 * Finds the enemy farthest away from the end of the path within range of
	 * the tower
	 * 
	 * @param tower
	 * @param enemyGroup
	 * @return Enemy
	 */
	public static Enemy findLastEnemy(Tower tower, SnapshotArray<Actor> enemies) {
		if (enemies.size == 0) {
			return null;
		}
		float lastEnemyDistance = Integer.MIN_VALUE;
		Enemy lastEnemy = null;
		Enemy platedLastEnemy = null;
		for (Actor enemy : enemies) {
			if (enemy instanceof Enemy) {
				if (((Enemy) enemy).isDead() == false) {
					if (CollisionDetection.targetWithinRange(((Enemy) enemy).getBody(), tower.getRangeShape())) {
						if (((Enemy) enemy).lengthTillEnd() > lastEnemyDistance) {
							if ((enemy instanceof IPlatedArmor == false) || (tower instanceof IRPG)) {
								lastEnemy = (Enemy) enemy;
								lastEnemyDistance = ((Enemy) enemy).lengthTillEnd();
							} else {
								// Attack the plated enemy if there are no
								// Other enemies to attack
								platedLastEnemy = (Enemy) enemy;
							}
						}
					}
				}
			}
		}
		// If there is no enemy then return plated enemy so the tower has
		// something to attack
		if (lastEnemy == null) {
			return platedLastEnemy;
		} else {
			return lastEnemy;
		}

	}

	/**
	 * Finds the enemy closest to the end of the path within range of the tower
	 * 
	 * @param tower
	 * @param enemyGroup
	 * @return Enemy
	 */
	public static CombatActor findFirstEnemy(Tower tower, SnapshotArray<Actor> enemies) {
		if (enemies.size == 0) {
			return null;
		}
		float firstEnemyDistance = Integer.MAX_VALUE;
		Enemy firstEnemy = null;
		Enemy platedFirstEnemy = null;
		for (Actor enemy : enemies) {
			if (enemy instanceof Enemy) {
				if (((Enemy) enemy).isDead() == false) {
					if (CollisionDetection.targetWithinRange(((Enemy) enemy).getBody(), tower.getRangeShape())) {
						if (((Enemy) enemy).lengthTillEnd() < firstEnemyDistance) {
							if ((enemy instanceof IPlatedArmor == false) || (tower instanceof IRPG)) {
								firstEnemy = (Enemy) enemy;
								firstEnemyDistance = ((Enemy) enemy).lengthTillEnd();
							} else {
								platedFirstEnemy = (Enemy) enemy;
							}
						}
					}
				}
			}
		}
		if (firstEnemy == null) {
			return platedFirstEnemy;
		} else {
			return firstEnemy;
		}
	}

	/**
	 * Finds the enemy with the least health within range of the tower
	 * 
	 * @param tower
	 * @param enemyGroup
	 * @return Enemy
	 */
	public static Enemy findLeastHPEnemy(Tower tower, SnapshotArray<Actor> enemies) {
		if (enemies.size == 0) {
			return null;
		}
		float lowestEnemyHealth = Integer.MAX_VALUE;
		;
		Enemy lowestHPEnemy = null;
		Enemy platedLowestHPEnemy = null;
		for (Actor enemy : enemies) {
			if (enemy instanceof Enemy) {
				if (((Enemy) enemy).isDead() == false) {
					if (CollisionDetection.targetWithinRange(((Enemy) enemy).getBody(), tower.getRangeShape())) {
						if (((Enemy) enemy).getHealth() < lowestEnemyHealth) {
							if ((enemy instanceof IPlatedArmor == false) || (tower instanceof IRPG)) {
								lowestHPEnemy = (Enemy) enemy;
								lowestEnemyHealth = ((Enemy) enemy).getHealth();
							} else {
								platedLowestHPEnemy = (Enemy) enemy;
							}
						}
					}
				}
			}
		}
		if (lowestHPEnemy == null) {
			return platedLowestHPEnemy;
		} else {
			return lowestHPEnemy;
		}

	}

	/**
	 * Finds the enemy with the most health within range of the tower
	 * 
	 * @param tower
	 * @param enemyGroup
	 * @return Enemy
	 */
	public static Enemy findMostHPEnemy(Tower tower, SnapshotArray<Actor> enemies) {
		if (enemies.size == 0) {
			return null;
		}
		float highestEnemyHealth = Integer.MIN_VALUE;
		Enemy highestHPEnemy = null;
		Enemy platedHighestHPEnemy = null;
		for (Actor enemy : enemies) {
			if (enemy instanceof Enemy) {
				if (((Enemy) enemy).isDead() == false) {
					if (CollisionDetection.targetWithinRange(((Enemy) enemy).getBody(), tower.getRangeShape())) {
						if (((Enemy) enemy).getHealth() > highestEnemyHealth) {
							if ((enemy instanceof IPlatedArmor == false) || (tower instanceof IRPG)) {
								highestHPEnemy = (Enemy) enemy;
								highestEnemyHealth = ((Enemy) enemy).getHealth();
							} else {
								platedHighestHPEnemy = (Enemy) enemy;
							}
						}
					}
				}
			}
		}
		if (highestHPEnemy == null) {
			return platedHighestHPEnemy;
		} else {
			return highestHPEnemy;
		}

	}

	/**
	 * Finds the enemy with the highest attack within range of the tower
	 * 
	 * @param tower
	 * @param enemies
	 * @return Enemy
	 */
	public static Enemy findDeadliestEnemy(Tower tower, SnapshotArray<Actor> enemies) {
		return null;
	}

	/**
	 * Finds the enemy with the lowest attack within range of the tower
	 * 
	 * @param tower
	 * @param enemies
	 * @return Enemy
	 */
	public static Enemy findWeakestEnemy(Tower tower, SnapshotArray<Actor> enemies) {
		return null;
	}
}
