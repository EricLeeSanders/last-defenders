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
 * Represents an RPG
 * 
 * @author Eric
 *
 */
public class RPG extends Actor implements Pool.Poolable {
	private static final float SPEED = 350f;
	private ITargetable target;
	private IAttacker shooter;
	private Group targetGroup;
	private Vector2 destination;
	private Pool<RPG> pool;
	private float radius;
	private ExplosionPool explosionPool;
	private TextureRegion rpgTexture;
	public RPG(Pool<RPG> pool, ExplosionPool explosionPool, TextureRegion rpgTexture){
		this.pool = pool;
		this.explosionPool = explosionPool;
		this.rpgTexture = rpgTexture;
	}
	/**
	 * Initializes an RPG
	 * 
	 * @param shooter
	 * @param target
	 * @param pos
	 *            - Position to spawn the RPG
	 * @param size
	 *            - Size of the RPG
	 */
	public Actor initialize(IAttacker shooter, ITargetable target, Group targetGroup, Vector2 pos, Dimension size, float radius) {
		this.target = target;
		this.shooter = shooter;
		this.targetGroup = targetGroup;
		this.radius = radius;
		this.setSize(size.getWidth(), size.getHeight());
		this.setOrigin(size.getWidth() / 2, size.getHeight() / 2);

		float startX = ActorUtil.calcXBotLeftFromCenter(pos.x, size.getWidth());
		float startY = ActorUtil.calcYBotLeftFromCenter(pos.y, size.getHeight());
		this.setPosition(startX, startY);

		destination = target.getPositionCenter();

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
		batch.draw(rpgTexture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}

	/**
	 * Determines when the rpg has reached its destination and when it should be
	 * freed to the pool. If the shooter is a tower, then it handles giving the
	 * Tower a kill.
	 *
	 * When the RPG reaches its destination, create an explosion
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
		target = null;
		shooter = null;
		destination = null;
		radius = 0;
		this.remove();
	}


}
