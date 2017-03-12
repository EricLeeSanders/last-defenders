package com.foxholedefense.game.model.actor.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.FHDGame;
import com.foxholedefense.game.GameStage;
import com.foxholedefense.game.helper.Damage;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.interfaces.IAttacker;
import com.foxholedefense.game.model.actor.interfaces.IFlame;
import com.foxholedefense.game.model.actor.interfaces.ITargetable;
import com.foxholedefense.game.service.factory.ActorFactory;
import com.foxholedefense.util.Dimension;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.UtilPool;

/**
 * Represents a Flame from a FlameThrower. Deals periodic damage as an AOE.
 *
 * @author Eric
 *
 */
public class Flame extends Actor implements Pool.Poolable {

	private static final float FRAME_DURATION = 0.025f;

	private Animation flameAnimation;
	private float stateTime;
	private CombatActor shooter;
	private ShapeRenderer flameOutline = Resources.getShapeRenderer();
	private Dimension flameSize;
	private Pool<Flame> pool;
	private float[] bodyPoints = new float[8];
	private Polygon flameBody;
	private Vector2 maxFlameTextureSize = UtilPool.getVector2();
	/**
	 * Constructs a flame
	 */
	public Flame(Pool<Flame> pool, Array<AtlasRegion> regions) {
		this.pool = pool;
		flameAnimation = new Animation(0.05f, regions);
		flameAnimation.setPlayMode(PlayMode.LOOP);
		for(AtlasRegion region : regions){
			float height = region.getRegionHeight();
			float width = region.getRegionWidth();

			if(height > maxFlameTextureSize.y){
				maxFlameTextureSize.y = height;
			}

			if(width > maxFlameTextureSize.x){
				maxFlameTextureSize.x = width;
			}
		}
		bodyPoints[0] = bodyPoints[1] = bodyPoints[2] = bodyPoints[7] = 0;
	}

	/**
	 * Initializes a flame.
	 *
	 * @param shooter
	 * @param target
	 */
	public Actor initialize(CombatActor shooter, ITargetable target, Group targetGroup, Dimension flameSize) {
		this.shooter = shooter;
		stateTime = 0;
		flameAnimation.setFrameDuration(FRAME_DURATION);
		this.flameSize = flameSize;
		bodyPoints[3] = bodyPoints[5] = flameSize.getHeight();
		bodyPoints[4] = bodyPoints[6] = flameSize.getWidth();
		flameBody = new Polygon(bodyPoints);
		Damage.dealFlameTargetDamage(shooter, target);
		Damage.dealFlameGroupDamage(shooter, target, targetGroup.getChildren(), getFlameBody());
		return this;
	}


	@Override
	public void act(float delta) {
		super.act(delta);
		stateTime += delta;
		if (shooter.isDead() || flameAnimation.isAnimationFinished(stateTime)) {
			pool.free(this);
		}
	}

	/**
	 * Draws the Flame and determines when it has finished.
	 *
	 */
	@Override
	public void draw(Batch batch, float alpha) {
		TextureRegion currentFlame = flameAnimation.getKeyFrame(stateTime, true);
		this.setOrigin(0, currentFlame.getRegionHeight() / 2);
		this.setPosition(shooter.getGunPos().x, shooter.getGunPos().y  - getOriginY());
		setRotation(shooter.getRotation());

		// Scale the textures. Only worry about width of the flameSize while scaling.
		// If the texture's Length != Height then this won't work.
		float heightScale = flameSize.getWidth() / currentFlame.getRegionHeight();
		float widthScale = flameSize.getWidth() / currentFlame.getRegionWidth();

		if (Logger.DEBUG) {
			batch.end();
			Polygon poly = getFlameBody();
			flameOutline.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
			flameOutline.begin(ShapeType.Line);
			flameOutline.setColor(Color.RED);
			flameOutline.polygon(poly.getTransformedVertices());
			flameOutline.end();
			batch.begin();
		}
		batch.draw(currentFlame, this.getX(), this.getY(), this.getOriginX(), this.getOriginY(), currentFlame.getRegionWidth(), currentFlame.getRegionHeight()
				, widthScale, heightScale, this.getRotation());
	}

	/**
	 * Get the flame body.
	 * Must be a polygon because a rectangle can't be rotated.
	 *
	 * @return
	 */
	public Polygon getFlameBody() {
		flameBody.setPosition(shooter.getGunPos().x, shooter.getGunPos().y - (flameSize.getHeight() / 2));
		flameBody.setOrigin(0, flameSize.getHeight() / 2);
		flameBody.setRotation(this.getRotation());
		return flameBody;
	}

	@Override
	public void reset() {
		this.clear();
		this.remove();
		stateTime = 0;
	}


}
