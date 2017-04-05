package com.foxholedefense.game.model.actor.combat.tower;

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
import com.foxholedefense.game.model.actor.interfaces.IRotatable;
import com.foxholedefense.game.model.actor.interfaces.IRpg;
import com.foxholedefense.game.model.actor.interfaces.ITargetable;
import com.foxholedefense.game.model.actor.interfaces.IVehicle;
import com.foxholedefense.game.service.factory.CombatActorFactory.CombatActorPool;
import com.foxholedefense.game.service.factory.ProjectileFactory;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.DebugOptions;
import com.foxholedefense.util.datastructures.Dimension;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.datastructures.pool.UtilPool;

/**
 * Represents a Tower Tank
 * 
 * @author Eric
 *
 */
public class TowerTank extends Tower implements IVehicle, IPlatedArmor, IRotatable, IRpg  {

	public static final float HEALTH = 20;
	public static final float ARMOR = 10;
	public static final float ATTACK = 10;
	public static final float ATTACK_SPEED = 0.9f;
	public static final float RANGE = 80;
	public static final float AOE_RADIUS = 75f;

	public static final int COST = 1500;
	public static final int ARMOR_COST = 1200;
	public static final int RANGE_INCREASE_COST = 650;
	public static final int SPEED_INCREASE_COST = 650;
	public static final int ATTACK_INCREASE_COST = 650;

	private static final Dimension RPG_SIZE = new Dimension(7, 7);
	private static final Vector2 GUN_POS = UtilPool.getVector2(57, 0);
	private static final Dimension TEXTURE_SIZE_BODY = new Dimension(76,50);
	private static final Dimension TEXTURE_SIZE_TURRET = new Dimension(120, 23);
	private static final DeathEffectType DEATH_EFFECT_TYPE = DeathEffectType.VEHCILE_EXPLOSION;

	private float[] bodyPoints = { 0, 0, 0, 50, 75, 50, 75, 0 };
	private Polygon body;
	private TextureRegion bodyRegion;
	private TextureRegion turretRegion;
	private float bodyRotation;
	private ProjectileFactory projectileFactory;

	public TowerTank(TextureRegion bodyRegion, TextureRegion turretRegion, CombatActorPool<CombatActor> pool, Group targetGroup, TextureRegion rangeRegion, TextureRegion collidingRangeRegion, ProjectileFactory projectileFactory) {
		super(turretRegion, TEXTURE_SIZE_TURRET, pool, targetGroup, GUN_POS, rangeRegion, collidingRangeRegion, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, COST, ARMOR_COST, RANGE_INCREASE_COST, SPEED_INCREASE_COST, ATTACK_INCREASE_COST, DEATH_EFFECT_TYPE);
		this.bodyRegion = bodyRegion;
		this.turretRegion = turretRegion;
		this.projectileFactory = projectileFactory;
		body = new Polygon(bodyPoints);
	}

	/**
	 * Draws the tank. Handles when to rotate the body of the tank instead of
	 * just the turret.
	 */
	@Override
	public void draw(Batch batch, float alpha) {
		// Only Rotate the tank body when the tank is not active
		// (When the tank is being placed)
		if (!isActive()) {
			bodyRotation = getRotation();
		}
		if(isShowRange()){
			drawRange(batch);
		}

		float x = ActorUtil.calcXBotLeftFromCenter(getPositionCenter().x, TEXTURE_SIZE_BODY.getWidth());
		float y = ActorUtil.calcYBotLeftFromCenter(getPositionCenter().y, TEXTURE_SIZE_BODY.getHeight());
		// draw body
		batch.draw(bodyRegion, x, y, TEXTURE_SIZE_BODY.getWidth() / 2, TEXTURE_SIZE_BODY.getHeight() / 2, TEXTURE_SIZE_BODY.getWidth(), TEXTURE_SIZE_BODY.getHeight()
				, 1, 1, bodyRotation);
		batch.draw(turretRegion, getX(), getY(), getOriginX(), getOriginY(), TEXTURE_SIZE_TURRET.getWidth(), TEXTURE_SIZE_TURRET.getHeight(), 1, 1, getRotation());

		if (DebugOptions.showTextureBoundaries) {
			drawDebugBody(batch);
		}
	}

	private void drawDebugBody(Batch batch){
		batch.end();

		ShapeRenderer bodyOutline = Resources.getShapeRenderer();

		bodyOutline.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
		bodyOutline.begin(ShapeType.Line);
		bodyOutline.setColor(Color.YELLOW);
		bodyOutline.polygon(getBody().getTransformedVertices());
		bodyOutline.end();

		batch.begin();
	}

	/**
	 * Body of the Tank. CombatActor/Tower holds the Turret but not the body Which
	 * we don't care about for collision detection.
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
	public String getName(){
		return "Tank";
	}

}