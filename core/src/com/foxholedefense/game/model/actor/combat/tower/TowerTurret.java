package com.foxholedefense.game.model.actor.combat.tower;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.DeathEffect.DeathEffectType;
import com.foxholedefense.game.model.actor.interfaces.IRotatable;
import com.foxholedefense.game.model.actor.interfaces.Targetable;
import com.foxholedefense.game.service.factory.CombatActorFactory.CombatActorPool;
import com.foxholedefense.game.service.factory.ProjectileFactory;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.DebugOptions;
import com.foxholedefense.util.datastructures.Dimension;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.FHDAudio.FHDSound;
import com.foxholedefense.util.datastructures.pool.UtilPool;

/**
 * Represents a Tower Turret. Turret is different in that its shape is a
 * triangle instead of a circle
 * 
 * @author Eric
 *
 */
public class TowerTurret extends Tower implements IRotatable {

	private static final float HEALTH = 14;
	private static final float ARMOR = 10;
	private static final float ATTACK = 3;
	private static final float ATTACK_SPEED = .2f;
	private static final float RANGE = 70;

	public static final int COST = 1300;
	private static final int ARMOR_COST = 900;
	private static final int RANGE_INCREASE_COST = 500;
	private static final int SPEED_INCREASE_COST = 500;
	private static final int ATTACK_INCREASE_COST = 500;

	private static final Dimension BULLET_SIZE = new Dimension(5, 5);
	private static final Vector2 GUN_POS = UtilPool.getVector2(8,0);
	private static final Dimension TEXTURE_SIZE_BAGS = new Dimension(67, 49);
	private static final Dimension TEXTURE_SIZE_TURRET = new Dimension(71, 24);
	private static final DeathEffectType DEATH_EFFECT_TYPE = DeathEffectType.BLOOD;

	private float[] rangeCoords = new float[6];
	private float[] bodyPoints = {5, 14, 5, 36, 11, 46, 35, 46, 35, 3, 11, 3};
	private TextureRegion bodyRegion;

	private Polygon body;
	private float bodyRotation;
	private Polygon rangePoly;
	private FHDAudio audio;
	private ProjectileFactory projectileFactory;
	private TextureRegion rangeRegion, collidingRangeRegion;

	public TowerTurret(TextureRegion bodyRegion, TextureRegion turretRegion, CombatActorPool<TowerTurret> pool, Group targetGroup, TextureRegion rangeRegion, TextureRegion collidingRangeRegion, ProjectileFactory projectileFactory, FHDAudio audio) {
		super(turretRegion, TEXTURE_SIZE_TURRET, pool, targetGroup, GUN_POS, rangeRegion, collidingRangeRegion, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, COST, ARMOR_COST, RANGE_INCREASE_COST, SPEED_INCREASE_COST, ATTACK_INCREASE_COST, DEATH_EFFECT_TYPE);
		this.bodyRegion = bodyRegion;
		this.audio = audio;
		this.projectileFactory = projectileFactory;
		body = new Polygon(bodyPoints);
		this.rangeRegion = rangeRegion;
		this.collidingRangeRegion = collidingRangeRegion;

		createRangeCoords();
		rangePoly = new Polygon(rangeCoords);
	}

	private void createRangeCoords(){
		rangeCoords[0] = 0;
		rangeCoords[1] = (RANGE / 2);
		rangeCoords[2] = RANGE;
		rangeCoords[3] = RANGE;
		rangeCoords[4] = RANGE;
		rangeCoords[5] = 0;
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

		if (isShowRange()) {
			drawRange(batch);
		}

		float x = ActorUtil.calcBotLeftPointFromCenter(getPositionCenter().x, TEXTURE_SIZE_BAGS.getWidth());
		float y = ActorUtil.calcBotLeftPointFromCenter(getPositionCenter().y, TEXTURE_SIZE_BAGS.getHeight());

		batch.draw(bodyRegion, x, y	, TEXTURE_SIZE_BAGS.getWidth() / 2, TEXTURE_SIZE_BAGS.getHeight() / 2, TEXTURE_SIZE_BAGS.getWidth(), TEXTURE_SIZE_BAGS.getHeight()
				, 1, 1, bodyRotation);

		super.draw(batch, alpha);

		if (DebugOptions.showTextureBoundaries) {
			drawDebugBody(batch);
		}

	}

	private void drawDebugBody(Batch batch){
		batch.end();

		ShapeRenderer sr = Resources.getShapeRenderer();

		sr.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
		sr.begin(ShapeType.Line);
		sr.setColor(Color.YELLOW);
		sr.polygon(getBody().getTransformedVertices());
		sr.end();

		sr.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
		sr.begin(ShapeType.Line);
		sr.setColor(Color.BLUE);
		sr.rect(getX(),getY(), getWidth(), getHeight());
		sr.end();

		sr.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
		sr.begin(ShapeType.Line);
		sr.setColor(Color.PURPLE);
		sr.polygon(getRangeShape().getTransformedVertices());
		sr.end();

		batch.begin();
	}

	@Override
	protected void drawRange(Batch batch){
		TextureRegion currentRangeRegion = rangeRegion;
		if(isTowerColliding()){
			currentRangeRegion = collidingRangeRegion;
		}
		float width = getRange();
		float height = getRange();
		float x = getPositionCenter().x;
		float y = getPositionCenter().y - (getRange()/2);
		batch.draw(currentRangeRegion,x, y, 0, (getRange()/2), width, height, 1, 1, bodyRotation);
	}

	/**
	 * Body of the Turret. CombatActor/Tower holds the Turret but not the body
	 * Which we don't care about for collision detection.
	 */
	@Override
	public Polygon getBody() {
		// turret is wider, but body is taller
		float width = TEXTURE_SIZE_TURRET.getWidth();
		float height = TEXTURE_SIZE_BAGS.getHeight();

		body.setOrigin(width/2, height/2);
		body.setRotation(bodyRotation);

		float x = getX();
		float y = ActorUtil.calcBotLeftPointFromCenter(getPositionCenter().y, TEXTURE_SIZE_BAGS.getHeight());
		body.setPosition(x, y);

		return body;
	}

	@Override
	public void reset() {
		super.reset();
		bodyRotation = 0;
	}

	@Override
	public Polygon getRangeShape() {
		float x = getPositionCenter().x;
		float y = getPositionCenter().y - (getRange()/2);
		rangePoly.setOrigin(0,(getRange()/2));
		rangePoly.setPosition(x,y);
		rangePoly.setRotation(bodyRotation);

		return rangePoly;
	}

	@Override
	public void attackTarget(Targetable target) {
		if(target != null){
			audio.playSound(FHDSound.MACHINE_GUN);
			projectileFactory.loadBullet().initialize(this,target, BULLET_SIZE);
		}
	}

	@Override
	public void increaseRange() {
		super.increaseRange();

		rangeCoords[0] = 0;
		rangeCoords[1] = (getRange() / 2);
		rangeCoords[2] = getRange();
		rangeCoords[3] = getRange();
		rangeCoords[4] = getRange();
		rangeCoords[5] = 0;

		rangePoly.setVertices(rangeCoords);
	}

	@Override
	public String getName(){
		return "Turret";
	}
}