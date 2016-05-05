package com.eric.mtd.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.interfaces.IPassiveEnemy;
import com.eric.mtd.game.model.actor.interfaces.IVehicle;
import com.eric.mtd.game.service.actorfactory.ActorFactory.CombatActorPool;

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
	public static final Vector2 TEXTURE_SIZE = new Vector2(40, 79);

	public EnemyHumvee(TextureRegion actorRegion, CombatActorPool<CombatActor> pool) {
		super(actorRegion, pool, BODY, TEXTURE_SIZE, GUN_POS, SPEED, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE);
	}

	@Override
	public void attackTarget() {
		// Does not attack

	}
}