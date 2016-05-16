package com.eric.mtd.game.model.actor.support;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
	private Sprite locationSprite;
	private Color rangeColor = new Color(0f, 0f, 0f, 0.5f);
	private float radius;
	private boolean showRange = true;
	public AirStrikeLocation(Vector2 location, float radius, Group group){
		group.addActor(this);
		this.location = location;
		this.radius = radius;
		createLocationSprite();
	}
	private void createLocationSprite(){
		Pixmap locationPixmap = new Pixmap(600, 600, Format.RGBA8888);
		locationPixmap.setColor(0,0,0,1f);
		locationPixmap.fillCircle(300,300,300);
		locationSprite = (new Sprite(new Texture(locationPixmap)));
		locationPixmap.dispose();
		locationSprite.setSize(radius, radius);
	}
	@Override
	public void draw(Batch batch, float alpha) {
		locationSprite.setPosition(getX() - (locationSprite.getWidth()/2), getY() - (locationSprite.getHeight()/2));
		locationSprite.draw(batch);
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

	public Sprite getLocationSprite() {
		return locationSprite;
	}

	public void setLocationSprite(Sprite locShapeRenderer) {
		this.locationSprite = locShapeRenderer;
	}

	public boolean isShowRange() {
		return showRange;
	}

	public void setShowRange(boolean showRange) {
		this.showRange = showRange;
	}
}
