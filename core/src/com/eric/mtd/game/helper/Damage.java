package com.eric.mtd.game.helper;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.health.interfaces.IPlatedArmor;
import com.eric.mtd.game.model.actor.projectile.Flame;
import com.eric.mtd.game.model.actor.projectile.interfaces.IRPG;
import com.eric.mtd.game.model.actor.tower.Tower;
import com.eric.mtd.util.Logger;
/**
 * Class that deals damage
 * @author Eric
 *
 */
public class Damage {
	public static void dealRpgDamage(GameActor shooter, GameActor target) {
		if (shooter instanceof IRPG) {
			if (target.isDead() == false) {
				target.takeDamage(shooter.getAttack());
				if (target.isDead() && shooter instanceof Tower) {
					// Only give the tower a kill if it is alive.
					if (shooter.isDead() == false) {
						((Tower) shooter).giveKill();
						if (Logger.DEBUG)
							System.out.println("RPG: giving kill to shooter");
					}
				}
			}
		}
	}

	public static void dealBulletDamage(GameActor shooter, GameActor target) {
		if ((!(target instanceof IPlatedArmor)) || (shooter instanceof IRPG)) {
			if (target.isDead() == false) {
				target.takeDamage(shooter.getAttack());
				if (target.isDead() && shooter instanceof Tower) {
					// Only give the tower a kill if it is alive.
					if (shooter.isDead() == false) {
						((Tower) shooter).giveKill();
						if (Logger.DEBUG)
							System.out.println("Bullet: giving kill to shooter");
					}
				}
			}
		}
	}

	public static void dealFlameDamage(GameActor shooter, Group targetGroup, Flame flame, float attackDamage) {
		for (Actor flameTarget : targetGroup.getChildren()) {
			Polygon targetBody = ((GameActor) flameTarget).getBody();
			if (CollisionDetection.polygonAndPolygon(targetBody, flame.getFlameBody())) {
				if (((GameActor) flameTarget).isDead() == false) {
					((GameActor) flameTarget).takeDamage(attackDamage);
					if (((GameActor) flameTarget).isDead() && shooter instanceof Tower) {
						// Only give the shooter a kill if it is alive.
						if (shooter.isDead() == false) {
							((Tower) shooter).giveKill();
							if (Logger.DEBUG)
								System.out.println("Flame: giving kill to shooter");
						}
					}
				}
			}
		}
	}

	public static void dealExplosionDamage(GameActor shooter, GameActor target, Group targetGroup) {
		float radius = ((IRPG) shooter).getAoeRadius();
		for (Actor aoeTarget : targetGroup.getChildren()) {
			if (((GameActor) aoeTarget).isDead() == false) {
				if (aoeTarget.equals(target) == false) {
					Circle aoeRadius = new Circle(target.getPositionCenter().x, target.getPositionCenter().y, radius);
					float distance = target.getPositionCenter().dst(((GameActor) aoeTarget).getPositionCenter());
					if (Logger.DEBUG)
						System.out.println("AOE Actor distance: " + distance + " aoe radius: " + aoeRadius.radius);
					if (CollisionDetection.polygonAndCircle(((GameActor) aoeTarget).getBody(), aoeRadius)) {
						float damagePercent = (1000 / distance);// TODO: find
																// better way to
																// do this
						if (damagePercent > 100) {
							damagePercent = 100;
						}
						float damage = (shooter.getAttack() * (damagePercent / 100));
						if (Logger.DEBUG)
							System.out.println("Doing " + damagePercent + "% of damage for " + damage + " damage");
						((GameActor) aoeTarget).takeDamage(damage);
						if (Logger.DEBUG)
							System.out.println("Actors new health:" + ((GameActor) aoeTarget).getHealth());
						if (((GameActor) aoeTarget).isDead() && shooter instanceof Tower) {
							if (shooter.isDead() == false) {
								if (Logger.DEBUG)
									System.out.println("Explosion: giving kill to shooter");
								((Tower) shooter).giveKill();
							}
						}
					}
				}
			}
		}
	}

}
