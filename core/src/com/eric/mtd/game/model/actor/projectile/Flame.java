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
import com.eric.mtd.game.helper.Damage;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.combat.tower.Tower;
import com.eric.mtd.game.model.actor.projectile.interfaces.IFlame;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.game.stage.GameStage;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * Represents a Flame from a FlameThrower. Deals periodic damage as an AOE.
 * 
 * @author Eric
 *
 */
public class Flame extends Actor implements Pool.Poolable {
	private Animation flameAnimation;
	private TextureRegion currentFlame;
	private float stateTime;
	private TextureRegion[] flameRegions = new TextureRegion[25];
	private CombatActor shooter, target;
	private ShapeRenderer flameOutline = Resources.getShapeRenderer();
	private Group targetGroup;
	Polygon poly = null;
	Polygon targetBodySnap = null;
	private float attackCounter = 0;
	private float attackTick, attackTickDamage;
	private Pool<Flame> pool;

	/**
	 * Constructs a flame
	 */
	public Flame(Pool<Flame> pool) {
		this.pool = pool;
		TextureAtlas flameAtlas = Resources.getAtlas(Resources.FLAMES_ATLAS);
		for (int i = 0; i < 25; i++) {
			flameRegions[i] = flameAtlas.findRegion("Flame" + (i + 1));
		}
	}

	/**
	 * Initializes a flame.
	 * 
	 * @param shooter
	 * @param target
	 */
	public void initialize(CombatActor shooter, CombatActor target, Group targetGroup) {
		this.shooter = shooter;
		this.target = target;
		if (shooter.getStage() instanceof GameStage) {
			((GameStage) shooter.getStage()).getActorGroups().getProjectileGroup().addActor(this);
		}
		stateTime = 0;
		flameAnimation = new Animation(shooter.getAttackSpeed() / 25, flameRegions);
		flameAnimation.setPlayMode(PlayMode.NORMAL);

		attackTick = (shooter.getAttackSpeed() / shooter.getAttack());
		attackTickDamage = 1; // Do a little bit of damage each tick
		this.targetGroup = targetGroup;
	}


	/**
	 * Attacks the Target Group with AOE Damage
	 */
	@Override
	public void act(float delta) {
		super.act(delta);
		if (shooter.isDead()) {
			this.remove();
			pool.free(this);
			System.out.println("Testing actor remove");
			return;
		}
		if (attackCounter >= attackTick) {
			attackCounter = 0;
			Damage.dealFlameDamage(shooter, targetGroup, this, attackTickDamage);
		} else {
			attackCounter += delta;
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
		stateTime += (Gdx.graphics.getDeltaTime() * MTDGame.gameSpeed);
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
		batch.draw(currentFlame, this.getX(), this.getY(), this.getOriginX(), this.getOriginY(), currentFlame.getRegionWidth(), currentFlame.getRegionHeight(), 1, 1, this.getRotation());
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
		Vector2 flameSize = ((IFlame) shooter).getFlameSize();
		float[] bodyPoints = { 0, 0, 0, flameSize.y, flameSize.x, flameSize.y, flameSize.x, 0 };
		Polygon flameBody = new Polygon(bodyPoints);
		flameBody.setPosition(shooter.getGunPos().x - (flameSize.x / 2), shooter.getGunPos().y);
		flameBody.setOrigin((flameSize.x / 2), 0);
		flameBody.setRotation(this.getRotation());

		return flameBody;
	}

	@Override
	public void reset() {
		if (Logger.DEBUG)
			System.out.println("freeing flame");
		this.clear();
		this.remove();
		stateTime = 0;
		flameAnimation = null;
		attackCounter = 0;
		targetGroup = null;
	}


}
