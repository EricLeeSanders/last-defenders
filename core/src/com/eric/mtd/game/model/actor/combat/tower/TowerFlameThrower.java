package com.eric.mtd.game.model.actor.combat.tower;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.projectile.Flame;
import com.eric.mtd.game.model.actor.projectile.interfaces.IFlame;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.game.service.actorfactory.ActorFactory.CombatActorPool;
import com.eric.mtd.util.AudioUtil;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.AudioUtil.ProjectileSound;

/**
 * Represents a Tower FlameThrower
 * 
 * @author Eric
 *
 */
public class TowerFlameThrower extends Tower implements IFlame {

	public static final float HEALTH = 8;
	public static final float ARMOR = 3;
	public static final float ATTACK = 2; //Damage Per Tick
	public static final float ATTACK_SPEED = 1f;
	public static final float ATTACK_TICK_SPEED = 0.2f;
	public static final float RANGE = 90;
	public static final Vector2 FLAME_SIZE = new Vector2(20, 62);
	public static final int COST = 600;
	public static final int ARMOR_COST = 5665;
	public static final int RANGE_INCREASE_COST = 450;
	public static final int SPEED_INCREASE_COST = 450;
	public static final int ATTACK_INCREASE_COST = 450;
	public static final float[] BODY = { 5, 22, 5, 34, 26, 34, 26, 22 };
	public static final Vector2 GUN_POS = new Vector2(4, 26);
	public static final Vector2 TEXTURE_SIZE = new Vector2(32, 56);

	public TowerFlameThrower(TextureRegion actorRegion, CombatActorPool<CombatActor> pool) {
		super(actorRegion, pool, BODY, TEXTURE_SIZE, GUN_POS, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, COST, ARMOR_COST, RANGE_INCREASE_COST, SPEED_INCREASE_COST, ATTACK_INCREASE_COST);
	}

	@Override
	public Vector2 getFlameSize() {
		return FLAME_SIZE;
	}

	@Override
	public void attackTarget() {
		if (Logger.DEBUG)
			System.out.println("Tower Flame: Attacking target");
		AudioUtil.playProjectileSound(ProjectileSound.FLAME_BURST);
		Flame flame = ActorFactory.loadFlame();
		flame.initialize(this, this.getTarget(), getEnemyGroup(), getFlameSize(), getAttackTickSpeed());

	}

	@Override
	public float getAttackTickSpeed() {
		return ATTACK_TICK_SPEED;
	}

}