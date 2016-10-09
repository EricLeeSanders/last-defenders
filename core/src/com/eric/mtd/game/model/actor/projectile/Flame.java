package com.eric.mtd.game.model.actor.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.MTDGame;
import com.eric.mtd.game.GameStage;
import com.eric.mtd.game.helper.Damage;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.combat.tower.Tower;
import com.eric.mtd.game.model.actor.interfaces.IAttacker;
import com.eric.mtd.game.model.actor.interfaces.IFlame;
import com.eric.mtd.game.model.actor.interfaces.ITargetable;
import com.eric.mtd.game.service.factory.ActorFactory;
import com.eric.mtd.util.Dimension;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * Represents a Flame from a FlameThrower. Deals periodic damage as an AOE.
 * 
 * @author Eric
 *
 */
public class Flame extends Actor implements Pool.Poolable {
	private static final int NUM_OF_FRAMES = 29;
	private Animation flameAnimation;
	private TextureRegion currentFlame;
	private float stateTime;
	private TextureRegion[] flameRegions = new TextureRegion[NUM_OF_FRAMES];
	private CombatActor shooter;
	private ITargetable target;
	private ShapeRenderer flameOutline = Resources.getShapeRenderer();
	private Group targetGroup;
	private Dimension flameSize;
	Polygon poly = null;
	Polygon targetBodySnap = null;
	private Pool<Flame> pool;
	private float[] bodyPoints = { 0, 0, 0,0,0,0,0, 0 };
	private Polygon flameBody = new Polygon(bodyPoints);
	/**
	 * Constructs a flame
	 */
	public Flame(Pool<Flame> pool, TextureAtlas actorAtlas) {
		this.pool = pool;
		for (int i = 0; i < NUM_OF_FRAMES; i++) {
			flameRegions[i] = actorAtlas.findRegion("Flame" + (i + 1));
		}
	}

	/**
	 * Initializes a flame.
	 * 
	 * @param shooter
	 * @param target
	 */
	public Actor initialize(CombatActor shooter, ITargetable target, Group targetGroup, Dimension flameSize) {
		this.shooter = shooter;
		this.target = target;
		stateTime = 0;
		flameAnimation = new Animation((shooter.getAttackSpeed() * 0.75f) / NUM_OF_FRAMES, flameRegions);
		flameAnimation.setPlayMode(PlayMode.NORMAL);
		this.flameSize = flameSize;
		bodyPoints[3] = bodyPoints[5] = flameSize.getHeight();
		bodyPoints[4] = bodyPoints[6] = flameSize.getWidth();
		this.targetGroup = targetGroup;
		Damage.dealFlameTargetDamage(shooter, target);
		Damage.dealFlameGroupDamage(shooter, target, targetGroup.getChildren(), getFlameBody());
		return this;
	}


	@Override
	public void act(float delta) {
		super.act(delta);
		stateTime += delta;
		if (shooter.isDead()) {
			this.remove();
			pool.free(this);
			return;
		}
	}

	/**
	 * Draws the Flame and determines when it has finished.
	 * 
	 */
	@Override
	public void draw(Batch batch, float alpha) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		currentFlame = flameAnimation.getKeyFrame(stateTime, false);
		this.setOrigin((currentFlame.getRegionWidth() / 2), 0);
		this.setPosition(shooter.getGunPos().x - (currentFlame.getRegionWidth() / 2), shooter.getGunPos().y);
		setRotation(shooter.getRotation());
		batch.end();
		poly = getFlameBody();
		if (Logger.DEBUG) {
			flameOutline.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
			flameOutline.begin(ShapeType.Line);
			flameOutline.setColor(Color.RED);
			flameOutline.polygon(poly.getTransformedVertices());
			flameOutline.end();
		}
		batch.begin();
		batch.draw(currentFlame, this.getX(), this.getY(), this.getOriginX(), this.getOriginY(), currentFlame.getRegionWidth(), currentFlame.getRegionHeight()
				, flameSize.getHeight()/currentFlame.getRegionWidth(),flameSize.getHeight()/currentFlame.getRegionHeight(), this.getRotation());
		if (flameAnimation.isAnimationFinished(stateTime)) {
			pool.free(this);
		}
	}

	/**
	 * Get the flame size
	 * 
	 * @return
	 */
	public Polygon getFlameBody() {
		flameBody.setPosition(shooter.getGunPos().x - (flameSize.getWidth() / 2), shooter.getGunPos().y);
		flameBody.setOrigin((flameSize.getWidth() / 2), 0);
		flameBody.setRotation(this.getRotation());
		return flameBody;
	}

	@Override
	public void reset() {
		this.clear();
		this.remove();
		stateTime = 0;
		flameAnimation = null;
		targetGroup = null;
	}


}
