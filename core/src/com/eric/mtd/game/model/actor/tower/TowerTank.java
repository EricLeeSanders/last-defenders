package com.eric.mtd.game.model.actor.tower;

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
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.health.interfaces.IPlatedArmor;
import com.eric.mtd.game.model.actor.interfaces.IRotatable;
import com.eric.mtd.game.model.actor.interfaces.IVehicle;
import com.eric.mtd.game.model.actor.projectile.RPG;
import com.eric.mtd.game.model.actor.projectile.interfaces.IRPG;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.game.service.actorfactory.ActorFactory.GameActorPool;
import com.eric.mtd.util.Logger;

/**
 * Represents a Tower Tank
 * 
 * @author Eric
 *
 */
public class TowerTank extends Tower implements IVehicle, IRPG, IPlatedArmor, IRotatable {

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
	public static final Vector2 BULLET_SIZE = new Vector2(10, 10);
	public static final float[] BODY = { 0, 0, 0, 75, 50, 75, 50, 0 };
	public static final Vector2 GUN_POS = new Vector2(0, 57);
	public static final Vector2 TEXTURE_BODY_SIZE = new Vector2(50, 76);
	public static final Vector2 TEXTURE_TURRET_SIZE = new Vector2(22, 120);
	private TextureRegion bodyRegion;
	private TextureRegion turretRegion;
	private ShapeRenderer body = new ShapeRenderer();
	private ShapeRenderer rangeShape = new ShapeRenderer();
	private float bodyRotation;

	public TowerTank(TextureRegion bodyRegion, TextureRegion turretRegion, GameActorPool<GameActor> pool) {
		super(turretRegion, pool, BODY, TEXTURE_TURRET_SIZE, GUN_POS, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, COST, ARMOR_COST, RANGE_INCREASE_COST, SPEED_INCREASE_COST, ATTACK_INCREASE_COST);
		this.bodyRegion = bodyRegion;
		this.turretRegion = turretRegion;
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
		batch.end();
		if (isShowRange()) {
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glEnable(GL20.GL_BLEND);

			rangeShape.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
			rangeShape.begin(ShapeType.Filled);
			rangeShape.setColor(getRangeColor());
			rangeShape.circle(((Circle) getRangeShape()).x, ((Circle) getRangeShape()).y, ((Circle) getRangeShape()).radius);
			rangeShape.end();

		}
		if (Logger.DEBUG) {
			body.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
			body.begin(ShapeType.Line);
			body.setColor(Color.YELLOW);
			body.polygon(getBody().getTransformedVertices());
			body.end();
		}

		batch.begin();
		batch.draw(bodyRegion, this.getPositionCenter().x - (TEXTURE_BODY_SIZE.x / 2), this.getPositionCenter().y - (TEXTURE_BODY_SIZE.y / 2), TEXTURE_BODY_SIZE.x / 2, TEXTURE_BODY_SIZE.y / 2, TEXTURE_BODY_SIZE.x, TEXTURE_BODY_SIZE.y, 1, 1, bodyRotation);
		batch.draw(turretRegion, getX(), getY(), getOriginX(), getOriginY(), TEXTURE_TURRET_SIZE.x, TEXTURE_TURRET_SIZE.y, 1, 1, getRotation());
	}

	/**
	 * Body of the Tank. GameActor/Tower holds the Turret but not the body Which
	 * we don't care about for collision detection.
	 */
	@Override
	public Polygon getBody() {
		Polygon poly = new Polygon(BODY);
		poly.setOrigin((TEXTURE_BODY_SIZE.x / 2), (TEXTURE_BODY_SIZE.y / 2));
		poly.setRotation(bodyRotation);
		poly.setPosition(getPositionCenter().x - (TEXTURE_BODY_SIZE.x / 2), getPositionCenter().y - (TEXTURE_BODY_SIZE.y / 2));

		return poly;
	}

	@Override
	public void reset() {
		super.reset();
		bodyRotation = 0;
	}

	@Override
	public float getAoeRadius() {
		return AOE_RADIUS;
	}

	@Override
	public void attackTarget() {
		if (Logger.DEBUG)
			System.out.println("Tower Tank: Attacking target at " + getTarget().getPositionCenter());
		RPG rpg = ActorFactory.loadRPG();
		rpg.initialize(this, getTarget(), this.getGunPos(), BULLET_SIZE);
	}

	@Override
	public void dispose() {
		if (Logger.DEBUG)
			System.out.println("Tower Tank Disposing");
		body.dispose();
		rangeShape.dispose();
		super.dispose();

	}

}