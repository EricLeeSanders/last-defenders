package com.eric.mtd.game.model.actor.support;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.eric.mtd.util.Resources;

public class AirStrikeLocation extends Actor {
	private Vector2 location;
	private ShapeRenderer locShapeRenderer = Resources.getShapeRenderer();
	private Color rangeColor = new Color(0f, 0f, 0f, 0.5f);
	private float radius;
	private boolean showRange = true;
	public AirStrikeLocation(Vector2 location, float radius, Group group){
		group.addActor(this);
		this.location = location;
		this.radius = radius;
	}
	
	@Override
	public void draw(Batch batch, float alpha) {
		batch.end();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		if(showRange){
			locShapeRenderer.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
			locShapeRenderer.begin(ShapeType.Filled);
			locShapeRenderer.setColor(rangeColor);
			locShapeRenderer.circle(((Circle) getRangeShape()).x, ((Circle) getRangeShape()).y, ((Circle) getRangeShape()).radius);
			locShapeRenderer.end();
		}
		batch.begin();
		super.draw(batch, alpha);
	}
	
	public Shape2D getRangeShape() {
		return new Circle(location.x, location.y, radius);
	}
	
	public Vector2 getLocation() {
		return location;
	}

	public void setLocation(Vector2 location) {
		this.location = location;
	}

	public ShapeRenderer getLocShapeRenderer() {
		return locShapeRenderer;
	}

	public void setLocShapeRenderer(ShapeRenderer locShapeRenderer) {
		this.locShapeRenderer = locShapeRenderer;
	}

	public boolean isShowRange() {
		return showRange;
	}

	public void setShowRange(boolean showRange) {
		this.showRange = showRange;
	}
}
