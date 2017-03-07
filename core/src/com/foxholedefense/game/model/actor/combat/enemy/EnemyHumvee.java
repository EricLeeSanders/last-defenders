package com.foxholedefense.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.effects.deatheffect.DeathEffectType;
import com.foxholedefense.game.model.actor.interfaces.IPassiveEnemy;
import com.foxholedefense.game.model.actor.interfaces.ITargetable;
import com.foxholedefense.game.model.actor.interfaces.IVehicle;
import com.foxholedefense.game.service.factory.ActorFactory.CombatActorPool;
import com.foxholedefense.game.service.factory.interfaces.IDeathEffectFactory;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.Dimension;

/**
 * Represents an Enemy Humvee. A passive enemy.
 * 
 * @author Eric
 *
 */
public class EnemyHumvee extends Enemy implements IVehicle, IPassiveEnemy {

	public static final float HEALTH = 16;
	public static final float ARMOR = 8;
	public static final float ATTACK = 0;
	public static final float ATTACK_SPEED = 0f;
	public static final float RANGE = 0;
	public static final float SPEED = 140f;

	public static final Vector2 GUN_POS = new Vector2(0, 0);
	
	private IDeathEffectFactory deathEffectFactory;
	private float[] bodyPoints = {15,0, 15,32, 69,32, 69, 0 };
	private Polygon body;
	
	public EnemyHumvee(TextureRegion stationaryTextureRegion, TextureRegion[] animatedRegions, CombatActorPool<CombatActor> pool, IDeathEffectFactory deathEffectFactory) {
		super(stationaryTextureRegion, animatedRegions, pool, null, GUN_POS, SPEED, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE);
		this.deathEffectFactory = deathEffectFactory;
		this.body = new Polygon(bodyPoints);
	}

	@Override
	public void attackTarget(ITargetable target) {
		// Does not attack

	}

	@Override
	protected void deathAnimation() {
		deathEffectFactory.loadDeathEffect(DeathEffectType.VEHCILE_EXPLOSION).initialize(this.getPositionCenter());;
		
	}

	@Override
	public Polygon getBody() {
		body.setOrigin((this.getWidth() / 2), (this.getHeight() / 2));
		body.setRotation(this.getRotation());
		body.setPosition(ActorUtil.calcXBotLeftFromCenter(getPositionCenter().x, this.getWidth()), ActorUtil.calcYBotLeftFromCenter(getPositionCenter().y, this.getHeight()));
		return body;
	}
}