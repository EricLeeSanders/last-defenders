package com.eric.mtd.game.helper;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.SnapshotArray;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.combat.tower.Tower;
import com.eric.mtd.game.model.actor.health.interfaces.IPlatedArmor;
import com.eric.mtd.game.model.actor.interfaces.IAttacker;
import com.eric.mtd.game.model.actor.interfaces.ITargetable;
import com.eric.mtd.game.model.actor.projectile.Flame;
import com.eric.mtd.util.Logger;
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
					((Tower) attacker).giveKill();
					if (Logger.DEBUG)
						System.out.println("Shooter: giving kill");
				}
			}
		}
	}
	public static void dealRpgDamage(IAttacker attacker, ITargetable target) {
		//System.out.println("rpg target group size: " + targetGroupArray.length);
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
		//Have to create a copy of the group otherwise when a target is killed, the iterator will skip
		//over the next in the group.
		SnapshotArray<Actor> targetGroupArray = new SnapshotArray<Actor>(targetGroup);
		targetGroup.begin();
		ITargetable flameTarget;
		for (Actor actor : targetGroupArray) {
			flameTarget = (ITargetable) actor;
			if (flameTarget != null && flameTarget.isDead() == false) {
				Polygon targetBody = flameTarget.getBody();
				if (CollisionDetection.polygonAndPolygon(targetBody, flameBody)) {
					if(!(flameTarget.equals(target))){
						dealFlameTargetDamage(attacker,flameTarget);
					}else {
						if(Logger.DEBUG)System.out.println("Target == flameTarget");
					}
				}
			}
		}
	}

	public static void dealExplosionDamage(IAttacker attacker, float radius, Vector2 position, ITargetable target, SnapshotArray<Actor>targetGroup) {
		Circle aoeRadius = new Circle(position.x, position.y, radius);
		//Have to create a copy of the group otherwise when a target is killed, the iterator will skip
		//over the next in the group.
		SnapshotArray<Actor> targetGroupArray = new SnapshotArray<Actor>(targetGroup);
		ITargetable aoeTarget;
		float distance, damage, damagePercent;
		for (Actor actor : targetGroupArray) {
			aoeTarget = (ITargetable) actor;
			distance = damage = damagePercent = 0;
			if (aoeTarget.isDead() == false) {
				if (aoeTarget.equals(target) == false) {
					distance = position.dst( aoeTarget.getPositionCenter());
					if (CollisionDetection.polygonAndCircle( aoeTarget.getBody(), aoeRadius)) {
						damagePercent = (1000 / distance);
						if (damagePercent > 100) {
							damagePercent = 100;
						}
						damage = (attacker.getAttack() * (damagePercent / 100));
						aoeTarget.takeDamage(damage);
						if (aoeTarget.isDead() && attacker instanceof Tower) {
							if (((Tower)attacker).isDead() == false) {
								if (Logger.DEBUG)
									System.out.println("Explosion: giving kill to attacker");
								((Tower) attacker).giveKill();
							}
						}
					}
				}
			}
		}
	}

}
