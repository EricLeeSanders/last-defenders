package com.eric.mtd.game.model.actor.combat.tower;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.deatheffect.DeathEffectType;
import com.eric.mtd.game.model.actor.interfaces.IRpg;
import com.eric.mtd.game.model.actor.projectile.RPG;
import com.eric.mtd.game.service.factory.ActorFactory;
import com.eric.mtd.game.service.factory.ActorFactory.CombatActorPool;
import com.eric.mtd.game.service.factory.ActorFactory.RPGPool;
import com.eric.mtd.game.service.factory.interfaces.IDeathEffectFactory;
import com.eric.mtd.game.service.factory.interfaces.IProjectileFactory;
import com.eric.mtd.util.MTDAudio;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.MTDAudio.MTDSound;
import com.eric.mtd.util.Dimension;

/**
 * Represents a Tower RocketLauncher
 * 
 * @author Eric
 *
 */
public class TowerRocketLauncher extends Tower implements IRpg {

	public static final float HEALTH = 8;
	public static final float ARMOR = 4;
	public static final float ATTACK = 10;
	public static final float ATTACK_SPEED = 1f;
	public static final float RANGE = 60;
	public static final int COST = 800;
	public static final int ARMOR_COST = 500;
	public static final int RANGE_INCREASE_COST = 450;
	public static final int SPEED_INCREASE_COST = 450;
	public static final int ATTACK_INCREASE_COST = 450;
	public static final float AOE_RADIUS = 50f;
	public static final Dimension BULLET_SIZE = new Dimension(10, 10);
	public static final float[] BODY = { 5, 22, 5, 34, 26, 34, 26, 22 };
	public static final Vector2 GUN_POS = new Vector2(4, 26);
	public static final Dimension TEXTURE_SIZE = new Dimension(32, 56);
	private MTDAudio audio;
	private IDeathEffectFactory deathEffectFactory;
	private IProjectileFactory projectileFactory;
	public TowerRocketLauncher(TextureRegion actorRegion, CombatActorPool<CombatActor> pool, Group targetGroup, IDeathEffectFactory deathEffectFactory, IProjectileFactory projectileFactory, MTDAudio audio) {
		super(actorRegion, pool, targetGroup, BODY, TEXTURE_SIZE, GUN_POS, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, COST, ARMOR_COST, RANGE_INCREASE_COST, SPEED_INCREASE_COST, ATTACK_INCREASE_COST);
		this.audio = audio;
		this.deathEffectFactory = deathEffectFactory;
		this.projectileFactory = projectileFactory;
	}
	@Override
	public void attackTarget() {
		if(getTarget() != null){
			audio.playSound(MTDSound.ROCKET_LAUNCH);
			projectileFactory.loadRPG().initialize(this, getTarget(), getTargetGroup(), this.getGunPos(), BULLET_SIZE, AOE_RADIUS);
		}
	}
	
	@Override
	public String getName(){
		return "Rocket Launcher";
	}
	
	@Override
	protected void deathAnimation() {
		deathEffectFactory.loadDeathEffect(DeathEffectType.BLOOD).initialize(this.getPositionCenter());;
		
	}
}