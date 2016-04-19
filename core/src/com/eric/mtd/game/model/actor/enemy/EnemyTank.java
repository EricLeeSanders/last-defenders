package com.eric.mtd.game.model.actor.enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.health.interfaces.IPlatedArmor;
import com.eric.mtd.game.model.actor.interfaces.IVehicle;
import com.eric.mtd.game.model.actor.projectile.RPG;
import com.eric.mtd.game.model.actor.projectile.interfaces.IRPG;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.game.service.actorfactory.ActorFactory.GameActorPool;
import com.eric.mtd.util.Logger;

/**
 * Represents an Enemy Tank.
 * 
 * @author Eric
 *
 */
public class EnemyTank extends Enemy implements IPlatedArmor, IRPG, IVehicle {

	public static final float HEALTH = 20;
	public static final float ARMOR = 10;
	public static final float ATTACK = 10;
	public static final float ATTACK_SPEED = 0.9f;
	public static final float RANGE = 100;
	public static final float SPEED = 45;
	public static final float AOE_RADIUS = 75f;
	public static final Vector2 BULLET_SIZE = new Vector2(10, 10);
	public static final float[] BODY = { 0, 0, 0, 75, 50, 75, 50, 0 };
	public static final Vector2 GUN_POS = new Vector2(0, 57);
	public static final Vector2 TEXTURE_BODY_SIZE = new Vector2(50, 76);
	public static final Vector2 TEXTURE_TURRET_SIZE = new Vector2(22, 120);

	private TextureRegion tankBodyRegion;
	private ShapeRenderer body = new ShapeRenderer();
	private float bodyRotation; //

	public EnemyTank(TextureRegion tankRegion, TextureRegion turretRegion, GameActorPool<GameActor> pool) {
		super(turretRegion, pool, BODY, TEXTURE_TURRET_SIZE, GUN_POS, SPEED, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE);
		this.tankBodyRegion = tankRegion;
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
		batch.draw(tankBodyRegion, this.getPositionCenter().x - (TEXTURE_BODY_SIZE.x / 2), this.getPositionCenter().y - (TEXTURE_BODY_SIZE.y / 2), TEXTURE_BODY_SIZE.x / 2, TEXTURE_BODY_SIZE.y / 2, TEXTURE_BODY_SIZE.x, TEXTURE_BODY_SIZE.y, 1, 1, bodyRotation);
		super.draw(batch, alpha);
	}

	/**
	 * Body of the Tank. GameActor/Enemy holds the Body of the turret Which we
	 * don't care about for collision detection.
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
	public float getAoeRadius() {
		return AOE_RADIUS;
	}

	@Override
	public void reset() {
		super.reset();
		bodyRotation = 0;
	}

	@Override
	public void attackTarget() {
		if (Logger.DEBUG)
			System.out.println("Enemy Tank: Attacking target at " + getTarget().getPositionCenter());
		RPG rpg = ActorFactory.loadRPG();
		rpg.initialize(this, getTarget(), this.getGunPos(), BULLET_SIZE);
	}

	@Override
	public void dispose() {
		if (Logger.DEBUG)
			System.out.println("Enemy Tank Disposing");
		body.dispose();
		super.dispose();

	}
}