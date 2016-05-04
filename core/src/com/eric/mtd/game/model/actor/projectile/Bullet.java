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
import com.eric.mtd.game.helper.Damage;
import com.eric.mtd.game.model.actor.combat.CombatActor;
import com.eric.mtd.game.model.actor.interfaces.IAttacker;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.game.stage.GameStage;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * Represents a bullet that is shot from an Actor. Has a attacker and a target
 * 
 * @author Eric
 *
 */
public class Bullet extends Projectile {
	private static final float SPEED = 350f;
	private ShapeRenderer bullet = Resources.getShapeRenderer();
	private CombatActor target;
	private IAttacker attacker;
	private Pool<Bullet> pool;
	
	public Bullet(Pool<Bullet> pool){
		super(pool);
		this.pool = pool;
	}

	/**
	 * Initializes the bullet with the following parameters
	 * 
	 * @param attacker
	 * @param target
	 * @param pos
	 *            - Position to spawn the bullet
	 * @param size
	 *            - Size of the bullet
	 */
	public void initialize(IAttacker attacker, CombatActor target, Vector2 pos, Vector2 size) {
		this.target = target;
		this.attacker = attacker;
		this.setPosition(pos.x, pos.y);
		this.setSize(size.x, size.y);
		target.getStage();
		if (target.getStage() instanceof GameStage) {
			((GameStage) target.getStage()).getActorGroups().getProjectileGroup().addActor(this);
		}
		Vector2 end = target.getPositionCenter();
		MoveToAction moveAction = new MoveToAction();
		moveAction.setPosition(end.x, end.y);
		moveAction.setDuration(end.dst(pos) / SPEED);
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
	 * be freed to the pool. If the attacker is a tower, then it handles giving
	 * the Tower a kill.
	 *
	 */
	@Override
	public void act(float delta) {
		super.act(delta);
		if (this.getActions().size == 0) {
			Damage.dealBulletDamage(attacker, target);
			pool.free(this);
		}
	}

	@Override
	public void reset() {
		if (Logger.DEBUG)
			System.out.println("freeing bullet");
		this.clear();
		target = null;
		attacker = null;
		this.remove();
	}

	@Override
	public void initialize(CombatActor shooter, CombatActor target, Group targetGroup) {
		// TODO Auto-generated method stub
		
	}


}
