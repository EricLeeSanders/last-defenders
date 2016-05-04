package com.eric.mtd.game.model.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
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
		this.setSize(textureSize.x, textureSize.y);
		this.setOrigin(textureSize.x / 2, textureSize.y / 2);
	}
	public Vector2 getRotatedCoords(Vector2 coords) {
		// Math stuff here -
		// http://math.stackexchange.com/questions/270194/how-to-find-the-vertices-angle-after-rotation
		double rotation = Math.toRadians(this.getRotation());
		float cosa = (float) Math.cos(rotation);
		float sina = (float) Math.sin(rotation);
		float newX = ((((coords.x - getPositionCenter().x) * cosa) - ((coords.y - getPositionCenter().y) * sina)) + getPositionCenter().x);
		float newY = ((((coords.x - getPositionCenter().x) * sina) + ((coords.y - getPositionCenter().y) * cosa)) + getPositionCenter().y);
		return new Vector2(newX, newY);
	}
	/**
	 * Calculates a rotation from the current position and the argument
	 * position. Calculates the shortest distance rotation.
	 * 
	 * @param vector
	 *            - Position to rotate to
	 * @return float - Rotation
	 */
	public float calculateRotation(Vector2 vector) {
		double prevAngle = this.getRotation();
		double angle = MathUtils.atan2(getPositionCenter().x - vector.x, vector.y - getPositionCenter().y);
		angle = Math.toDegrees(angle);
		double negAngle = (angle - 360) % 360;
		double posAngle = (angle + 360) % 360;
		double negDistance = Math.abs(prevAngle - negAngle);
		double posDistance = Math.abs(prevAngle - posAngle);
		if (negDistance < posDistance) {
			angle = negAngle;
		} else {
			angle = posAngle;
		}
		angle = Math.round(angle); // Round to help smooth movement
		return (float) angle;
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
