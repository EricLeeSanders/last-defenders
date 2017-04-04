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
import com.foxholedefense.game.model.actor.effects.texture.animation.death.DeathEffect.DeathEffectType;
import com.foxholedefense.game.model.actor.health.interfaces.IPlatedArmor;
import com.foxholedefense.game.model.actor.interfaces.IRpg;
import com.foxholedefense.game.model.actor.interfaces.ITargetable;
import com.foxholedefense.game.model.actor.interfaces.IVehicle;
import com.foxholedefense.game.service.factory.CombatActorFactory.CombatActorPool;
import com.foxholedefense.game.service.factory.EffectFactory;
import com.foxholedefense.game.service.factory.ProjectileFactory;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.DebugOptions;
import com.foxholedefense.util.datastructures.Dimension;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.datastructures.pool.UtilPool;

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
	private static final Dimension RPG_SIZE = new Dimension(7, 7);
	private static final Vector2 GUN_POS = UtilPool.getVector2(57, 0);
	private static final Dimension TEXTURE_SIZE_BODY = new Dimension(76, 50);
	private static final Dimension TEXTURE_SIZE_TURRET = new Dimension(120, 33);
	private float[] bodyPoints = { 0, 0, 0, 50, 75, 50, 75, 0 };
	private TextureRegion bodyRegion;
	private float bodyRotation;
	private Polygon body;
	private EffectFactory effectFactory;
	private ProjectileFactory projectileFactory;
	public EnemyTank( TextureRegion bodyRegion, TextureRegion turretRegion, TextureRegion[] animatedRegions, CombatActorPool<CombatActor> pool, Group targetGroup, EffectFactory effectFactory, ProjectileFactory projectileFactory) {
		super(turretRegion, animatedRegions, TEXTURE_SIZE_TURRET, pool, targetGroup, GUN_POS, SPEED, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE);
		this.bodyRegion = bodyRegion;
		this.effectFactory = effectFactory;
		this.projectileFactory = projectileFactory;
		body = new Polygon(bodyPoints);
	}

	/**
	 * Draws the tank. Handles when to rotate the body of the tank instead of
	 * just the turret.
	 */
	@Override
	public void draw(Batch batch, float alpha) {

		// If the tank is not attacking, then rotate the body as well
		if (!isAttacking()) {
			bodyRotation = getRotation();
		}
		float x = ActorUtil.calcXBotLeftFromCenter(getPositionCenter().x, TEXTURE_SIZE_BODY.getWidth());
		float y = ActorUtil.calcYBotLeftFromCenter(getPositionCenter().y, TEXTURE_SIZE_BODY.getHeight());
		// draw body
		batch.draw(bodyRegion, x, y, TEXTURE_SIZE_BODY.getWidth() / 2, TEXTURE_SIZE_BODY.getHeight() / 2, TEXTURE_SIZE_BODY.getWidth(), TEXTURE_SIZE_BODY.getHeight()
				, 1, 1, bodyRotation);
		super.draw(batch, alpha);

		if (DebugOptions.showTextureBoundaries) {
			drawDebugBody(batch);
		}
	}

	private void drawDebugBody(Batch batch){
		ShapeRenderer bodyOutline = Resources.getShapeRenderer();
		batch.end();
		bodyOutline.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
		bodyOutline.begin(ShapeType.Line);
		bodyOutline.setColor(Color.YELLOW);
		bodyOutline.polygon(getBody().getTransformedVertices());
		bodyOutline.end();

		batch.begin();
	}

	/**
	 * Body of the Tank. CombatActor/Enemy holds the Body of the turret Which we
	 * don't care about for collision detection.
	 */
	@Override
	public Polygon getBody() {
		body.setOrigin(TEXTURE_SIZE_BODY.getWidth()/2, TEXTURE_SIZE_BODY.getHeight()/2);
		body.setRotation(bodyRotation);

		float x = ActorUtil.calcXBotLeftFromCenter(getPositionCenter().x, TEXTURE_SIZE_BODY.getWidth());
		float y = ActorUtil.calcYBotLeftFromCenter(getPositionCenter().y, TEXTURE_SIZE_BODY.getHeight());
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
		effectFactory.loadDeathEffect(DeathEffectType.VEHCILE_EXPLOSION).initialize(this.getPositionCenter());

	}

}