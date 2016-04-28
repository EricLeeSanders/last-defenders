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
	private static final int COST = 1000;
	private static final float SCALE = 0.5f;
	private TextureRegion currentTexture;
	private TextureRegion [] textureRegions;
	private ShapeRenderer debugBody = Resources.getShapeRenderer();
	private boolean active;
	private Pool<Apache> pool;
	private float textureCounter; // Used to animate textures
	private int textureIndex; // Current texture index
	public Apache(Pool<Apache> pool, TextureRegion [] textureRegions) {
		this.pool = pool;
		this.textureRegions = textureRegions;
		this.setOrigin((textureRegions[0].getRegionWidth() / 2), 0);
		this.setSize(this.textureRegions[0].getRegionWidth()*SCALE, this.textureRegions[0].getRegionHeight()*SCALE);
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
		changeTextures(delta);
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
		debugBody.rect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		debugBody.end();
		batch.begin();
		batch.draw(currentTexture, this.getX(), this.getY(), this.getOriginX(), this.getOriginY(), this.getWidth(), this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation());
	}

	/**
	 * Handles the changing of textures
	 * 
	 * @param delta
	 */
	public void changeTextures(float delta) {
		if (textureCounter >= 0.2f) {
			textureCounter = 0;
			textureIndex++;
			currentTexture = textureRegions[textureIndex % 3];
		} else {
			textureCounter += delta;
		}
	}
	
	public void setPositionCenter(Vector2 pos) {
		this.setPosition(pos.x - (this.getWidth() / 2), pos.y - (this.getHeight() / 2));
	}

	public Vector2 getPositionCenter() {
		return new Vector2(getX() + (textureRegions[0].getRegionWidth() / 2), getY() + (textureRegions[0].getRegionHeight() / 2));
	}


	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public static int getCost() {
		return COST;
	}
}
