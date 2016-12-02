package com.foxholedefense.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.deatheffect.DeathEffectType;
import com.foxholedefense.game.model.actor.interfaces.IRpg;
import com.foxholedefense.game.service.factory.ActorFactory.CombatActorPool;
import com.foxholedefense.game.service.factory.interfaces.IDeathEffectFactory;
import com.foxholedefense.game.service.factory.interfaces.IProjectileFactory;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.Dimension;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDAudio.FHDSound;

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
	public static final Dimension BULLET_SIZE = new Dimension(10, 10);
	public static final Vector2 GUN_POS = new Vector2(4, 26);
	public static final Dimension TEXTURE_SIZE = new Dimension(32, 56);
	private Circle body;
	private FHDAudio audio;
	private IProjectileFactory projectileFactory;
	private IDeathEffectFactory deathEffectFactory;
	public EnemyRocketLauncher(TextureRegion stationaryTextureRegion, TextureRegion[] animatedRegions, CombatActorPool<CombatActor> pool, Group targetGroup, IDeathEffectFactory deathEffectFactory, IProjectileFactory projectileFactory, FHDAudio audio) {
		super(stationaryTextureRegion, animatedRegions, pool, targetGroup, TEXTURE_SIZE, GUN_POS, SPEED, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE);
		this.audio = audio;
		this.projectileFactory = projectileFactory;
		this.deathEffectFactory = deathEffectFactory;
		this.body = new Circle(this.getPositionCenter(), 10);
	}

	@Override
	public void attackTarget() {
		if(getTarget() != null){
			audio.playSound(FHDSound.ROCKET_LAUNCH);
			projectileFactory.loadRPG().initialize(this, getTarget(), getTargetGroup(), this.getGunPos(), BULLET_SIZE, AOE_RADIUS);
		}

	}

	@Override
	protected void deathAnimation() {
		deathEffectFactory.loadDeathEffect(DeathEffectType.BLOOD).initialize(this.getPositionCenter());;
		
	}

	@Override
	public Circle getBody() {
		body.setPosition(getPositionCenter().x, getPositionCenter().y);
		return body;
	}
}