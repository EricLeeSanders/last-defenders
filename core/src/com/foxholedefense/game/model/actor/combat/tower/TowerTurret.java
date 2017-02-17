package com.foxholedefense.game.model.actor.combat.tower;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.effects.deatheffect.DeathEffectType;
import com.foxholedefense.game.model.actor.interfaces.IRotatable;
import com.foxholedefense.game.service.factory.ActorFactory.CombatActorPool;
import com.foxholedefense.game.service.factory.interfaces.IDeathEffectFactory;
import com.foxholedefense.game.service.factory.interfaces.IProjectileFactory;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.Dimension;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.FHDAudio.FHDSound;

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
	public static final Dimension BULLET_SIZE = new Dimension(5, 5);
	public static final int COST = 1300;
	public static final int ARMOR_COST = 900;
	public static final int RANGE_INCREASE_COST = 500;
	public static final int SPEED_INCREASE_COST = 500;
	public static final int ATTACK_INCREASE_COST = 500;
	public static final Vector2 GUN_POS = new Vector2(26, -4);
	private float[] rangeCoords = new float[6];
	private TextureRegion bodyRegion;
	private TextureRegion turretRegion;

	private ShapeRenderer rangeOutline = Resources.getShapeRenderer();
	private ShapeRenderer turretOutline = Resources.getShapeRenderer();
	private ShapeRenderer bodyOutline = Resources.getShapeRenderer();
	private Circle body;
	private float bodyRotation;
	private Polygon rangePoly;
	private FHDAudio audio;
	private IDeathEffectFactory deathEffectFactory;
	private IProjectileFactory projectileFactory;
	private TextureRegion rangeRegion, collidingRangeRegion;

	public TowerTurret(TextureRegion bodyRegion, TextureRegion turretRegion, CombatActorPool<CombatActor> pool, Group targetGroup, TextureRegion rangeRegion, TextureRegion collidingRangeRegion, IDeathEffectFactory deathEffectFactory, IProjectileFactory projectileFactory, FHDAudio audio) {
		super(turretRegion, pool, targetGroup, GUN_POS, rangeRegion, collidingRangeRegion, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, COST, ARMOR_COST, RANGE_INCREASE_COST, SPEED_INCREASE_COST, ATTACK_INCREASE_COST);
		this.bodyRegion = bodyRegion;
		this.turretRegion = turretRegion;
		this.audio = audio;
		this.deathEffectFactory = deathEffectFactory;
		this.projectileFactory = projectileFactory;
		this.body = new Circle(this.getPositionCenter(), 27);
		this.rangeRegion = rangeRegion;
		this.collidingRangeRegion = collidingRangeRegion;


		createRangeCoords();
		rangePoly = new Polygon(rangeCoords);
	}

	private void createRangeCoords(){

		rangeCoords[0] = (bodyRegion.getRegionWidth() / 2);
		rangeCoords[1] = (bodyRegion.getRegionHeight() / 2);
		rangeCoords[2] = RANGE + (bodyRegion.getRegionWidth() / 2);
		rangeCoords[3] = (RANGE/2) + (bodyRegion.getRegionHeight() / 2);
		rangeCoords[4] = RANGE + (bodyRegion.getRegionWidth() / 2);
		rangeCoords[5] = (bodyRegion.getRegionHeight() / 2) - (RANGE/2);
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

			bodyOutline.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
			bodyOutline.begin(ShapeType.Line);
			bodyOutline.setColor(Color.YELLOW);
			bodyOutline.circle(getBody().x, getBody().y, getBody().radius);
			bodyOutline.end();

			turretOutline.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
			turretOutline.begin(ShapeType.Line);
			turretOutline.setColor(Color.YELLOW);
			turretOutline.rect(getX(),getY(), getWidth(), getHeight());
			turretOutline.end();
			
			rangeOutline.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
			rangeOutline.begin(ShapeType.Line);
			rangeOutline.setColor(Color.PURPLE);
			rangeOutline.polygon(((Polygon)getRangeShape()).getTransformedVertices());
			rangeOutline.end();
			batch.begin();
		}
		if (isShowRange()) {
			drawRange(batch);
		}

		float x = ActorUtil.calcXBotLeftFromCenter(getPositionCenter().x, bodyRegion.getRegionWidth());
		float y = ActorUtil.calcYBotLeftFromCenter(getPositionCenter().y, bodyRegion.getRegionHeight());

		batch.draw(bodyRegion, x, y	, bodyRegion.getRegionWidth() / 2, bodyRegion.getRegionHeight() / 2, bodyRegion.getRegionWidth(), bodyRegion.getRegionHeight()
				, 1, 1, bodyRotation);
		//batch.draw(turretRegion, getX(), getY(), getOriginX(), getOriginY(), TEXTURE_TURRET_SIZE.getWidth(), TEXTURE_TURRET_SIZE.getHeight(), 1, 1, getRotation());
		super.draw(batch, alpha);

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
	public Circle getBody() {
		body.setPosition(getPositionCenter().x, getPositionCenter().y);
		return body;
	}

	@Override
	public void reset() {
		super.reset();
		bodyRotation = 0;
	}

	@Override
	public Shape2D getRangeShape() {

		rangePoly.setOrigin(getOriginX(), getOriginY());
		rangePoly.setRotation(bodyRotation);

		float x = ActorUtil.calcXBotLeftFromCenter(getPositionCenter().x, bodyRegion.getRegionWidth());
		float y = ActorUtil.calcYBotLeftFromCenter(getPositionCenter().y, bodyRegion.getRegionHeight());
		rangePoly.setPosition(x, y);
		return rangePoly;
	}

	@Override
	public void attackTarget() {
		if(getTarget() != null){
			audio.playSound(FHDSound.MACHINE_GUN);
			projectileFactory.loadBullet().initialize(this, getTarget(), this.getGunPos(), BULLET_SIZE);
		}

	}

	@Override
	public void increaseRange() {
		super.increaseRange();
		rangeCoords[2] = getRange() + (bodyRegion.getRegionWidth() / 2);
		rangeCoords[3] = (getRange()/2) + (bodyRegion.getRegionHeight() / 2);
		rangeCoords[4] = getRange() + (bodyRegion.getRegionWidth() / 2);
		rangeCoords[5] = (bodyRegion.getRegionHeight() / 2) - (getRange()/2);
		rangePoly.setVertices(rangeCoords);
	}

	@Override
	public String getName(){
		return "Turret";
	}

	@Override
	protected void deathAnimation() {
		deathEffectFactory.loadDeathEffect(DeathEffectType.BLOOD).initialize(this.getPositionCenter());;
		
	}

}