package com.eric.mtd.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.projectile.Bullet;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.game.service.actorfactory.ActorFactory.BulletPool;
import com.eric.mtd.game.service.actorfactory.ActorFactory.CombatActorPool;
import com.eric.mtd.util.MTDAudio;
import com.eric.mtd.util.MTDAudio.ProjectileSound;
import com.eric.mtd.util.Dimension;
import com.eric.mtd.util.Logger;

/**
 * Represents an Enemy Sniper
 * 
 * @author Eric
 *
 */
public class EnemySniper extends Enemy {
	public static final float HEALTH = 8;
	public static final float ARMOR = 4;
	public static final float ATTACK = 5;
	public static final float ATTACK_SPEED = 1;
	public static final float RANGE = 100;
	public static final float SPEED = 70f;
	public static final Dimension BULLET_SIZE = new Dimension(10, 10);
	public static final float[] BODY = { 5, 22, 5, 34, 26, 34, 26, 22 };
	public static final Vector2 GUN_POS = new Vector2(4, 26);
	public static final Dimension TEXTURE_SIZE = new Dimension(32, 56);
	private BulletPool bulletPool;
	private MTDAudio audio;
	public EnemySniper(TextureRegion[] actorRegions, CombatActorPool<CombatActor> pool, BulletPool bulletPool, MTDAudio audio) {
		super(actorRegions, pool, BODY, TEXTURE_SIZE, GUN_POS, SPEED, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE);
		this.bulletPool = bulletPool;
		this.audio = audio;
	}

	@Override
	public void attackTarget() {
		if (Logger.DEBUG)
			System.out.println("Enemy Sniper: Attacking target at " + getTarget().getPositionCenter());
		if(getTarget() != null){
			audio.playProjectileSound(ProjectileSound.SNIPER);
			getProjectileGroup().addActor(bulletPool.obtain().initialize(this, getTarget(), this.getGunPos(), BULLET_SIZE));
		}

	}
}