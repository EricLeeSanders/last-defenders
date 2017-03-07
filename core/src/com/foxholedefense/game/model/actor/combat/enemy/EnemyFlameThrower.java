package com.foxholedefense.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.effects.deatheffect.DeathEffectType;
import com.foxholedefense.game.model.actor.interfaces.IFlame;
import com.foxholedefense.game.model.actor.interfaces.ITargetable;
import com.foxholedefense.game.service.factory.ActorFactory.CombatActorPool;
import com.foxholedefense.game.service.factory.interfaces.IDeathEffectFactory;
import com.foxholedefense.game.service.factory.interfaces.IProjectileFactory;
import com.foxholedefense.util.Dimension;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDAudio.FHDSound;

/**
 * Represents an Enemy FlameThrower
 * 
 * @author Eric
 *
 */
public class EnemyFlameThrower extends Enemy implements IFlame {
	public static final float HEALTH = 8;
	public static final float ARMOR = 3;
	public static final float ATTACK = 10; 
	public static final float ATTACK_SPEED = 1f;
	public static final float RANGE = 80;
	public static final float SPEED = 70f;
	public static final Vector2 GUN_POS = new Vector2(26, -4);
	private Circle body;
	private Dimension flameSize = new Dimension(20, RANGE-26);
	private FHDAudio audio;
	private IDeathEffectFactory deathEffectFactory;
	private IProjectileFactory projectileFactory;
	public EnemyFlameThrower(TextureRegion stationaryTextureRegion, TextureRegion[] animatedRegions, CombatActorPool<CombatActor> pool, Group targetGroup, IDeathEffectFactory deathEffectFactory, IProjectileFactory projectileFactory, FHDAudio audio) {
		super(stationaryTextureRegion, animatedRegions, pool, targetGroup, GUN_POS, SPEED, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE);
		this.audio = audio;
		this.deathEffectFactory = deathEffectFactory;
		this.projectileFactory = projectileFactory;
		this.body = new Circle(this.getPositionCenter(), 10);
	}

	@Override
	public void attackTarget(ITargetable target) {
		if(target != null){
			audio.playSound(FHDSound.FLAME_BURST);
			projectileFactory.loadFlame().initialize(this, target, getTargetGroup(), getFlameSize());
		}
	}
	@Override
	public Dimension getFlameSize() {
		flameSize.set(flameSize.getWidth(), this.getRange()-GUN_POS.y);
		return flameSize;
	}

	@Override
	protected void deathAnimation() {
		deathEffectFactory.loadDeathEffect(DeathEffectType.BLOOD).initialize(this.getPositionCenter());
		
	}

	@Override
	public Circle getBody() {
		body.setPosition(getPositionCenter().x, getPositionCenter().y);
		return body;
	}

}