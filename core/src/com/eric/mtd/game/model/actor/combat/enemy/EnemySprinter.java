package com.eric.mtd.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.interfaces.IPassiveEnemy;
import com.eric.mtd.game.service.actorfactory.ActorFactory.CombatActorPool;
import com.eric.mtd.util.Dimension;

/**
 * Represents an Enemy Sprint A passive enemy.
 * 
 * @author Eric
 *
 */
public class EnemySprinter extends Enemy implements IPassiveEnemy {

	public static final float HEALTH = 8;
	public static final float ARMOR = 4;
	public static final float ATTACK = 0;
	public static final float ATTACK_SPEED = 0f;
	public static final float RANGE = 0;
	public static final float SPEED = 100f;

	public static final float[] BODY = { 5, 22, 5, 34, 26, 34, 26, 22 };
	public static final Vector2 GUN_POS = new Vector2(4, 26);
	public static final Dimension TEXTURE_SIZE = new Dimension(32, 56);

	public EnemySprinter(TextureRegion[] actorRegions, CombatActorPool<CombatActor> pool) {
		super(actorRegions, pool, BODY, TEXTURE_SIZE, GUN_POS, SPEED, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE);
	}

	@Override
	public void attackTarget() {
		// Does not attack

	}
}