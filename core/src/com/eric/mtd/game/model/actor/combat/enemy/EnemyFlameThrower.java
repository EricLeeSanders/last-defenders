package com.eric.mtd.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.interfaces.IFlame;
import com.eric.mtd.game.model.actor.projectile.Flame;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.game.service.actorfactory.ActorFactory.CombatActorPool;
import com.eric.mtd.game.service.actorfactory.ActorFactory.FlamePool;
import com.eric.mtd.util.MTDAudio;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.MTDAudio.ProjectileSound;
import com.eric.mtd.util.Dimension;

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
	public static final float[] BODY = { 5, 22, 5, 34, 26, 34, 26, 22 };
	public static final Vector2 GUN_POS = new Vector2(4, 26);
	public static final Dimension TEXTURE_SIZE = new Dimension(32, 56);
	private Dimension flameSize = new Dimension(20, RANGE-26);
	private FlamePool flamePool;
	private MTDAudio audio;
	public EnemyFlameThrower(TextureRegion[] actorRegions, CombatActorPool<CombatActor> pool, FlamePool flamePool, MTDAudio audio) {
		super(actorRegions, pool, BODY, TEXTURE_SIZE, GUN_POS, SPEED, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE);
		this.flamePool = flamePool;
		this.audio = audio;
	}

	@Override
	public void attackTarget() {
		if (Logger.DEBUG)
			System.out.println("Enemy Flame: Attacking target at " + getTarget().getPositionCenter());
		if(getTarget() != null){
			audio.playProjectileSound(ProjectileSound.FLAME_BURST);
			getProjectileGroup().addActor(flamePool.obtain().initialize(this, this.getTarget(), getTargetGroup(), getFlameSize()));
		}
	}
	@Override
	public Dimension getFlameSize() {
		flameSize.set(flameSize.getWidth(), this.getRange()-GUN_POS.y);
		return flameSize;
	}

}