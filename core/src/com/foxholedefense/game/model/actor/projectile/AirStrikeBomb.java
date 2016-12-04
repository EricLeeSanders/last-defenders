package com.foxholedefense.game.model.actor.projectile;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.GameStage;
import com.foxholedefense.game.helper.Damage;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.interfaces.IAttacker;
import com.foxholedefense.game.service.factory.ActorFactory;
import com.foxholedefense.game.service.factory.ActorFactory.ExplosionPool;
import com.foxholedefense.util.Dimension;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

/**
 * Represents an AirStrike's Bomb
 * 
 * @author Eric
 *
 */
public class AirStrikeBomb extends Actor implements Pool.Poolable {
	private static final float SPEED = 600f;
	private IAttacker attacker;
	private Vector2 destination;
	private Group targetGroup;
	private Pool<AirStrikeBomb> pool;
	private float radius;
	private ExplosionPool explosionPool;
	private TextureRegion bombTexture;
	public AirStrikeBomb(Pool<AirStrikeBomb> pool, ExplosionPool explosionPool, TextureRegion bombTexture) {
		this.pool = pool;
		this.explosionPool = explosionPool;
		this.bombTexture = bombTexture;
	}
	/**
	 * Initializes and AirStrike Bomb
	 * @param attacker
	 * @param destination
	 * @param targetGroup
	 * @param pos
	 * @param size
	 * @return 
	 */
	public Actor initialize(IAttacker attacker, Vector2 destination, Group targetGroup, Vector2 pos, Dimension size, float radius) {
		this.targetGroup = targetGroup;
		this.setPosition(pos.x, pos.y);
		this.setSize(size.getWidth(), size.getHeight());
		this.setOrigin(size.getWidth() / 2, size.getHeight() / 2);
		this.radius = radius;
		this.attacker = attacker;
		this.destination = destination;
		MoveToAction moveAction = new MoveToAction();
		moveAction.setPosition(destination.x, destination.y);
		moveAction.setDuration(destination.dst(pos) / SPEED);
		addAction(moveAction);
		
		return this;
	}


	@Override
	public void draw(Batch batch, float alpha) {
		batch.draw(bombTexture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
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
			explosionPool.obtain().initialize(attacker, radius,  null, targetGroup, destination);
			pool.free(this);
		}

	}

	@Override
	public void reset() {
		this.clear();
		this.remove();
	}


}
