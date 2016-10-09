package com.eric.mtd.game.model.actor.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import com.eric.mtd.game.service.factory.ActorFactory;
import com.eric.mtd.game.service.factory.ActorFactory.ExplosionPool;
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
	private Sprite rpg;
	private ITargetable target;
	private IAttacker shooter;
	private Group targetGroup;
	private Vector2 destination;
	private Pool<RPG> pool;
	private float radius;
	private ExplosionPool explosionPool;
	public RPG(Pool<RPG> pool, ExplosionPool explosionPool){
		this.pool = pool;
		createRPGSprite();
		this.explosionPool = explosionPool;
	}
	private void createRPGSprite(){
		Pixmap rpgPixmap = new Pixmap(100, 100, Format.RGBA8888);
		rpgPixmap.setColor(0,0,0,1f);
		rpgPixmap.fillCircle(50, 50, 50);
		rpg = (new Sprite(new Texture(rpgPixmap)));
		rpgPixmap.dispose();
		rpg.setSize(10, 10);
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
		rpg.setPosition(getX() - (rpg.getWidth()/2), getY() - (rpg.getHeight()/2));
		rpg.draw(batch);
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
			explosionPool.obtain().initialize(shooter, radius, target, targetGroup, destination);
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
