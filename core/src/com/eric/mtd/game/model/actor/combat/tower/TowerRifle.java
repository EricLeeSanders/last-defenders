package com.eric.mtd.game.model.actor.combat.tower;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.projectile.Bullet;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.game.service.actorfactory.ActorFactory.CombatActorPool;
import com.eric.mtd.util.AudioUtil;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.AudioUtil.ProjectileSound;
import com.eric.mtd.util.Dimension;

/**
 * Represents a Tower Rifle
 * 
 * @author Eric
 *
 */
public class TowerRifle extends Tower {

	public static float HEALTH = 8;
	public static float ARMOR = 4;
	public static float ATTACK = 4;
	public static float ATTACK_SPEED = 0.8f;
	public static float RANGE = 50;
	public static final Dimension BULLET_SIZE = new Dimension(10, 10);
	public static final int COST = 200;
	public static final int ARMOR_COST = 100;
	public static final int RANGE_INCREASE_COST = 50;
	public static final int SPEED_INCREASE_COST = 50;
	public static final int ATTACK_INCREASE_COST = 50;
	public static final float[] BODY = { 5, 22, 5, 34, 26, 34, 26, 22 };
	public static final Vector2 GUN_POS = new Vector2(4, 26);
	public static final Dimension TEXTURE_SIZE = new Dimension(32, 56);

	public TowerRifle(TextureRegion actorRegion, CombatActorPool<CombatActor> pool) {
		super(actorRegion, pool, BODY, TEXTURE_SIZE, GUN_POS, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, COST, ARMOR_COST, RANGE_INCREASE_COST, SPEED_INCREASE_COST, ATTACK_INCREASE_COST);
	}

	@Override
	public void attackTarget() {
		if (Logger.DEBUG)
			System.out.println("Tower Rifle: Attacking target at " + getTarget().getPositionCenter());
		AudioUtil.playProjectileSound(ProjectileSound.RIFLE);
		getProjectileGroup().addActor(ActorFactory.loadBullet().initialize(this, getTarget(), this.getGunPos(), BULLET_SIZE));

	}

}