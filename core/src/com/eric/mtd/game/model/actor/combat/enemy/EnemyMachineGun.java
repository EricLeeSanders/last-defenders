package com.eric.mtd.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.projectile.Bullet;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.game.service.actorfactory.ActorFactory.CombatActorPool;
import com.eric.mtd.util.AudioUtil;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.AudioUtil.ProjectileSound;
import com.eric.mtd.util.Dimension;

/**
 * Represents an Enemy MachineGun
 * 
 * @author Eric
 *
 */
public class EnemyMachineGun extends Enemy {

	public static final float HEALTH = 8;
	public static final float ARMOR = 4;
	public static final float ATTACK = 1;
	public static final float ATTACK_SPEED = 0.2f;
	public static final float RANGE = 50;
	public static final float SPEED = 70f;
	public static final Dimension BULLET_SIZE = new Dimension(10, 10);
	public static final float[] BODY = { 5, 22, 5, 34, 26, 34, 26, 22 };
	public static final Vector2 GUN_POS = new Vector2(4, 26);
	public static final Dimension TEXTURE_SIZE = new Dimension(32, 56);
	public EnemyMachineGun(TextureRegion[] actorRegions, CombatActorPool<CombatActor> pool) {
		super(actorRegions, pool, BODY, TEXTURE_SIZE, GUN_POS, SPEED, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE);
	}

	@Override
	public void attackTarget() {
		if (Logger.DEBUG)
			System.out.println("Enemy Machine: Attacking target at " + getTarget().getPositionCenter());
		if(getTarget() != null){
			AudioUtil.playProjectileSound(ProjectileSound.MACHINE_GUN);
			getProjectileGroup().addActor(ActorFactory.loadBullet().initialize(this, getTarget(), this.getGunPos(), BULLET_SIZE));
		}

	}
}