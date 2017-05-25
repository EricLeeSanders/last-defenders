package com.foxholedefense.game.model.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.foxholedefense.util.DebugOptions;
import com.foxholedefense.util.datastructures.Dimension;
import com.foxholedefense.util.datastructures.pool.FHDVector2;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.datastructures.pool.UtilPool;

public class GameActor extends Actor{
	private TextureRegion textureRegion;
	private FHDVector2 positionCenter = UtilPool.getVector2();

	public GameActor(){

	}

	public GameActor(Dimension textureSize){
		this.setSize(textureSize.getWidth(),textureSize.getHeight());
		this.setOrigin(textureSize.getWidth() / 2, textureSize.getHeight() / 2);
	}

	@Override
	public void draw(Batch batch, float alpha) {

		TextureRegion textureRegion = getTextureRegion();
		if(textureRegion != null) {
			batch.draw(getTextureRegion(), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		}

		if(DebugOptions.showTextureBoundaries){
			drawDebugBody(batch);
		}
	}

	private void drawDebugBody(Batch batch){
		batch.end();
		ShapeRenderer bodyOutline = Resources.getShapeRenderer();
		bodyOutline.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
		bodyOutline.begin(ShapeType.Line);
		bodyOutline.setColor(Color.BLUE);
		bodyOutline.rect(getX(),getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		bodyOutline.end();
		batch.begin();
	}

	public Vector2 getPositionCenter() {
		positionCenter.set(getX() + getOriginX(), getY() + getOriginY());
		return positionCenter;
	}

	public void setPositionCenter(Vector2 pos) {
		setPositionCenter(pos.x,pos.y);
	}

	public void setPositionCenter(float x, float y){
		setPosition(x, y, Align.center);
	}

	public TextureRegion getTextureRegion() {
		return textureRegion;
	}

	public void setTextureRegion(TextureRegion textureRegion) {
		this.textureRegion = textureRegion;
	}

	public void setSize(Dimension textureSize){
		this.setSize(textureSize.getWidth(),textureSize.getHeight());
		this.setOrigin(textureSize.getWidth() / 2, textureSize.getHeight() / 2);
	}
}
