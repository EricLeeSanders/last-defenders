package com.eric.mtd.game.model.actor.combat.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.eric.mtd.game.model.actor.projectile.Bullet;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.interfaces.IRotatable;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.game.service.actorfactory.ActorFactory.CombatActorPool;
import com.eric.mtd.util.AudioUtil;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;
import com.eric.mtd.util.AudioUtil.ProjectileSound;
import com.eric.mtd.util.Dimension;

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
	public static final float RANGE = 70;
	public static final Dimension BULLET_SIZE = new Dimension(10, 10);
	public static final int COST = 1300;
	public static final int ARMOR_COST = 900;
	public static final int RANGE_INCREASE_COST = 500;
	public static final int SPEED_INCREASE_COST = 500;
	public static final int ATTACK_INCREASE_COST = 500;

	public static final float[] BODY = { 30, 0, 13, 3, 3, 14, 0, 23, 0, 35, 3, 43, 11, 51, 16, 55, 30, 56, 46, 54, 56, 43, 60, 35, 60, 23, 56, 14, 49, 5 };
	public static final Vector2 GUN_POS = new Vector2(4, 26);
	public static final Dimension TEXTURE_BODY_SIZE = new Dimension(60, 56);
	public static final Dimension TEXTURE_TURRET_SIZE = new Dimension(32, 56);
	private float[] rangeCoords = { 30, 28, -10, RANGE + (TEXTURE_BODY_SIZE.getHeight() / 2), 70, RANGE + (TEXTURE_BODY_SIZE.getHeight() / 2)};
	private TextureRegion bodyRegion;
	private TextureRegion turretRegion;
	private float[] rangeTransformedVertices;
	private ShapeRenderer body = Resources.getShapeRenderer();
	private ShapeRenderer rangeBody = Resources.getShapeRenderer();
	private ShapeRenderer rangeOutline = Resources.getShapeRenderer();
	private ShapeRenderer bodyOutline = Resources.getShapeRenderer();
	private float bodyRotation;
	private Polygon bodyPoly = new Polygon(BODY);
	private Polygon rangePoly = new Polygon(rangeCoords);

	public TowerTurret(TextureRegion bodyRegion, TextureRegion turretRegion, CombatActorPool<CombatActor> pool) {
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
			bodyOutline.rect(getX(),getY(), TEXTURE_BODY_SIZE.getWidth(), TEXTURE_BODY_SIZE.getHeight());
			bodyOutline.end();
			
			rangeBody.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
			rangeBody.begin(ShapeType.Line);
			rangeBody.setColor(Color.YELLOW);
			rangeBody.polygon(((Polygon)getRangeShape()).getTransformedVertices());
			rangeBody.end();
			batch.begin();
		}
		if (isShowRange()) {
			drawRange(batch);
		}
		batch.draw(bodyRegion, this.getPositionCenter().x - (TEXTURE_BODY_SIZE.getWidth() / 2), this.getPositionCenter().y - (TEXTURE_BODY_SIZE.getHeight() / 2)
				, TEXTURE_BODY_SIZE.getWidth() / 2, TEXTURE_BODY_SIZE.getHeight() / 2, TEXTURE_BODY_SIZE.getWidth(), TEXTURE_BODY_SIZE.getHeight()
				, 1, 1, bodyRotation);
		batch.draw(turretRegion, getX(), getY(), getOriginX(), getOriginY(), TEXTURE_TURRET_SIZE.getWidth(), TEXTURE_TURRET_SIZE.getHeight(), 1, 1, getRotation());

	}
	@Override
	protected void drawRange(Batch batch){
		getRangeSprite().setPosition(getPositionCenter().x - (getRangeSprite().getWidth()/2), getPositionCenter().y);
		getRangeSprite().setRotation(bodyRotation);
		getRangeSprite().draw(batch);
	}
	@Override
	protected void createRangeSprite(){
		Pixmap rangePixmap = new Pixmap(400,400, Format.RGBA8888);
		rangePixmap.setColor(1.0f, 1.0f, 1.0f, 0.5f);
		rangePixmap.fillTriangle(400,400,200,0,0,400);
		setRangeSprite(new Sprite(new Texture(rangePixmap)));
		rangePixmap.dispose();
		getRangeSprite().setOrigin(40,0);
		getRangeSprite().flip(false, true);
		getRangeSprite().setSize(80,70);
	}
	/**
	 * Body of the Turret. CombatActor/Tower holds the Turret but not the body
	 * Which we don't care about for collision detection.
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
	public Shape2D getRangeShape() {
		changeRangeCoords();
		rangePoly.setOrigin((TEXTURE_BODY_SIZE.getWidth() / 2), (TEXTURE_BODY_SIZE.getHeight() / 2));
		rangePoly.setRotation(bodyRotation);
		rangePoly.setPosition(getPositionCenter().x - (TEXTURE_BODY_SIZE.getWidth() / 2), getPositionCenter().y - (TEXTURE_BODY_SIZE.getHeight() / 2));
		return rangePoly;
	}

	@Override
	public void attackTarget() {
		if (Logger.DEBUG)
			System.out.println("Tower Turret: Attacking target");
		if(getTarget() != null){
			AudioUtil.playProjectileSound(ProjectileSound.MACHINE_GUN);
			getProjectileGroup().addActor(ActorFactory.loadBullet().initialize(this, getTarget(), this.getGunPos(), BULLET_SIZE));
		}

	}
	@Override
	public void increaseRange(){
		super.increaseRange();
		rangeCoords[3] = rangeCoords[5] = this.getRange() + (TEXTURE_BODY_SIZE.getHeight() / 2);
		getRangeSprite().setSize(80, this.getRange());
	}
	private void changeRangeCoords(){
		rangeCoords[3] = rangeCoords[5] = this.getRange() + (TEXTURE_BODY_SIZE.getHeight() / 2);
	}

}