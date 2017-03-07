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
import com.foxholedefense.game.model.actor.effects.deatheffect.DeathEffectType;
import com.foxholedefense.game.model.actor.health.interfaces.IPlatedArmor;
import com.foxholedefense.game.model.actor.interfaces.IRpg;
import com.foxholedefense.game.model.actor.interfaces.ITargetable;
import com.foxholedefense.game.model.actor.interfaces.IVehicle;
import com.foxholedefense.game.service.factory.ActorFactory.CombatActorPool;
import com.foxholedefense.game.service.factory.interfaces.IDeathEffectFactory;
import com.foxholedefense.game.service.factory.interfaces.IProjectileFactory;
import com.foxholedefense.util.ActorUtil;
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
	public static final float RANGE = 80;
	public static final float SPEED = 45;
	public static final float AOE_RADIUS = 75f;
	public static final Dimension RPG_SIZE = new Dimension(7, 7);
	public static final Vector2 GUN_POS = new Vector2(57, 0);
	private float[] bodyPoints = { 0, 0, 0, 50, 75, 50, 75, 0 };
	private TextureRegion bodyRegion;
	private ShapeRenderer bodyOutline = Resources.getShapeRenderer();
	private float bodyRotation;
	private Polygon body;
	private IDeathEffectFactory deathEffectFactory;
	private IProjectileFactory projectileFactory;
	public EnemyTank( TextureRegion bodyRegion, TextureRegion turretRegion, TextureRegion[] animatedRegions, CombatActorPool<CombatActor> pool, Group targetGroup, IDeathEffectFactory deathEffectFactory, IProjectileFactory projectileFactory) {
		super(turretRegion, animatedRegions, pool, targetGroup, GUN_POS, SPEED, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE);
		this.bodyRegion = bodyRegion;
		this.deathEffectFactory = deathEffectFactory;
		this.projectileFactory = projectileFactory;
		body = new Polygon(bodyPoints);
	}

	/**
	 * Draws the tank. Handles when to rotate the body of the tank instead of
	 * just the turret.
	 */
	@Override
	public void draw(Batch batch, float alpha) {
		if (Logger.DEBUG) {
			batch.end();
			bodyOutline.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
			bodyOutline.begin(ShapeType.Line);
			bodyOutline.setColor(Color.YELLOW);
			bodyOutline.polygon(getBody().getTransformedVertices());
			bodyOutline.end();

			batch.begin();
		}

		// If the tank is not attacking, then rotate the body as well
		if (!isAttacking()) {
			bodyRotation = getRotation();
		}
		float x = ActorUtil.calcXBotLeftFromCenter(getPositionCenter().x, bodyRegion.getRegionWidth());
		float y = ActorUtil.calcYBotLeftFromCenter(getPositionCenter().y, bodyRegion.getRegionHeight());
		// draw body
		batch.draw(bodyRegion, x, y, bodyRegion.getRegionWidth() / 2, bodyRegion.getRegionHeight() / 2, bodyRegion.getRegionWidth(), bodyRegion.getRegionHeight()
				, 1, 1, bodyRotation);
		super.draw(batch, alpha);
	}

	/**
	 * Body of the Tank. CombatActor/Enemy holds the Body of the turret Which we
	 * don't care about for collision detection.
	 */
	@Override
	public Polygon getBody() {
		body.setOrigin(bodyRegion.getRegionWidth()/2, bodyRegion.getRegionHeight()/2);
		body.setRotation(bodyRotation);

		float x = ActorUtil.calcXBotLeftFromCenter(getPositionCenter().x, bodyRegion.getRegionWidth());
		float y = ActorUtil.calcYBotLeftFromCenter(getPositionCenter().y, bodyRegion.getRegionHeight());
		body.setPosition(x, y);

		return body;
	}

	@Override
	public void reset() {
		super.reset();
		bodyRotation = 0;
	}

	@Override
	public void attackTarget(ITargetable target) {
		if(target != null){
			projectileFactory.loadRPG().initialize(this, target, getTargetGroup(), this.getGunPos(), RPG_SIZE, AOE_RADIUS);
		}
	}

	@Override
	protected void deathAnimation() {
		deathEffectFactory.loadDeathEffect(DeathEffectType.VEHCILE_EXPLOSION).initialize(this.getPositionCenter());;
		
	}

}