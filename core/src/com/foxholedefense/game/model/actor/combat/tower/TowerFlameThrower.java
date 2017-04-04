package com.foxholedefense.game.model.actor.combat.tower;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.DeathEffect.DeathEffectType;
import com.foxholedefense.game.model.actor.interfaces.IFlame;
import com.foxholedefense.game.model.actor.interfaces.ITargetable;
import com.foxholedefense.game.service.factory.CombatActorFactory.CombatActorPool;
import com.foxholedefense.game.service.factory.EffectFactory;
import com.foxholedefense.game.service.factory.ProjectileFactory;
import com.foxholedefense.util.datastructures.Dimension;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDAudio.FHDSound;
import com.foxholedefense.util.datastructures.pool.UtilPool;

/**
 * Represents a Tower FlameThrower
 * 
 * @author Eric
 *
 */
public class TowerFlameThrower extends Tower implements IFlame {

	public static final float HEALTH = 800;
	public static final float ARMOR = 4;
	public static final float ATTACK = 0.7f;
	public static final float ATTACK_SPEED = 1.20f;
	public static final float RANGE = 70;
	public static final int COST = 600;
	public static final int ARMOR_COST = 5665;
	public static final int RANGE_INCREASE_COST = 450;
	public static final int SPEED_INCREASE_COST = 450;
	public static final int ATTACK_INCREASE_COST = 450;
	private static final Vector2 GUN_POS = UtilPool.getVector2(26, 4);
	private static final Dimension TEXTURE_SIZE = new Dimension(56, 26);
	private Circle body;
	private Dimension flameSize = new Dimension(RANGE-26, 20);
	private FHDAudio audio;
	private EffectFactory effectFactory;
	private ProjectileFactory projectileFactory;

	public TowerFlameThrower(TextureRegion actorRegion, CombatActorPool<CombatActor> pool, Group targetGroup, TextureRegion rangeRegion, TextureRegion collidingRangeRegion, EffectFactory effectFactory, ProjectileFactory projectileFactory, FHDAudio audio) {
		super(actorRegion, TEXTURE_SIZE, pool, targetGroup, GUN_POS, rangeRegion, collidingRangeRegion, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, COST, ARMOR_COST, RANGE_INCREASE_COST, SPEED_INCREASE_COST, ATTACK_INCREASE_COST);
		this.audio = audio;
		this.effectFactory = effectFactory;
		this.projectileFactory = projectileFactory;
		this.body = new Circle(this.getPositionCenter(), 10);
	}


	@Override
	public Dimension getFlameSize() {
		flameSize.set(this.getRange()-GUN_POS.x, this.getHeight());
		return flameSize;
	}

	@Override
	public void attackTarget(ITargetable target) {
		if(target != null){
			audio.playSound(FHDSound.FLAME_BURST);
			projectileFactory.loadFlame().initialize(this, getTargetGroup(), getFlameSize());
		}
	}
	
	@Override
	public String getName(){
		return "Flame Thrower";
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