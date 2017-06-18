package com.foxholedefense.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.DeathEffect.DeathEffectType;
import com.foxholedefense.game.model.actor.interfaces.IPassiveEnemy;
import com.foxholedefense.game.model.actor.interfaces.Targetable;
import com.foxholedefense.game.model.actor.interfaces.IVehicle;
import com.foxholedefense.game.service.factory.CombatActorFactory.CombatActorPool;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.datastructures.Dimension;
import com.foxholedefense.util.datastructures.pool.UtilPool;

/**
 * Represents an Enemy Humvee. A passive enemy.
 * 
 * @author Eric
 *
 */
public class EnemyHumvee extends Enemy implements IVehicle, IPassiveEnemy {

	private static final float HEALTH = 16;
	private static final float ARMOR = 8;
	private static final float ATTACK = 0;
	private static final float ATTACK_SPEED = 0f;
	private static final float RANGE = 0;
	private static final float SPEED = 140f;
	private static final int KILL_REWARD = 15;

	private static final Vector2 GUN_POS = UtilPool.getVector2();
	private static final Dimension TEXTURE_SIZE = new Dimension(74, 32);
	private static final DeathEffectType DEATH_EFFECT_TYPE = DeathEffectType.VEHCILE_EXPLOSION;

	private float[] bodyPoints = {15,0, 15,32, 69,32, 69, 0 };
	private Polygon body;
	
	public EnemyHumvee(TextureRegion stationaryTextureRegion, TextureRegion[] animatedRegions, CombatActorPool<EnemyHumvee> pool) {
		super(stationaryTextureRegion, animatedRegions, TEXTURE_SIZE, pool, null, GUN_POS, SPEED, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, KILL_REWARD, DEATH_EFFECT_TYPE);
		this.body = new Polygon(bodyPoints);
	}

	@Override
	public void attackTarget(Targetable target) {
		// Does not attack
	}

	@Override
	public Polygon getBody() {
		body.setOrigin((this.getWidth() / 2), (this.getHeight() / 2));
		body.setRotation(this.getRotation());
		body.setPosition(ActorUtil.calcBotLeftPointFromCenter(getPositionCenter().x, this.getWidth()), ActorUtil.calcBotLeftPointFromCenter(getPositionCenter().y, this.getHeight()));
		return body;
	}
}