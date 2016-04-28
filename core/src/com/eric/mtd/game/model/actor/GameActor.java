package com.eric.mtd.game.model.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.eric.mtd.util.Resources;
import com.badlogic.gdx.utils.Pool;

public class GameActor extends Actor{
	private TextureRegion textureRegion;
	private Vector2 textureSize;
	private Vector2 positionCenter = new Vector2();
	public GameActor(TextureRegion textureRegion, Vector2 textureSize){
		this.setTextureRegion(textureRegion);
		this.setTextureSize(textureSize);
		this.setOrigin(textureSize.x / 2, textureSize.y / 2);
	}
	
	@Override
	public void draw(Batch batch, float alpha) {
		batch.end();
		batch.begin();
		batch.draw(getTextureRegion(), getX(), getY(), getOriginX(), getOriginY(), getTextureSize().x, getTextureSize().y, 1, 1, getRotation());
	}
	
	public Vector2 getPositionCenter() {
		positionCenter.set(getX() + (getTextureSize().x / 2), getY() + (getTextureSize().y / 2));
		return positionCenter;
	}
	
	public void setPositionCenter(Vector2 pos) {
		this.setPosition(pos.x - (getTextureSize().x / 2), pos.y - (getTextureSize().y / 2));
	}

	public Vector2 getTextureSize() {
		return textureSize;
	}

	public void setTextureSize(Vector2 textureSize) {
		this.textureSize = textureSize;
	}

	public TextureRegion getTextureRegion() {
		return textureRegion;
	}

	public void setTextureRegion(TextureRegion textureRegion) {
		this.textureRegion = textureRegion;
	}
}
