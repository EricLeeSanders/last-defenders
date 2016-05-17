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
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.util.Dimension;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * Represents an AirStrike's Bomb
 * 
 * @author Eric
 *
 */
public class AirStrikeBomb extends Actor implements Pool.Poolable {
	private static final float SPEED = 600f;
	private Sprite bomb;
	private IAttacker attacker;
	private Vector2 destination;
	private Group targetGroup;
	private Pool<AirStrikeBomb> pool;
	private float radius;
	public AirStrikeBomb(Pool<AirStrikeBomb> pool){
		this.pool = pool;
		createBombSprite();
	}
	private void createBombSprite(){
		Pixmap bombPixmap = new Pixmap(100, 100, Format.RGBA8888);
		bombPixmap.setColor(0,0,0,1f);
		bombPixmap.fillCircle(50, 50, 50);
		bomb = (new Sprite(new Texture(bombPixmap)));
		bombPixmap.dispose();
		bomb.setSize(10, 10);
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
		this.radius = radius;
		this.attacker = attacker;
		this.destination = destination;
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
		bomb.setPosition(getX() - (bomb.getWidth()/2), getY() - (bomb.getHeight()/2));
		bomb.draw(batch);
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
			Explosion explosion = ActorFactory.loadExplosion(); // Get an
																// Explosion
			explosion.initialize(attacker, radius,  null, targetGroup, destination);
			pool.free(this);

		}

	}

	@Override
	public void reset() {
		if (Logger.DEBUG)
			System.out.println("freeing RPG");
		this.clear();
		this.remove();
	}


}
