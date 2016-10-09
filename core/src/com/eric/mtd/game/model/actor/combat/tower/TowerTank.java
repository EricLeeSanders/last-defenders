package com.eric.mtd.game.model.actor.combat.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.deatheffect.DeathEffectType;
import com.eric.mtd.game.model.actor.health.interfaces.IPlatedArmor;
import com.eric.mtd.game.model.actor.interfaces.IRotatable;
import com.eric.mtd.game.model.actor.interfaces.IRpg;
import com.eric.mtd.game.model.actor.interfaces.IVehicle;
import com.eric.mtd.game.model.actor.projectile.RPG;
import com.eric.mtd.game.service.factory.ActorFactory;
import com.eric.mtd.game.service.factory.ActorFactory.CombatActorPool;
import com.eric.mtd.game.service.factory.ActorFactory.RPGPool;
import com.eric.mtd.game.service.factory.interfaces.IDeathEffectFactory;
import com.eric.mtd.game.service.factory.interfaces.IProjectileFactory;
import com.eric.mtd.util.Dimension;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

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
	public static final int COST = 1500;
	public static final int ARMOR_COST = 1200;
	public static final int RANGE_INCREASE_COST = 650;
	public static final int SPEED_INCREASE_COST = 650;
	public static final int ATTACK_INCREASE_COST = 650;
	public static final float AOE_RADIUS = 75f;
	public static final Dimension BULLET_SIZE = new Dimension(10, 10);
	public static final float[] BODY = { 0, 0, 0, 75, 50, 75, 50, 0 };
	public static final Vector2 GUN_POS = new Vector2(0, 57);
	public static final Dimension TEXTURE_BODY_SIZE = new Dimension(50, 76);
	public static final Dimension TEXTURE_TURRET_SIZE = new Dimension(22, 120);
	private TextureRegion bodyRegion;
	private TextureRegion turretRegion;
	private ShapeRenderer bodyOutline = Resources.getShapeRenderer();
	private ShapeRenderer body = Resources.getShapeRenderer();
	private float bodyRotation;
	private IDeathEffectFactory deathEffectFactory;
	private IProjectileFactory projectileFactory;
	public TowerTank(TextureRegion bodyRegion, TextureRegion turretRegion, CombatActorPool<CombatActor> pool, Group targetGroup, IDeathEffectFactory deathEffectFactory, IProjectileFactory projectileFactory) {
		super(turretRegion, pool, targetGroup, BODY, TEXTURE_TURRET_SIZE, GUN_POS, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, COST, ARMOR_COST, RANGE_INCREASE_COST, SPEED_INCREASE_COST, ATTACK_INCREASE_COST);
		this.bodyRegion = bodyRegion;
		this.turretRegion = turretRegion;
		this.deathEffectFactory = deathEffectFactory;
		this.projectileFactory = projectileFactory;

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
		if (Logger.DEBUG) {
			batch.end();
			body.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
			body.begin(ShapeType.Line);
			body.setColor(Color.YELLOW);
			body.polygon(getBody().getTransformedVertices());
			body.end();
			
			bodyOutline.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
			bodyOutline.begin(ShapeType.Line);
			bodyOutline.setColor(Color.YELLOW);
			bodyOutline.rect(getX(),getY(), getTextureSize().getWidth(), getTextureSize().getHeight());
			bodyOutline.end();
			batch.begin();
		}

		batch.draw(bodyRegion, this.getPositionCenter().x - (TEXTURE_BODY_SIZE.getWidth() / 2), this.getPositionCenter().y - (TEXTURE_BODY_SIZE.getHeight() / 2)
				, TEXTURE_BODY_SIZE.getWidth() / 2, TEXTURE_BODY_SIZE.getHeight() / 2, TEXTURE_BODY_SIZE.getWidth(), TEXTURE_BODY_SIZE.getHeight()
				, 1, 1, bodyRotation);
		batch.draw(turretRegion, getX(), getY(), getOriginX(), getOriginY(), TEXTURE_TURRET_SIZE.getWidth(), TEXTURE_TURRET_SIZE.getHeight(), 1, 1, getRotation());
	}

	/**
	 * Body of the Tank. CombatActor/Tower holds the Turret but not the body Which
	 * we don't care about for collision detection.
	 */
	@Override
	public Polygon getBody() {
		Polygon poly = new Polygon(BODY);
		poly.setOrigin((TEXTURE_BODY_SIZE.getWidth() / 2), (TEXTURE_BODY_SIZE.getHeight() / 2));
		poly.setRotation(bodyRotation);
		poly.setPosition(getPositionCenter().x - (TEXTURE_BODY_SIZE.getWidth() / 2), getPositionCenter().y - (TEXTURE_BODY_SIZE.getHeight() / 2));

		return poly;
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
	public String getName(){
		return "Tank";
	}

	@Override
	protected void deathAnimation() {
		deathEffectFactory.loadDeathEffect(DeathEffectType.VEHCILE_EXPLOSION).initialize(this.getPositionCenter());;
		
	}

}