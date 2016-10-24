package com.eric.mtd.game.model.actor.support;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.game.model.actor.interfaces.IAttacker;
import com.eric.mtd.game.model.actor.projectile.Bullet;
import com.eric.mtd.game.service.factory.ActorFactory;
import com.eric.mtd.util.MTDAudio;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;
import com.eric.mtd.util.Dimension;

public class SupportActor extends GameActor implements Pool.Poolable, IAttacker{
	private Pool<SupportActor> pool;
	private float range, attack;
	private Vector2 gunPos;
	private boolean active;
	private int cost;
	private Group getTargetGroup;
	private boolean showRange;
	private Sprite rangeSprite;
	private Color rangeColor = new Color(1.0f, 1.0f, 1.0f, 0.5f);
	public SupportActor(Pool<SupportActor> pool, Group targetGroup, TextureRegion textureRegion, Dimension textureSize
						, float range, float attack, Vector2 gunPos, int cost) {
		super(textureRegion, textureSize);
		this.pool = pool;
		this.range = range;
		this.attack = attack;
		this.gunPos = gunPos;
		this.cost = cost;
		this.getTargetGroup = targetGroup;
		createRangeSprite();
	}
	protected void createRangeSprite(){
		Pixmap rangePixmap = new Pixmap(600, 600, Format.RGBA8888);
		rangePixmap.setColor(1.0f, 1.0f, 1.0f, 0.75f);
		rangePixmap.fillCircle(300, 300, 300);
		setRangeSprite(new Sprite(new Texture(rangePixmap)));
		rangePixmap.dispose();
	}
	@Override
	public void draw(Batch batch, float alpha) {
		if (showRange) {
			rangeSprite.setBounds(getPositionCenter().x - range, getPositionCenter().y - range, range*2, range*2);
			rangeSprite.draw(batch);

		}
		super.draw(batch, alpha);
	}

	public Group getTargetGroup(){
		return getTargetGroup;
	}
	
	public void freeActor() {
		pool.free(this);
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public int getCost(){
		return cost;
	}
	public void setShowRange(boolean bool) {
		showRange = bool;
	}

	public boolean isShowRange() {
		return showRange;
	}

	public void setRangeColor(float r, float g, float b, float a) {
		rangeColor.set(r, g, b, a);
	}

	public Color getRangeColor() {
		return rangeColor;
	}
	
	@Override
	public Shape2D getRangeShape() {
		return new Circle(getPositionCenter().x, getPositionCenter().y, range);
	}

	@Override
	public Vector2 getGunPos() {
		return getRotatedCoords((getPositionCenter().x + gunPos.x), (getPositionCenter().y + gunPos.y));
	}

	@Override
	public float getAttack() {
		return attack;
	}
	@Override
	public void reset() {
		this.setActive(false);
		this.setPosition(0, 0);
		this.setRotation(0);
		this.clear();
		this.remove();
	}
	public Sprite getRangeSprite() {
		return rangeSprite;
	}
	public void setRangeSprite(Sprite rangeSprite) {
		this.rangeSprite = rangeSprite;
	}
	
}
