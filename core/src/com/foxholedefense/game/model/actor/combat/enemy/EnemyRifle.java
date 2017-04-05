package com.foxholedefense.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.DeathEffect.DeathEffectType;
import com.foxholedefense.game.model.actor.interfaces.ITargetable;
import com.foxholedefense.game.service.factory.CombatActorFactory.CombatActorPool;
import com.foxholedefense.game.service.factory.ProjectileFactory;
import com.foxholedefense.util.datastructures.Dimension;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDAudio.FHDSound;
import com.foxholedefense.util.datastructures.pool.UtilPool;

/**
 * Represents an Enemy Rifle
 * 
 * @author Eric
 *
 */
public class EnemyRifle extends Enemy {

	private static final float HEALTH = 8;
	private static final float ARMOR = 4;
	private static final float ATTACK = 1;
	private static final float ATTACK_SPEED = 0.8f;
	private static final float RANGE = 50;
	private static final float SPEED = 70f;
	private static final int KILL_REWARD = 15;

	private static final Dimension BULLET_SIZE = new Dimension(5, 5);
	private static final Vector2 GUN_POS = UtilPool.getVector2(26, -4);
	private static final Dimension TEXTURE_SIZE = new Dimension(53, 33);
	private static final DeathEffectType DEATH_EFFECT_TYPE = DeathEffectType.BLOOD;

	private Circle body;
	private FHDAudio audio;
	private ProjectileFactory projectileFactory;
	
	public EnemyRifle(TextureRegion stationaryTextureRegion, TextureRegion[] animatedRegions, CombatActorPool<EnemyRifle> pool, Group targetGroup, ProjectileFactory projectileFactory, FHDAudio audio) {
		super(stationaryTextureRegion, animatedRegions, TEXTURE_SIZE, pool, targetGroup, GUN_POS, SPEED, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, KILL_REWARD, DEATH_EFFECT_TYPE);
		this.audio = audio;
		this.projectileFactory = projectileFactory;
		this.body = new Circle(this.getPositionCenter(), 10);
	}

	@Override
	public void attackTarget(ITargetable target) {
		if(target != null){
			audio.playSound(FHDSound.RIFLE);
			projectileFactory.loadBullet().initialize(this, target, this.getGunPos(), BULLET_SIZE);
		}
	}

	@Override
	public Circle getBody() {
		body.setPosition(getPositionCenter().x, getPositionCenter().y);
		return body;
	}
}