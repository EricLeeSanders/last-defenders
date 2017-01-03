package com.foxholedefense.game.model.actor.projectile;


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
import com.foxholedefense.game.GameStage;
import com.foxholedefense.game.helper.Damage;
import com.foxholedefense.game.model.actor.GameActor;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.interfaces.IAttacker;
import com.foxholedefense.game.model.actor.interfaces.ITargetable;
import com.foxholedefense.game.service.factory.ActorFactory;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.Dimension;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

/**
 * Represents a bullet that is shot from an Actor. Has a attacker and a target
 * 
 * @author Eric
 *
 */
public class Bullet extends Actor implements Pool.Poolable{
	private static final float SPEED = 350f;
	private ITargetable target;
	private IAttacker attacker;
	private Pool<Bullet> pool;
	private TextureRegion bulletTexture;
	
	public Bullet(Pool<Bullet> pool, TextureRegion bulletTexture) {
		this.pool = pool;
		this.bulletTexture = bulletTexture;
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
	public Actor initialize(IAttacker attacker, ITargetable target, Vector2 pos, Dimension size ) {
		this.target = target;
		this.attacker = attacker;
		this.setSize(size.getWidth(), size.getHeight());
		this.setOrigin(size.getWidth() / 2, size.getHeight() / 2);

		float startX = ActorUtil.calcXBotLeftFromCenter(pos.x, size.getWidth());
		float startY = ActorUtil.calcYBotLeftFromCenter(pos.y, size.getHeight());
		this.setPosition(startX, startY);

		Vector2 end = target.getPositionCenter();
		MoveToAction moveAction = new MoveToAction();
		float endX = ActorUtil.calcXBotLeftFromCenter(end.x, size.getWidth());
		float endY = ActorUtil.calcYBotLeftFromCenter(end.y, size.getHeight());
		moveAction.setPosition(endX, endY);
		moveAction.setDuration(end.dst(pos) / SPEED);
		addAction(moveAction);
		return this;
	}

	@Override
	public void draw(Batch batch, float alpha) {
		batch.draw(bulletTexture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
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
			return;
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
