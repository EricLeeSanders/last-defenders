package com.foxholedefense.game.model.actor;

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
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.Dimension;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

public class GameActor extends Actor{
	private TextureRegion textureRegion;
	private Vector2 positionCenter = new Vector2();
	private ShapeRenderer bodyOutline = Resources.getShapeRenderer();
	public GameActor(TextureRegion textureRegion){
		this.setTextureRegion(textureRegion);
		this.setSize(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
		this.setOrigin(textureRegion.getRegionWidth() / 2,textureRegion.getRegionHeight() / 2);
	}

	public Vector2 getRotatedCoords(float x, float y) {
		// Math stuff here -
		// http://math.stackexchange.com/questions/270194/how-to-find-the-vertices-angle-after-rotation
		double rotation = Math.toRadians(this.getRotation());
		float cos = (float) Math.cos(rotation);
		float sin = (float) Math.sin(rotation);
		float newX = ((((x - getPositionCenter().x) * cos) - ((y - getPositionCenter().y) * sin)) + getPositionCenter().x);
		float newY = ((((x - getPositionCenter().x) * sin) + ((y - getPositionCenter().y) * cos)) + getPositionCenter().y);
		return new Vector2(newX, newY);
	}
	public Vector2 getRotatedCoords(Vector2 coords) {
		return getRotatedCoords(coords.x, coords.y);
	}
	/**
	 * Calculates a rotation from the current position and the target
	 * position.
	 *
	 * @param x
	 * @param y
	 * @return float - Rotation
	 */
	public float calculateRotation(float x, float y) {
		return calculateRotation(new Vector2(x,y));
	}
	/**
	 * Calculates a rotation from the current position and the target
	 * position.
	 *
	 * @param target
	 *            - Position to rotate to
	 * @return float - Rotation
	 */
	public float calculateRotation(Vector2 target) {
		Vector2 targetCopy = new Vector2(target);
		return targetCopy.sub(getPositionCenter()).angle();
	}

	@Override
	public void draw(Batch batch, float alpha) {
		if(Logger.DEBUG == true){
			batch.end();
			bodyOutline.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
			bodyOutline.begin(ShapeType.Line);
			bodyOutline.setColor(Color.BLUE);
			bodyOutline.rect(getX(),getY(), getWidth(), getHeight());
			bodyOutline.end();
			batch.begin();
		}
		TextureRegion textureRegion = getTextureRegion();
		if(textureRegion != null) {
			batch.draw(getTextureRegion(), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		}
	}
	
	public Vector2 getPositionCenter() {
		positionCenter.set(getX() + getOriginX(), getY() + getOriginY());
		return positionCenter;
	}
	
	public void setPositionCenter(Vector2 pos) {
		setPositionCenter(pos.x,pos.y);
	}
	
	public void setPositionCenter(float x, float y){
		setPosition(x - getOriginX(), y - getOriginY());
	}

	public TextureRegion getTextureRegion() {
		return textureRegion;
	}

	public void setTextureRegion(TextureRegion textureRegion) {
		this.textureRegion = textureRegion;
	}
}
