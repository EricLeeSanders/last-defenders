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
import com.foxholedefense.util.DebugOptions;
import com.foxholedefense.util.Dimension;
import com.foxholedefense.util.FHDVector2;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.UtilPool;

public class GameActor extends Actor{
	private TextureRegion textureRegion;
	private FHDVector2 positionCenter = UtilPool.getVector2();
	private ShapeRenderer bodyOutline = Resources.getShapeRenderer();
	public GameActor(TextureRegion textureRegion){
		this.setTextureRegion(textureRegion);
		this.setSize(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
		this.setOrigin(textureRegion.getRegionWidth() / 2,textureRegion.getRegionHeight() / 2);
	}

	@Override
	public void draw(Batch batch, float alpha) {

		TextureRegion textureRegion = getTextureRegion();
		if(textureRegion != null) {
			batch.draw(getTextureRegion(), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		}

		if(DebugOptions.showTextureBoundaries){
			batch.end();
			bodyOutline.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
			bodyOutline.begin(ShapeType.Line);
			bodyOutline.setColor(Color.BLUE);
			bodyOutline.rect(getX(),getY(), getWidth(), getHeight());
			bodyOutline.end();
			batch.begin();
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
