package com.eric.mtd.game.model.actor.tower;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.projectile.RPG;
import com.eric.mtd.game.model.actor.projectile.interfaces.IRPG;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.game.service.actorfactory.ActorFactory.GameActorPool;
import com.eric.mtd.util.AudioUtil;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.AudioUtil.ProjectileSound;

/**
 * Represents a Tower RocketLauncher
 * 
 * @author Eric
 *
 */
public class TowerRocketLauncher extends Tower implements IRPG {

	public static final float HEALTH = 8;
	public static final float ARMOR = 4;
	public static final float ATTACK = 10;
	public static final float ATTACK_SPEED = 1f;
	public static final float RANGE = 60;
	public static final int COST = 800;
	public static final int ARMOR_COST = 500;
	public static final int RANGE_INCREASE_COST = 450;
	public static final int SPEED_INCREASE_COST = 450;
	public static final int ATTACK_INCREASE_COST = 450;
	public static final float AOE_RADIUS = 50f;
	public static final Vector2 BULLET_SIZE = new Vector2(10, 10);
	public static final float[] BODY = { 5, 22, 5, 34, 26, 34, 26, 22 };
	public static final Vector2 GUN_POS = new Vector2(4, 26);
	public static final Vector2 TEXTURE_SIZE = new Vector2(32, 56);

	public TowerRocketLauncher(TextureRegion actorRegion, GameActorPool<GameActor> pool) {
		super(actorRegion, pool, BODY, TEXTURE_SIZE, GUN_POS, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, COST, ARMOR_COST, RANGE_INCREASE_COST, SPEED_INCREASE_COST, ATTACK_INCREASE_COST);
	}

	@Override
	public float getAoeRadius() {
		return AOE_RADIUS;
	}

	@Override
	public void attackTarget() {
		if (Logger.DEBUG)
			System.out.println("Tower Rocket: Attacking target at " + getTarget().getPositionCenter());
		AudioUtil.playProjectileSound(ProjectileSound.ROCKET_LAUNCH);
		RPG rpg = ActorFactory.loadRPG();
		rpg.initialize(this, getTarget(), getEnemyGroup(), this.getGunPos(), BULLET_SIZE);

	}

}