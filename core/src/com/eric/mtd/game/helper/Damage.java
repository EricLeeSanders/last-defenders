package com.eric.mtd.game.helper;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.combat.tower.Tower;
import com.eric.mtd.game.model.actor.health.interfaces.IPlatedArmor;
import com.eric.mtd.game.model.actor.interfaces.IAttacker;
import com.eric.mtd.game.model.actor.projectile.Flame;
import com.eric.mtd.game.model.actor.projectile.interfaces.IAoe;
import com.eric.mtd.util.Logger;
/**
 * Class that deals damage
 * @author Eric
 *
 */
public class Damage {
	public static void dealRpgDamage(IAttacker attacker, CombatActor target) {
		if (target.isDead() == false) {
			target.takeDamage(attacker.getAttack());
			if (target.isDead() && attacker instanceof Tower) {
				// Only give the tower a kill if it is alive.
				if (((Tower)attacker).isDead() == false) {
					((Tower) attacker).giveKill();
					if (Logger.DEBUG)
						System.out.println("RPG: giving kill to attacker");
				}
			}
		}
	}

	public static void dealBulletDamage(IAttacker attacker, CombatActor target) {
		if ((!(target instanceof IPlatedArmor)) || (attacker instanceof IAoe)) {
			if (target.isDead() == false) {
				target.takeDamage(attacker.getAttack());
				if (target.isDead() && attacker instanceof Tower) {
					// Only give the tower a kill if it is alive.
					if (((Tower)attacker).isDead() == false) {
						((Tower) attacker).giveKill();
						if (Logger.DEBUG)
							System.out.println("Bullet: giving kill to attacker");
					}
				}
			}
		}
	}

	public static void dealFlameDamage(CombatActor attacker, Group targetGroup, Flame flame, float attackDamage) {
		//Have to create a copy of the group otherwise when a target is killed, the iterator will skip
		//over the next in the group.
		Array<Actor> targetGroupArray = new Array<Actor>(targetGroup.getChildren());
		for (Actor flameTarget : targetGroupArray) {
			Polygon targetBody = ((CombatActor) flameTarget).getBody();
			if (CollisionDetection.polygonAndPolygon(targetBody, flame.getFlameBody())) {
				if (((CombatActor) flameTarget).isDead() == false) {
					((CombatActor) flameTarget).takeDamage(attackDamage);
					if (((CombatActor) flameTarget).isDead() && attacker instanceof Tower) {
						// Only give the attacker a kill if it is alive.
						if (((Tower)attacker).isDead() == false) {
							((Tower) attacker).giveKill();
							if (Logger.DEBUG)
								System.out.println("Flame: giving kill to attacker");
						}
					}
				}
			}
		}
	}

	public static void dealExplosionDamage(IAttacker attacker, Vector2 position, CombatActor target, Group targetGroup) {
		float radius = ((IAoe) attacker).getAoeRadius();
		Circle aoeRadius = new Circle(position.x, position.y, radius);
		//Have to create a copy of the group otherwise when a target is killed, the iterator will skip
		//over the next in the group.
		Array<Actor> targetGroupArray = new Array<Actor>(targetGroup.getChildren());
		if (Logger.DEBUG){
			System.out.println("=============================================================");
			System.out.println("Circle center position = " + aoeRadius.x + "," + aoeRadius.y);
		}
		for (Actor aoeTarget : targetGroupArray) {
			if (Logger.DEBUG){
				System.out.println("AOE Actor pos: " + ((CombatActor) aoeTarget).getPositionCenter() + " group size" + targetGroup.getChildren().size);
			}
			if (((CombatActor) aoeTarget).isDead() == false) {
				if (aoeTarget.equals(target) == false) {
					float distance = position.dst(((CombatActor) aoeTarget).getPositionCenter());
					if (Logger.DEBUG){
						System.out.println("AOE Actor pos: " + ((CombatActor) aoeTarget).getPositionCenter() + " group size" + targetGroup.getChildren().size);
						System.out.println("AOE Actor distance: " + distance + " aoe radius: " + aoeRadius.radius);
					}
					if (CollisionDetection.polygonAndCircle(((CombatActor) aoeTarget).getBody(), aoeRadius)) {
						float damagePercent = (1000 / distance);// TODO: find
																// better way to
																// do this
						if (damagePercent > 100) {
							damagePercent = 100;
						}
						float damage = (attacker.getAttack() * (damagePercent / 100));
						if (Logger.DEBUG)
							System.out.println("Doing " + damagePercent + "% of damage for " + damage + " damage");
						((CombatActor) aoeTarget).takeDamage(damage);
						if (Logger.DEBUG)
							System.out.println("Actors new health:" + ((CombatActor) aoeTarget).getHealth());
						if (((CombatActor) aoeTarget).isDead() && attacker instanceof Tower) {
							if (((Tower)attacker).isDead() == false) {
								if (Logger.DEBUG)
									System.out.println("Explosion: giving kill to attacker");
								((Tower) attacker).giveKill();
							}
						}
					}
				}
				else{
					if (Logger.DEBUG)
						System.out.println("Explosion: AOE Actor = target");
				}
			}
			else{
				if (Logger.DEBUG)
					System.out.println("AOE Target dead");
			}
		}
	}

}
