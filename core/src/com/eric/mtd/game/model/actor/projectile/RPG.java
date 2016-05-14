package com.eric.mtd.game.model.actor.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.game.GameStage;
import com.eric.mtd.game.helper.Damage;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.interfaces.IAttacker;
import com.eric.mtd.game.model.actor.interfaces.ITargetable;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.util.Dimension;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * Represents an RPG
 * 
 * @author Eric
 *
 */
public class RPG extends Actor implements Pool.Poolable {
	private static final float SPEED = 350f;
	private ShapeRenderer rpg = Resources.getShapeRenderer();
	private ITargetable target;
	private IAttacker shooter;
	private Group targetGroup;
	private Vector2 destination;
	private Pool<RPG> pool;
	private float radius;
	public RPG(Pool<RPG> pool){
		this.pool = pool;
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
		this.setPosition(pos.x, pos.y);
		this.setSize(size.getWidth(), size.getHeight());
		destination = target.getPositionCenter();
		MoveToAction moveAction = new MoveToAction();
		moveAction.setPosition(destination.x, destination.y);
		moveAction.setDuration(destination.dst(pos) / SPEED);
		addAction(moveAction);
		return this;
	}

	/**
	 * Draw the RPG
	 */
	@Override
	public void draw(Batch batch, float alpha) {
		batch.end();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		rpg.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
		rpg.begin(ShapeType.Filled);
		rpg.setColor(Color.BLACK);
		rpg.circle(getBody().x, getBody().y, 3);
		rpg.end();
		batch.begin();
	}

	/**
	 * get the body of the RPG
	 * 
	 * @return
	 */
	public Rectangle getBody() {
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
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
			Damage.dealRpgDamage(shooter, target); // Deal damage
			Explosion explosion = ActorFactory.loadExplosion(); // Get an
																// Explosion
			explosion.initialize(shooter, radius, target, targetGroup, destination);
			pool.free(this);

		}

	}

	@Override
	public void reset() {
		if (Logger.DEBUG)
			System.out.println("freeing RPG");
		this.clear();
		target = null;
		shooter = null;
		destination = null;
		radius = 0;
		this.remove();
	}


}
