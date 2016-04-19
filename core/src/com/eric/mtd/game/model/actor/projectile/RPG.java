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
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.game.helper.Damage;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.game.stage.GameStage;
import com.eric.mtd.util.Logger;

/**
 * Represents an RPG
 * 
 * @author Eric
 *
 */
public class RPG extends Actor implements Pool.Poolable {
	private static final float SPEED = 350f;
	private ShapeRenderer rpg = new ShapeRenderer();
	private GameActor target, shooter;
	private Vector2 destination;

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
	public void initialize(GameActor shooter, GameActor target, Vector2 pos, Vector2 size) {
		this.target = target;
		this.shooter = shooter;
		this.setPosition(pos.x, pos.y);
		this.setSize(size.x, size.y);
		if (shooter.getStage() instanceof GameStage) {
			((GameStage) shooter.getStage()).getActorGroups().getBulletGroup().addActor(this);
		}
		Vector2 start = shooter.getGunPos();
		destination = target.getPositionCenter();
		MoveToAction moveAction = new MoveToAction();
		moveAction.setPosition(destination.x, destination.y);
		moveAction.setDuration(destination.dst(start) / SPEED);
		addAction(moveAction);
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
			explosion.initialize(shooter, target, destination);
			ActorFactory.rpgPool.free(this);

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
		this.remove();
	}

}
