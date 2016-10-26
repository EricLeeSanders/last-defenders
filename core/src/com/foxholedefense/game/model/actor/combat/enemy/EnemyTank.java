package com.foxholedefense.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.deatheffect.DeathEffectType;
import com.foxholedefense.game.model.actor.health.interfaces.IPlatedArmor;
import com.foxholedefense.game.model.actor.interfaces.IRpg;
import com.foxholedefense.game.model.actor.interfaces.IVehicle;
import com.foxholedefense.game.model.actor.projectile.RPG;
import com.foxholedefense.game.service.factory.ActorFactory;
import com.foxholedefense.game.service.factory.ActorFactory.CombatActorPool;
import com.foxholedefense.game.service.factory.ActorFactory.RPGPool;
import com.foxholedefense.game.service.factory.interfaces.IDeathEffectFactory;
import com.foxholedefense.game.service.factory.interfaces.IProjectileFactory;
import com.foxholedefense.util.Dimension;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

/**
 * Represents an Enemy Tank.
 * 
 * @author Eric
 *
 */
public class EnemyTank extends Enemy implements IPlatedArmor, IVehicle, IRpg {

	public static final float HEALTH = 20;
	public static final float ARMOR = 10;
	public static final float ATTACK = 10;
	public static final float ATTACK_SPEED = 0.9f;
	public static final float RANGE = 100;
	public static final float SPEED = 45;
	public static final float AOE_RADIUS = 75f;
	public static final Dimension BULLET_SIZE = new Dimension(10, 10);
	public static final float[] BODY = { 0, 0, 0, 75, 50, 75, 50, 0 };
	public static final Vector2 GUN_POS = new Vector2(0, 57);
	public static final Dimension TEXTURE_BODY_SIZE = new Dimension(50, 76);
	public static final Dimension TEXTURE_TURRET_SIZE = new Dimension(22, 120);
	private Polygon bodyPoly = new Polygon(BODY);
	private TextureRegion tankBodyRegion;
	private ShapeRenderer body = Resources.getShapeRenderer();
	private float bodyRotation; 
	private IDeathEffectFactory deathEffectFactory;
	private IProjectileFactory projectileFactory;
	public EnemyTank(TextureRegion tankRegion, TextureRegion turretRegion, CombatActorPool<CombatActor> pool, Group targetGroup, IDeathEffectFactory deathEffectFactory, IProjectileFactory projectileFactory) {
		super(turretRegion, pool, targetGroup, BODY, TEXTURE_TURRET_SIZE, GUN_POS, SPEED, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE);
		this.tankBodyRegion = tankRegion;
		this.deathEffectFactory = deathEffectFactory;
		this.projectileFactory = projectileFactory;
	}

	/**
	 * Draws the tank. Handles when to rotate the body of the tank instead of
	 * just the turret.
	 */
	@Override
	public void draw(Batch batch, float alpha) {
		batch.end();
		if (Logger.DEBUG) {
			body.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
			body.begin(ShapeType.Line);
			body.setColor(Color.YELLOW);
			body.polygon(getBody().getTransformedVertices());
			body.end();
		}
		batch.begin();
		// If the tank is not attacking, then rotate the body as well
		if (!isAttacking()) {
			bodyRotation = getRotation();
		}
		batch.draw(tankBodyRegion, this.getPositionCenter().x - (TEXTURE_BODY_SIZE.getWidth() / 2), this.getPositionCenter().y - (TEXTURE_BODY_SIZE.getHeight() / 2)
				, TEXTURE_BODY_SIZE.getWidth() / 2, TEXTURE_BODY_SIZE.getHeight() / 2, TEXTURE_BODY_SIZE.getWidth(), TEXTURE_BODY_SIZE.getHeight()
				, 1, 1, bodyRotation);
		super.draw(batch, alpha);
	}

	/**
	 * Body of the Tank. CombatActor/Enemy holds the Body of the turret Which we
	 * don't care about for collision detection.
	 */
	@Override
	public Polygon getBody() {
		bodyPoly.setOrigin((TEXTURE_BODY_SIZE.getWidth() / 2), (TEXTURE_BODY_SIZE.getHeight() / 2));
		bodyPoly.setRotation(bodyRotation);
		bodyPoly.setPosition(getPositionCenter().x - (TEXTURE_BODY_SIZE.getWidth() / 2), getPositionCenter().y - (TEXTURE_BODY_SIZE.getHeight() / 2));

		return bodyPoly;
	}

	@Override
	public void reset() {
		super.reset();
		bodyRotation = 0;
	}

	@Override
	public void attackTarget() {
		if(getTarget() != null){
			projectileFactory.loadRPG().initialize(this, getTarget(), getTargetGroup(), this.getGunPos(), BULLET_SIZE, AOE_RADIUS);
		}
	}

	@Override
	protected void deathAnimation() {
		deathEffectFactory.loadDeathEffect(DeathEffectType.VEHCILE_EXPLOSION).initialize(this.getPositionCenter());;
		
	}

}