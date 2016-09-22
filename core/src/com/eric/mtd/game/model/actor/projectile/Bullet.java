package com.eric.mtd.game.model.actor.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
 * Represents a bullet that is shot from an Actor. Has a attacker and a target
 * 
 * @author Eric
 *
 */
public class Bullet extends Actor implements Pool.Poolable{
	private static final float SPEED = 350f;
	private Sprite bullet;
	private ITargetable target;
	private IAttacker attacker;
	private Pool<Bullet> pool;
	
	public Bullet(Pool<Bullet> pool){
		this.pool = pool;
		createBulletSprite();
	}
	private void createBulletSprite(){
		Pixmap bulletPixmap = new Pixmap(100, 100, Format.RGBA8888);
		bulletPixmap.setColor(0,0,0,1f);
		bulletPixmap.fillCircle(50, 50, 50);
		bullet = (new Sprite(new Texture(bulletPixmap)));
		bulletPixmap.dispose();
		bullet.setSize(5, 5);
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
	public Actor initialize(IAttacker attacker, ITargetable target, Vector2 pos, Dimension size) {
		this.target = target;
		this.attacker = attacker;
		this.setPosition(pos.x, pos.y);
		this.setSize(size.getWidth(), size.getHeight());
		Vector2 end = target.getPositionCenter();
		MoveToAction moveAction = new MoveToAction();
		moveAction.setPosition(end.x, end.y);
		moveAction.setDuration(end.dst(pos) / SPEED);
		addAction(moveAction);
		return this;
	}

	/**
	 * Draw the bullet
	 */
	@Override
	public void draw(Batch batch, float alpha) {
		bullet.setPosition(getX() - (bullet.getWidth()/2), getY() - (bullet.getHeight()/2));
		bullet.draw(batch);
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
		this.clear();
		target = null;
		attacker = null;
		this.remove();
	}


}
