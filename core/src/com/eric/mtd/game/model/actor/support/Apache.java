package com.eric.mtd.game.model.actor.support;

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

public class Apache extends Actor implements Pool.Poolable {
	private TextureRegion textureRegion;
	private ShapeRenderer debugBody = Resources.getShapeRenderer();
	private float health;
	private boolean active;
	private Pool<Apache> pool;

	public Apache(Pool<Apache> pool) {
		this.pool = pool;
		TextureAtlas supportAtlas = Resources.getAtlas(Resources.SUPPORT_ATLAS);
		textureRegion = supportAtlas.findRegion("apache1");
		this.setOrigin((textureRegion.getRegionWidth() / 2), 0);
	}

	@Override
	public void reset() {
		if (Logger.DEBUG)
			System.out.println("freeing apache");
		this.setActive(false);
		this.setRotation(0);
		this.clear();
		this.remove();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (isActive()) {
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
		batch.end();
		debugBody.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
		debugBody.begin(ShapeType.Line);
		debugBody.setColor(Color.RED);
		debugBody.rect(this.getX(), this.getY(), textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
		debugBody.end();
		batch.begin();
		batch.draw(textureRegion, this.getX(), this.getY(), this.getOriginX(), this.getOriginY(), textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), 1, 1, this.getRotation());
	}


	public void setPositionCenter(Vector2 pos) {
		this.setPosition(pos.x - (textureRegion.getRegionWidth() / 2), pos.y - (textureRegion.getRegionHeight() / 2));
	}

	public Vector2 getPositionCenter() {
		return new Vector2(getX() + (textureRegion.getRegionWidth() / 2), getY() + (textureRegion.getRegionHeight() / 2));
	}


	public boolean isActive() {
		return active;
	}

	public void setActive(boolean dead) {
		this.active = active;
	}
}
