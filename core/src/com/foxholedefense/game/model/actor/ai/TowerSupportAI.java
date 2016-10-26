package com.foxholedefense.game.model.actor.ai;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.helper.CollisionDetection;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.health.interfaces.IPlatedArmor;
import com.foxholedefense.game.model.actor.interfaces.IAttacker;
import com.foxholedefense.game.model.actor.interfaces.IRpg;

/**
 * Contains Tower AI methods to find enemies.
 * 
 * @author Eric
 *
 */
public final class TowerSupportAI {
	/**
	 * Finds the enemy farthest away from the end of the path within range of
	 * the tower
	 * 
	 * @param attacker
	 * @param enemyGroup
	 * @return Enemy
	 */
	public static Enemy findLastEnemy(IAttacker attacker, SnapshotArray<Actor> enemies) {
		if (enemies.size == 0) {
			return null;
		}
		float lastEnemyDistance = Integer.MIN_VALUE;
		Enemy lastEnemy = null;
		Enemy platedLastEnemy = null;
		Enemy tempEnemy = null;
		for (Actor actor : enemies) {
			if (actor instanceof Enemy) {
				tempEnemy = (Enemy)actor;
				if (tempEnemy.isDead() == false) {
					if (CollisionDetection.targetWithinRange(tempEnemy.getBody(), attacker.getRangeShape())) {
						if (tempEnemy.lengthTillEnd() > lastEnemyDistance) {
							if ((tempEnemy instanceof IPlatedArmor == false) || (attacker instanceof IRpg)) {
								lastEnemy = tempEnemy;
								lastEnemyDistance = tempEnemy.lengthTillEnd();
							} else {
								// Attack the plated enemy if there are no
								// Other enemies to attack
								platedLastEnemy = tempEnemy;
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
	 * Finds the enemy closest to the end of the path within range of the attacker
	 * 
	 * @param attacker
	 * @param enemyGroup
	 * @return Enemy
	 */
	public static CombatActor findFirstEnemy(IAttacker attacker, SnapshotArray<Actor> enemies) {
		if (enemies.size == 0) {
			return null;
		}
		float firstEnemyDistance = Integer.MAX_VALUE;
		Enemy firstEnemy = null;
		Enemy platedFirstEnemy = null;
		Enemy tempEnemy = null;
		for (Actor actor : enemies) {
			if (actor instanceof Enemy) {
				tempEnemy = (Enemy) actor;
				if (tempEnemy.isDead() == false) {
					if (CollisionDetection.targetWithinRange(tempEnemy.getBody(), attacker.getRangeShape())) {
						if (tempEnemy.lengthTillEnd() < firstEnemyDistance) {
							if ((tempEnemy instanceof IPlatedArmor == false) || (attacker instanceof IRpg)) {
								firstEnemy = tempEnemy;
								firstEnemyDistance = tempEnemy.lengthTillEnd();
							} else {
								platedFirstEnemy = tempEnemy;
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
	 * Finds the enemy with the least health within range of the attacker
	 * 
	 * @param attacker
	 * @param enemyGroup
	 * @return Enemy
	 */
	public static Enemy findLeastHPEnemy(IAttacker attacker, SnapshotArray<Actor> enemies) {
		if (enemies.size == 0) {
			return null;
		}
		float lowestEnemyHealth = Integer.MAX_VALUE;
		Enemy lowestHPEnemy = null;
		Enemy platedLowestHPEnemy = null;
		Enemy tempEnemy = null;
		for (Actor actor : enemies) {
			if (actor instanceof Enemy) {
				tempEnemy = (Enemy) actor;
				if (tempEnemy.isDead() == false) {
					if (CollisionDetection.targetWithinRange(tempEnemy.getBody(), attacker.getRangeShape())) {
						if (tempEnemy.getHealth() < lowestEnemyHealth) {
							if ((tempEnemy instanceof IPlatedArmor == false) || (attacker instanceof IRpg)) {
								lowestHPEnemy = tempEnemy;
								lowestEnemyHealth = tempEnemy.getHealth();
							} else {
								platedLowestHPEnemy = tempEnemy;
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
	 * @param attacker
	 * @param enemyGroup
	 * @return Enemy
	 */
	public static Enemy findMostHPEnemy(IAttacker attacker, SnapshotArray<Actor> enemies) {
		if (enemies.size == 0) {
			return null;
		}
		float highestEnemyHealth = Integer.MIN_VALUE;
		Enemy highestHPEnemy = null;
		Enemy platedHighestHPEnemy = null;
		Enemy tempEnemy;
		for (Actor actor : enemies) {
			if (actor instanceof Enemy) {
				tempEnemy = (Enemy) actor;
				if (tempEnemy.isDead() == false) {
					if (CollisionDetection.targetWithinRange(tempEnemy.getBody(), attacker.getRangeShape())) {
						if (tempEnemy.getHealth() > highestEnemyHealth) {
							if ((tempEnemy instanceof IPlatedArmor == false) || (attacker instanceof IRpg)) {
								highestHPEnemy = tempEnemy;
								highestEnemyHealth = tempEnemy.getHealth();
							} else {
								platedHighestHPEnemy = tempEnemy;
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
	 * Finds the enemy with the highest attack within range of the attacker
	 * 
	 * @param attacker
	 * @param enemies
	 * @return Enemy
	 */
	public static Enemy findDeadliestEnemy(IAttacker attacker, SnapshotArray<Actor> enemies) {
		return null;
	}

	/**
	 * Finds the enemy with the lowest attack within range of the attacker
	 * 
	 * @param attacker
	 * @param enemies
	 * @return Enemy
	 */
	public static Enemy findWeakestEnemy(IAttacker attacker, SnapshotArray<Actor> enemies) {
		return null;
	}
}
