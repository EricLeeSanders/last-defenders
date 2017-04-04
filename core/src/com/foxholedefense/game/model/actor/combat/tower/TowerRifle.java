package com.foxholedefense.game.model.actor.combat.tower;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.DeathEffect.DeathEffectType;
import com.foxholedefense.game.model.actor.interfaces.ITargetable;
import com.foxholedefense.game.service.factory.CombatActorFactory.CombatActorPool;
import com.foxholedefense.game.service.factory.EffectFactory;
import com.foxholedefense.game.service.factory.ProjectileFactory;
import com.foxholedefense.util.datastructures.Dimension;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDAudio.FHDSound;
import com.foxholedefense.util.datastructures.pool.UtilPool;

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
	public static final int COST = 200;
	public static final int ARMOR_COST = 100;
	public static final int RANGE_INCREASE_COST = 50;
	public static final int SPEED_INCREASE_COST = 50;
	public static final int ATTACK_INCREASE_COST = 50;
	private static final Dimension BULLET_SIZE = new Dimension(5, 5);
	private static final Vector2 GUN_POS = UtilPool.getVector2(26, -4);
	private static final Dimension TEXTURE_SIZE = new Dimension(53, 26);
	private Circle body;
	private FHDAudio audio;
	private EffectFactory effectFactory;
	private ProjectileFactory projectileFactory;
	public TowerRifle(TextureRegion actorRegion, CombatActorPool<CombatActor> pool, Group targetGroup, TextureRegion rangeRegion, TextureRegion collidingRangeRegion, EffectFactory effectFactory, ProjectileFactory projectileFactory, FHDAudio audio) {
		super(actorRegion, TEXTURE_SIZE, pool, targetGroup, GUN_POS, rangeRegion, collidingRangeRegion, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, COST, ARMOR_COST, RANGE_INCREASE_COST, SPEED_INCREASE_COST, ATTACK_INCREASE_COST);
		this.audio = audio;
		this.effectFactory = effectFactory;
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
	public String getName(){
		return "Rifle";
	}

	@Override
	protected void deathAnimation() {
		effectFactory.loadDeathEffect(DeathEffectType.BLOOD).initialize(this.getPositionCenter());

	}

	@Override
	public Circle getBody() {
		body.setPosition(getPositionCenter().x, getPositionCenter().y);
		return body;
	}
}