package com.foxholedefense.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.DeathEffect.DeathEffectType;
import com.foxholedefense.game.model.actor.interfaces.IRocket;
import com.foxholedefense.game.model.actor.interfaces.ITargetable;
import com.foxholedefense.game.service.factory.CombatActorFactory.CombatActorPool;
import com.foxholedefense.game.service.factory.ProjectileFactory;
import com.foxholedefense.util.datastructures.Dimension;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDAudio.FHDSound;
import com.foxholedefense.util.datastructures.pool.UtilPool;

/**
 * Represents an Enemy Rocket Launcher
 * 
 * @author Eric
 *
 */
public class EnemyRocketLauncher extends Enemy implements IRocket {

	private static final float HEALTH = 8;
	private static final float ARMOR = 4;
	private static final float ATTACK = 9;
	private static final float ATTACK_SPEED = 1;
	private static final float RANGE = 60;
	private static final float SPEED = 55f;
	private static final float AOE_RADIUS = 50f;
	private static final int KILL_REWARD = 15;

	private static final Dimension ROCKET_SIZE = new Dimension(23, 5);
	private static final Vector2 GUN_POS = UtilPool.getVector2(25, -10);
	private static final Dimension TEXTURE_SIZE = new Dimension(57, 48);
	private static final DeathEffectType DEATH_EFFECT_TYPE = DeathEffectType.BLOOD;

	private Circle body;
	private FHDAudio audio;
	private ProjectileFactory projectileFactory;

	public EnemyRocketLauncher(TextureRegion stationaryTextureRegion, TextureRegion[] animatedRegions, CombatActorPool<EnemyRocketLauncher> pool, Group targetGroup, ProjectileFactory projectileFactory, FHDAudio audio) {
		super(stationaryTextureRegion, animatedRegions, TEXTURE_SIZE, pool, targetGroup, GUN_POS, SPEED, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, KILL_REWARD, DEATH_EFFECT_TYPE);
		this.audio = audio;
		this.projectileFactory = projectileFactory;
		this.body = new Circle(this.getPositionCenter(), 10);
	}

	@Override
	public void attackTarget(ITargetable target) {
		if(target != null){
			audio.playSound(FHDSound.ROCKET_LAUNCH);
			projectileFactory.loadRocket().initialize(this, target.getPositionCenter(), getTargetGroup(), this.getGunPos(), ROCKET_SIZE, AOE_RADIUS);
		}
	}

	@Override
	public Circle getBody() {
		body.setPosition(getPositionCenter().x, getPositionCenter().y);
		return body;
	}
}