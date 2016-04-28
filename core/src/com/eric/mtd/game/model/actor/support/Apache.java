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

public class Apache extends SupportActor{
	private static final int COST = 2000;
	private static final float SCALE = 0.5f;
	private TextureRegion currentTexture;
	private TextureRegion [] textureRegions;
	private ShapeRenderer debugBody = Resources.getShapeRenderer();
	private boolean active;
	private float textureCounter; // Used to animate textures
	private int textureIndex; // Current texture index
	public Apache(Pool<SupportActor> pool, TextureRegion [] textureRegions) {
		super(pool, textureRegions[0], new Vector2(textureRegions[0].getRegionWidth()*SCALE, textureRegions[0].getRegionHeight()*SCALE),
				COST);
		this.textureRegions = textureRegions;
		this.setOrigin((textureRegions[0].getRegionWidth() / 2), 0);
		this.setSize(this.textureRegions[0].getRegionWidth()*SCALE, this.textureRegions[0].getRegionHeight()*SCALE);
	}

	@Override
	public void reset() {
		if (Logger.DEBUG)
			System.out.println("freeing apache");
		super.reset();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		changeTextures(delta);
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
			super.setTextureRegion(textureRegions[textureIndex % 3]);
		} else {
			textureCounter += delta;
		}
	}


}
