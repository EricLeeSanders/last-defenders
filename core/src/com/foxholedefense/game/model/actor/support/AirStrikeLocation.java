package com.foxholedefense.game.model.actor.support;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.FHDVector2;
import com.foxholedefense.util.Resources;

public class AirStrikeLocation extends Actor {
	private FHDVector2 location;
	private float radius;
	private boolean showRange = true;
	private TextureRegion rangeTexture;

	public AirStrikeLocation(FHDVector2 location, float radius, TextureRegion rangeTexture) {
		this.location = location;
		this.radius = radius;
		this.rangeTexture = rangeTexture;
	}

	@Override
	public void draw(Batch batch, float alpha) {
		if(isShowRange()){
			drawRange(batch);
		}
		super.draw(batch, alpha);
	}

	protected void drawRange(Batch batch){
		float width = radius * 2;
		float height = radius * 2;
		float x = ActorUtil.calcXBotLeftFromCenter(location.x, width);
		float y = ActorUtil.calcYBotLeftFromCenter(location.y, height);
		batch.draw(rangeTexture,x, y, getOriginX(), getOriginY(), width, height, 1, 1, 0);
	}
	
	public Shape2D getRangeShape() {
		return new Circle(location.x, location.y, radius);
	}
	
	public FHDVector2 getLocation() {
		return location;
	}

	public boolean isShowRange() {
		return showRange;
	}

	public void setShowRange(boolean showRange) {
		this.showRange = showRange;
	}

	@Override
	public void clear () {
		location.free();
		super.clear();
	}
}
