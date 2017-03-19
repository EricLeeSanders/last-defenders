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
		if (target != null && !target.isDead()) {
			Logger.debug("Doing " + attacker.getAttack() + " damage to: " + target.getClass().getSimpleName());
			target.takeDamage(attacker.getAttack());
			if (target.isDead() && attacker instanceof Tower) {
				// Only give the tower a kill if it is alive.
				if (!((Tower)attacker).isDead()) {
					Logger.debug("Target: giving kill to shooter");
					((Tower) attacker).giveKill();
				}
			}
		}
	}

	public static void dealBulletDamage(IAttacker attacker, ITargetable target) {
		if (!(target instanceof IPlatedArmor)) {
			dealTargetDamage(attacker, target);
		}
	}

	public static void dealFlameGroupDamage(IAttacker attacker, SnapshotArray<Actor>targetGroup, Polygon flameBody) {
		for(int i = targetGroup.size - 1; i >= 0; i--){
			Actor actor = targetGroup.get(i);
			ITargetable flameTarget = (ITargetable) actor;
			if (flameTarget != null && !flameTarget.isDead()) {
				Shape2D targetBody = flameTarget.getBody();
				if (CollisionDetection.flameAndTarget(targetBody, flameBody)) {
					if (!(flameTarget instanceof IPlatedArmor)) {
						dealTargetDamage(attacker, flameTarget);
					}
				}
			}
		}
	}
	private static Circle aoeRadius = new Circle();
	public static void dealExplosionDamage(IAttacker attacker, float radius, Vector2 position, SnapshotArray<Actor>targetGroup) {
		aoeRadius.setPosition(position.x, position.y);
		aoeRadius.setRadius(radius);
		for(int i = targetGroup.size - 1; i >= 0; i--){
			Actor actor = targetGroup.get(i);
			ITargetable aoeTarget = (ITargetable) actor;
			if (!aoeTarget.isDead()) {
				if (CollisionDetection.explosionAndTarget( aoeTarget.getBody(), aoeRadius)) {
					float damage = attacker.getAttack();
					aoeTarget.takeDamage(damage);
					if (aoeTarget.isDead() && attacker instanceof Tower) {
						if (!((Tower)attacker).isDead()) {
							Logger.debug("Explosion: giving kill to shooter");
							((Tower) attacker).giveKill();
						}
					}
				}
			}
		}
	}

}
