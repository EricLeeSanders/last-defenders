package com.foxholedefense.game.model.actor.combat.tower;

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
 * Represents a Tower Sniper
 * 
 * @author Eric
 *
 */
public class TowerSniper extends Tower {

	public static final float HEALTH = 8;
	public static final float ARMOR = 4;
	public static final float ATTACK = 7;
	public static final float ATTACK_SPEED = 1;
	public static final float RANGE = 100;

	public static final int COST = 400;
	public static final int ARMOR_COST = 200;
	public static final int RANGE_INCREASE_COST = 100;
	public static final int SPEED_INCREASE_COST = 100;
	public static final int ATTACK_INCREASE_COST = 100;

	private static final Dimension BULLET_SIZE = new Dimension(5, 5);
	private static final Vector2 GUN_POS = UtilPool.getVector2(38, -9);
	private static final Dimension TEXTURE_SIZE = new Dimension(78, 31);
	private static final DeathEffectType DEATH_EFFECT_TYPE = DeathEffectType.BLOOD;

	private Circle body;
	private FHDAudio audio;
	private ProjectileFactory projectileFactory;
	
	public TowerSniper(TextureRegion actorRegion, CombatActorPool<CombatActor> pool, Group targetGroup, TextureRegion rangeRegion, TextureRegion collidingRangeRegion, ProjectileFactory projectileFactory, FHDAudio audio) {
		super(actorRegion, TEXTURE_SIZE, pool, targetGroup, GUN_POS, rangeRegion, collidingRangeRegion, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, COST, ARMOR_COST, RANGE_INCREASE_COST, SPEED_INCREASE_COST, ATTACK_INCREASE_COST, DEATH_EFFECT_TYPE);
		this.audio = audio;
		this.projectileFactory = projectileFactory;
		this.body = new Circle(this.getPositionCenter(), 10);
	}

	@Override
	public void attackTarget(ITargetable target) {
		audio.playSound(FHDSound.SNIPER);
		if(target != null){
			projectileFactory.loadBullet().initialize(this, target, this.getGunPos(), BULLET_SIZE);
		}
	}
	
	@Override
	public String getName(){
		return "Sniper";
	}

	@Override
	public Circle getBody() {
		body.setPosition(getPositionCenter().x, getPositionCenter().y);
		return body;
	}
}