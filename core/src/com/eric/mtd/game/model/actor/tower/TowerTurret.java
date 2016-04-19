package com.eric.mtd.game.model.actor.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.projectile.Bullet;
import com.eric.mtd.game.model.actor.interfaces.IRotatable;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.game.service.actorfactory.ActorFactory.GameActorPool;
import com.eric.mtd.util.AudioUtil;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.AudioUtil.ProjectileSound;

/**
 * Represents a Tower Turret. Turret is different in that its shape is a
 * triangle instead of a circle
 * 
 * @author Eric
 *
 */
public class TowerTurret extends Tower implements IRotatable {

	public static final float HEALTH = 14;
	public static final float ARMOR = 10;
	public static final float ATTACK = 3;
	public static final float ATTACK_SPEED = .2f;
	public static final float RANGE = 50;
	public static final Vector2 BULLET_SIZE = new Vector2(10, 10);
	public static final int COST = 1300;
	public static final int ARMOR_COST = 900;
	public static final int RANGE_INCREASE_COST = 500;
	public static final int SPEED_INCREASE_COST = 500;
	public static final int ATTACK_INCREASE_COST = 500;

	public static final float[] BODY = { 30, 0, 13, 3, 3, 14, 0, 23, 0, 35, 3, 43, 11, 51, 16, 55, 30, 56, 46, 54, 56, 43, 60, 35, 60, 23, 56, 14, 49, 5 };
	public static final float[] RANGE_COORDS = { 30, 28, -10, 170, 70, 170 };
	public static final Vector2 GUN_POS = new Vector2(4, 26);
	public static final Vector2 TEXTURE_BODY_SIZE = new Vector2(60, 56);
	public static final Vector2 TEXTURE_TURRET_SIZE = new Vector2(32, 56);

	private TextureRegion bodyRegion;
	private TextureRegion turretRegion;
	private ShapeRenderer rangeShape = new ShapeRenderer();
	private float[] rangeTransformedVertices;
	private ShapeRenderer body = new ShapeRenderer();
	private float bodyRotation;

	public TowerTurret(TextureRegion bodyRegion, TextureRegion turretRegion, GameActorPool<GameActor> pool) {
		super(turretRegion, pool, BODY, TEXTURE_TURRET_SIZE, GUN_POS, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, COST, ARMOR_COST, RANGE_INCREASE_COST, SPEED_INCREASE_COST, ATTACK_INCREASE_COST);
		this.bodyRegion = bodyRegion;
		this.turretRegion = turretRegion;
	}

	/**
	 * Draws the turret body. Handles when to rotate the body of the turret
	 * instead of just the turret.
	 */
	@Override
	public void draw(Batch batch, float alpha) {
		// Only rotate the turret body when the turret is not active
		// (When the turret is being placed)
		if (!isActive()) {
			bodyRotation = getRotation();
		}
		batch.end();
		if (Logger.DEBUG) {
			body.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
			body.begin(ShapeType.Line);
			body.setColor(Color.YELLOW);
			body.polygon(getBody().getTransformedVertices());
			body.end();
		}
		if (isShowRange()) {
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glEnable(GL20.GL_BLEND);

			rangeShape.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
			rangeShape.begin(ShapeType.Filled);
			rangeShape.setColor(getRangeColor());
			rangeTransformedVertices = ((Polygon) getRangeShape()).getTransformedVertices();
			rangeShape.triangle(rangeTransformedVertices[0], rangeTransformedVertices[1], rangeTransformedVertices[2], rangeTransformedVertices[3], rangeTransformedVertices[4], rangeTransformedVertices[5]);
			rangeShape.end();
		}
		batch.begin();
		batch.draw(bodyRegion, this.getPositionCenter().x - (TEXTURE_BODY_SIZE.x / 2), this.getPositionCenter().y - (TEXTURE_BODY_SIZE.y / 2), TEXTURE_BODY_SIZE.x / 2, TEXTURE_BODY_SIZE.y / 2, TEXTURE_BODY_SIZE.x, TEXTURE_BODY_SIZE.y, 1, 1, bodyRotation);
		batch.draw(turretRegion, getX(), getY(), getOriginX(), getOriginY(), TEXTURE_TURRET_SIZE.x, TEXTURE_TURRET_SIZE.y, 1, 1, getRotation());

	}

	/**
	 * Body of the Turret. GameActor/Tower holds the Turret but not the body
	 * Which we don't care about for collision detection.
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
	public Shape2D getRangeShape() {
		Polygon poly = new Polygon(RANGE_COORDS);
		poly.setOrigin((TEXTURE_BODY_SIZE.x / 2), (TEXTURE_BODY_SIZE.y / 2));
		poly.setRotation(bodyRotation);
		poly.setPosition(getPositionCenter().x - (TEXTURE_BODY_SIZE.x / 2), getPositionCenter().y - (TEXTURE_BODY_SIZE.y / 2));
		return poly;
	}

	@Override
	public void attackTarget() {
		if (Logger.DEBUG)
			System.out.println("Tower Turret: Attacking target");
		AudioUtil.playProjectileSound(ProjectileSound.MACHINE);
		Bullet bullet = ActorFactory.loadBullet();
		bullet.initialize(this, getTarget(), this.getGunPos(), BULLET_SIZE);

	}

	@Override
	public void dispose() {
		if (Logger.DEBUG)
			System.out.println("Tower Turret Disposing");
		body.dispose();
		rangeShape.dispose();
		super.dispose();

	}

}