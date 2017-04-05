package com.foxholedefense.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.DeathEffect.DeathEffectType;
import com.foxholedefense.game.model.actor.interfaces.IRpg;
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
public class EnemyRocketLauncher extends Enemy implements IRpg {

	public static final float HEALTH = 8;
	public static final float ARMOR = 4;
	public static final float ATTACK = 9;
	public static final float ATTACK_SPEED = 1;
	public static final float RANGE = 60;
	public static final float SPEED = 55f;
	public static final float AOE_RADIUS = 50f;

	private static final Dimension RPG_SIZE = new Dimension(7, 7);
	private static final Vector2 GUN_POS = UtilPool.getVector2(26, -4);
	private static final Dimension TEXTURE_SIZE = new Dimension(57, 48);
	private static final DeathEffectType DEATH_EFFECT_TYPE = DeathEffectType.BLOOD;

	private Circle body;
	private FHDAudio audio;
	private ProjectileFactory projectileFactory;

	public EnemyRocketLauncher(TextureRegion stationaryTextureRegion, TextureRegion[] animatedRegions, CombatActorPool<CombatActor> pool, Group targetGroup, ProjectileFactory projectileFactory, FHDAudio audio) {
		super(stationaryTextureRegion, animatedRegions, TEXTURE_SIZE, pool, targetGroup, GUN_POS, SPEED, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, DEATH_EFFECT_TYPE);
		this.audio = audio;
		this.projectileFactory = projectileFactory;
		this.body = new Circle(this.getPositionCenter(), 10);
	}

	@Override
	public void attackTarget(ITargetable target) {
		if(target != null){
			audio.playSound(FHDSound.ROCKET_LAUNCH);
			projectileFactory.loadRPG().initialize(this, target, getTargetGroup(), this.getGunPos(), RPG_SIZE, AOE_RADIUS);
		}
	}

	@Override
	public Circle getBody() {
		body.setPosition(getPositionCenter().x, getPositionCenter().y);
		return body;
	}
}