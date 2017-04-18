package com.foxholedefense.game.model.actor.projectile;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.interfaces.IAttacker;
import com.foxholedefense.game.model.actor.interfaces.ITargetable;
import com.foxholedefense.game.service.factory.ProjectileFactory.ExplosionPool;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.datastructures.Dimension;

/**
 * Represents an Rocket
 * 
 * @author Eric
 *
 */
public class Rocket extends Actor implements Pool.Poolable {
	private static final float SPEED = 350f;
	private IAttacker shooter;
	private Group targetGroup;
	private Vector2 destination = new Vector2(0,0);
	private Pool<Rocket> pool;
	private float radius;
	private ExplosionPool explosionPool;
	private TextureRegion rocketTexture;
	public Rocket(Pool<Rocket> pool, ExplosionPool explosionPool, TextureRegion rocketTexture){
		this.pool = pool;
		this.explosionPool = explosionPool;
		this.rocketTexture = rocketTexture;
	}
	/**
	 * Initializes an Rocket
	 *
	 */
	public Actor initialize(IAttacker shooter, Vector2 destination, Group targetGroup, Vector2 pos, Dimension size, float radius) {
		this.shooter = shooter;
		this.targetGroup = targetGroup;
		this.radius = radius;
		this.destination.set(destination);
		setRotation(ActorUtil.calculateRotation(destination, shooter.getPositionCenter()));
		this.setSize(size.getWidth(), size.getHeight());
		this.setOrigin(size.getWidth() / 2, size.getHeight() / 2);

		float startX = ActorUtil.calcXBotLeftFromCenter(pos.x, size.getWidth());
		float startY = ActorUtil.calcYBotLeftFromCenter(pos.y, size.getHeight());
		this.setPosition(startX, startY);

		MoveToAction moveAction = new MoveToAction();
		float endX = ActorUtil.calcXBotLeftFromCenter(destination.x, size.getWidth());
		float endY = ActorUtil.calcYBotLeftFromCenter(destination.y, size.getHeight());
		moveAction.setPosition(endX, endY);
		moveAction.setDuration(destination.dst(pos) / SPEED);
		addAction(moveAction);
		return this;
	}


	@Override
	public void draw(Batch batch, float alpha) {
		batch.draw(rocketTexture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}

	/**
	 * Determines when the rpg has reached its destination and when it should be
	 * freed to the pool. If the shooter is a tower, then it handles giving the
	 * Tower a kill.
	 *
	 * When the Rocket reaches its destination, create an explosion
	 */
	@Override
	public void act(float delta) {
		super.act(delta);
		if (this.getActions().size == 0) {
			explosionPool.obtain().initialize(shooter, radius, targetGroup, destination);
			pool.free(this);
		}
	}

	@Override
	public void reset() {
		this.clear();
		shooter = null;
		radius = 0;
		this.remove();
	}


}
