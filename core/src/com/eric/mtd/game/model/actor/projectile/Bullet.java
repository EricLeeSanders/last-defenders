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
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.game.helper.Damage;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.game.stage.GameStage;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * Represents a bullet that is shot from an Actor. Has a shooter and a target
 * 
 * @author Eric
 *
 */
public class Bullet extends Actor implements Pool.Poolable {
	private static final float SPEED = 350f;
	private ShapeRenderer bullet = Resources.getShapeRenderer();
	private GameActor target, shooter;

	/**
	 * Initializes the bullet with the following parameters
	 * 
	 * @param shooter
	 * @param target
	 * @param pos
	 *            - Position to spawn the bullet
	 * @param size
	 *            - Size of the bullet
	 */
	public void initialize(GameActor shooter, GameActor target, Vector2 pos, Vector2 size) {
		this.target = target;
		this.shooter = shooter;
		this.setPosition(pos.x, pos.y);
		this.setSize(size.x, size.y);
		shooter.getStage();
		if (shooter.getStage() instanceof GameStage) {
			((GameStage) shooter.getStage()).getActorGroups().getBulletGroup().addActor(this);
		}
		Vector2 start = shooter.getGunPos();
		Vector2 end = target.getPositionCenter();
		MoveToAction moveAction = new MoveToAction();
		moveAction.setPosition(end.x, end.y);
		moveAction.setDuration(end.dst(start) / SPEED);
		addAction(moveAction);
	}

	/**
	 * Draw the bullet
	 */
	@Override
	public void draw(Batch batch, float alpha) {
		batch.end();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		bullet.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
		bullet.begin(ShapeType.Filled);
		bullet.setColor(Color.BLACK);
		bullet.circle(getBody().x, getBody().y, 3);
		bullet.end();
		batch.begin();
	}

	/**
	 * Get the body of the bullet
	 * 
	 * @return
	 */
	public Rectangle getBody() {
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
	}

	/**
	 * Determines when the bullet has reached its destination and when it should
	 * be freed to the pool. If the shooter is a tower, then it handles giving
	 * the Tower a kill.
	 *
	 */
	@Override
	public void act(float delta) {
		super.act(delta);
		if (this.getActions().size == 0) {
			Damage.dealBulletDamage(shooter, target);
			ActorFactory.bulletPool.free(this);
		}
	}

	@Override
	public void reset() {
		if (Logger.DEBUG)
			System.out.println("freeing bullet");
		this.clear();
		target = null;
		shooter = null;
		this.remove();
	}


}
