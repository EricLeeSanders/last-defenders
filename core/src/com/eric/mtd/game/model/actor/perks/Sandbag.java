package com.eric.mtd.game.model.actor.perks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.game.model.actor.interfaces.ICollision;
import com.eric.mtd.game.service.actorfactory.ActorFactory.SandbagPool;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

public class Sandbag extends Actor implements Pool.Poolable, ICollision {
	private TextureRegion textureRegion;
	private ShapeRenderer shapeRenderer2 = Resources.getShapeRenderer();
	private float health;
	private boolean dead;
	private SandbagPool pool;

	public Sandbag(SandbagPool pool) {
		this.pool = pool;
		TextureAtlas perksAtlas = Resources.getAtlas(Resources.PERKS_ATLAS);
		textureRegion = perksAtlas.findRegion("sandbags");
		this.setOrigin((textureRegion.getRegionWidth() / 2), 0);
	}

	@Override
	public void reset() {
		if (Logger.DEBUG)
			System.out.println("freeing sandbag");
		this.setDead(false);
		this.setRotation(0);
		this.clear();
		this.remove();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (isDead()) {
			pool.free(this);
		}
	}

	public void freeActor() {
		pool.free(this);
	}

	@Override
	public void draw(Batch batch, float alpha) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		// this.setPosition(actor.getGunPos().x-(currentFlame.getRegionWidth()/2),actor.getGunPos().y);
		batch.end();
		shapeRenderer2.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
		shapeRenderer2.begin(ShapeType.Line);
		shapeRenderer2.setColor(Color.RED);
		shapeRenderer2.rect(this.getX(), this.getY(), textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
		shapeRenderer2.end();
		batch.begin(); // Question: Not sure why this needs to be here... and
						// the end above...
		// batch.draw(textureRegion, this.getX(), this.getY());
		batch.draw(textureRegion, this.getX(), this.getY(), this.getOriginX(), this.getOriginY(), textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), 1, 1, this.getRotation());
	}

	public void takeDamage(float damage) {
		health = health - damage;
		if (health <= 0) {
			this.setDead(true);
		}

	}

	@Override
	public void setPositionCenter(Vector2 pos) {
		this.setPosition(pos.x - (textureRegion.getRegionWidth() / 2), pos.y - (textureRegion.getRegionHeight() / 2));
	}

	@Override
	public Vector2 getPositionCenter() {
		return new Vector2(getX() + (textureRegion.getRegionWidth() / 2), getY() + (textureRegion.getRegionHeight() / 2));
	}

	@Override
	public Rectangle getBody() {
		Rectangle body = new Rectangle(this.getX(), this.getY(), textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
		return body;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}
}
