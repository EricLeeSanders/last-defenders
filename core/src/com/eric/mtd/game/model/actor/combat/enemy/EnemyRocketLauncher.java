package com.eric.mtd.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.projectile.RPG;
import com.eric.mtd.game.model.actor.projectile.interfaces.IAoe;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.game.service.actorfactory.ActorFactory.CombatActorPool;
import com.eric.mtd.util.AudioUtil;
import com.eric.mtd.util.AudioUtil.ProjectileSound;
import com.eric.mtd.util.Logger;

/**
 * Represents an Enemy Rocket Launcher
 * 
 * @author Eric
 *
 */
public class EnemyRocketLauncher extends Enemy implements IAoe {

	public static final float HEALTH = 8;
	public static final float ARMOR = 4;
	public static final float ATTACK = 9;
	public static final float ATTACK_SPEED = 1;
	public static final float RANGE = 60;
	public static final float SPEED = 55f;
	public static final float AOE_RADIUS = 50f;
	public static final Vector2 BULLET_SIZE = new Vector2(10, 10);
	public static final float[] BODY = { 5, 22, 5, 34, 26, 34, 26, 22 };
	public static final Vector2 GUN_POS = new Vector2(4, 26);
	public static final Vector2 TEXTURE_SIZE = new Vector2(32, 56);

	public EnemyRocketLauncher(TextureRegion[] actorRegions, CombatActorPool<CombatActor> pool) {
		super(actorRegions, pool, BODY, TEXTURE_SIZE, GUN_POS, SPEED, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE);
	}

	@Override
	public float getAoeRadius() {
		return AOE_RADIUS;
	}

	@Override
	public void attackTarget() {
		if (Logger.DEBUG)
			System.out.println("Enemy Rocket: Attacking target at " + getTarget().getPositionCenter());
		AudioUtil.playProjectileSound(ProjectileSound.ROCKET_LAUNCH);
		RPG rpg = ActorFactory.loadRPG();
		rpg.initialize(this, getTarget(), getTowerGroup(), this.getGunPos(), BULLET_SIZE);

	}
}