package com.foxholedefense.game.helper;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.health.interfaces.IPlatedArmor;
import com.foxholedefense.game.model.actor.interfaces.IAttacker;
import com.foxholedefense.game.model.actor.interfaces.ITargetable;
import com.foxholedefense.game.model.actor.projectile.Flame;
import com.foxholedefense.util.Logger;
/**
 * Class that deals damage
 * @author Eric
 *
 */
public class Damage {
	private static void dealTargetDamage(IAttacker attacker, ITargetable target) {
		if (target != null && target.isDead() == false) {
			target.takeDamage(attacker.getAttack());
			if (target.isDead() && attacker instanceof Tower) {
				// Only give the tower a kill if it is alive.
				if (((Tower)attacker).isDead() == false) {
					Logger.debug("Target: giving kill to shooter");
					((Tower) attacker).giveKill();
				}
			}
		}
	}
	public static void dealRpgDamage(IAttacker attacker, ITargetable target) {
		dealTargetDamage(attacker,target);
	}

	public static void dealBulletDamage(IAttacker attacker, ITargetable target) {
		if (!(target instanceof IPlatedArmor)) {
			dealTargetDamage(attacker, target);
		}
	}
	public static void dealFlameTargetDamage(IAttacker attacker, ITargetable target) {
		if (!(target instanceof IPlatedArmor)) {
			dealTargetDamage(attacker, target);
		}
	}
	public static void dealFlameGroupDamage(IAttacker attacker, ITargetable target, SnapshotArray<Actor>targetGroup, Polygon flameBody) {
		ITargetable flameTarget;
		for(int i = targetGroup.size - 1; i >= 0; i--){
			Actor actor = targetGroup.get(i);
			flameTarget = (ITargetable) actor;
			if (flameTarget != null && flameTarget.isDead() == false) {
				Shape2D targetBody = flameTarget.getBody();
				if (CollisionDetection.flameAndTarget(targetBody, flameBody)) {
					if(!(flameTarget.equals(target))){
						dealFlameTargetDamage(attacker,flameTarget);
					}
				}
			}
		}
	}

	public static void dealExplosionDamage(IAttacker attacker, float radius, Vector2 position, ITargetable target, SnapshotArray<Actor>targetGroup) {
		Circle aoeRadius = new Circle(position.x, position.y, radius);
		ITargetable aoeTarget;
		float distance, damage, damagePercent;
		for(int i = targetGroup.size - 1; i >= 0; i--){
			Actor actor = targetGroup.get(i);
			aoeTarget = (ITargetable) actor;
			distance = damage = damagePercent = 0;
			if (aoeTarget.isDead() == false) {
				if (aoeTarget.equals(target) == false) {
					distance = position.dst( aoeTarget.getPositionCenter());
					Logger.debug("AOE Actor distance: " + distance + " aoe radius: " + aoeRadius.radius);
					if (CollisionDetection.explosionAndTarget( aoeTarget.getBody(), aoeRadius)) {
						damagePercent = (1000 / distance);
						if (damagePercent > 100) {
							damagePercent = 100;
						}
						damage = (attacker.getAttack() * (damagePercent / 100));
						Logger.debug("Doing " + damagePercent + "% of damage for " + damage + " damage");
						aoeTarget.takeDamage(damage);
						Logger.debug("Actors new health:" + ((CombatActor) aoeTarget).getHealth());
						if (aoeTarget.isDead() && attacker instanceof Tower) {
							if (((Tower)attacker).isDead() == false) {
								Logger.debug("Explosion: giving kill to shooter");
								((Tower) attacker).giveKill();
							}
						}
					}
				}
			}
		}
	}

}
