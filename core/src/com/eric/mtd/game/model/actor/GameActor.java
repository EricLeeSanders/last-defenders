package com.eric.mtd.game.model.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.eric.mtd.util.Dimension;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;
import com.badlogic.gdx.utils.Pool;

public class GameActor extends Actor{
	private TextureRegion textureRegion;
	private Dimension textureSize;
	private Vector2 positionCenter = new Vector2();
	private ShapeRenderer bodyOutline = Resources.getShapeRenderer();
	public GameActor(TextureRegion textureRegion, Dimension textureSize){
		this.setTextureRegion(textureRegion);
		this.setTextureSize(textureSize);
		this.setSize(textureSize.getWidth(), textureSize.getHeight());
		this.setOrigin(textureSize.getWidth() / 2, textureSize.getHeight() / 2);
	}
	public Vector2 getRotatedCoords(float x, float y) {
		// Math stuff here -
		// http://math.stackexchange.com/questions/270194/how-to-find-the-vertices-angle-after-rotation
		double rotation = Math.toRadians(this.getRotation());
		float cosa = (float) Math.cos(rotation);
		float sina = (float) Math.sin(rotation);
		float newX = ((((x - getPositionCenter().x) * cosa) - ((y - getPositionCenter().y) * sina)) + getPositionCenter().x);
		float newY = ((((x - getPositionCenter().x) * sina) + ((y - getPositionCenter().y) * cosa)) + getPositionCenter().y);
		return new Vector2(newX, newY);
	}
	public Vector2 getRotatedCoords(Vector2 coords) {
		return getRotatedCoords(coords.x, coords.y);
	}
	/**
	 * Calculates a rotation from the current position and the argument
	 * position. Calculates the shortest distance rotation.
	 * 
	 * @param x 
	 * @param y
	 * @return float - Rotation
	 */
	public float calculateRotation(float x, float y) {
		double prevAngle = this.getRotation();
		double angle = MathUtils.atan2(getPositionCenter().x - x, y - getPositionCenter().y);
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
	/**
	 * Calculates a rotation from the current position and the argument
	 * position. Calculates the shortest distance rotation.
	 * 
	 * @param vector
	 *            - Position to rotate to
	 * @return float - Rotation
	 */
	public float calculateRotation(Vector2 vector) {
		return calculateRotation(vector.x, vector.y);
	}
	@Override
	public void draw(Batch batch, float alpha) {
		if(Logger.DEBUG == true){
			batch.end();
			bodyOutline.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
			bodyOutline.begin(ShapeType.Line);
			bodyOutline.setColor(Color.YELLOW);
			bodyOutline.rect(getX(),getY(), textureSize.getWidth(), textureSize.getHeight());
			bodyOutline.end();
			batch.begin();
		}
		batch.draw(getTextureRegion(), getX(), getY(), getOriginX(), getOriginY(), getTextureSize().getWidth(), getTextureSize().getHeight(), 1, 1, getRotation());
	}
	
	public Vector2 getPositionCenter() {
		positionCenter.set(getX() + (getTextureSize().getWidth() / 2), getY() + (getTextureSize().getHeight() / 2));
		return positionCenter;
	}
	
	public void setPositionCenter(Vector2 pos) {
		setPositionCenter(pos.x,pos.y);
	}
	
	public void setPositionCenter(float x, float y){
		this.setPosition(x - (getTextureSize().getWidth() / 2), y - (getTextureSize().getHeight() / 2));
	}

	public Dimension getTextureSize() {
		return textureSize;
	}

	public void setTextureSize(Dimension textureSize) {
		this.textureSize = textureSize;
	}

	public TextureRegion getTextureRegion() {
		return textureRegion;
	}

	public void setTextureRegion(TextureRegion textureRegion) {
		this.textureRegion = textureRegion;
	}
}
