package com.foxholedefense.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.deatheffect.DeathEffect;
import com.foxholedefense.game.model.actor.deatheffect.DeathEffectType;
import com.foxholedefense.game.model.actor.interfaces.IPassiveEnemy;
import com.foxholedefense.game.model.actor.interfaces.IVehicle;
import com.foxholedefense.game.service.factory.ActorFactory.CombatActorPool;
import com.foxholedefense.game.service.factory.ActorFactory.DeathEffectPool;
import com.foxholedefense.game.service.factory.interfaces.IDeathEffectFactory;
import com.foxholedefense.game.service.factory.interfaces.IProjectileFactory;
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

	public static final float[] BODY = {0,0, 0,79, 40, 79, 40, 0 };
	public static final Vector2 GUN_POS = new Vector2(0, 0);
	public static final Dimension TEXTURE_SIZE = new Dimension(40, 79);
	
	private IDeathEffectFactory deathEffectFactory;
	
	public EnemyHumvee(TextureRegion actorRegion, CombatActorPool<CombatActor> pool, IDeathEffectFactory deathEffectFactory) {
		super(actorRegion, pool, null, BODY, TEXTURE_SIZE, GUN_POS, SPEED, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE);
		this.deathEffectFactory = deathEffectFactory;
	}

	@Override
	public void attackTarget() {
		// Does not attack

	}

	@Override
	protected void deathAnimation() {
		deathEffectFactory.loadDeathEffect(DeathEffectType.VEHCILE_EXPLOSION).initialize(this.getPositionCenter());;
		
	}
}