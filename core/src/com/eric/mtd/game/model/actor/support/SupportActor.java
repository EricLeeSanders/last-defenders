package com.eric.mtd.game.model.actor.support;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
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
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.util.AudioUtil;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;
import com.eric.mtd.util.AudioUtil.ProjectileSound;
import com.eric.mtd.util.Dimension;

public class SupportActor extends GameActor implements Pool.Poolable, IAttacker{
	private Pool<SupportActor> pool;
	private float range, attack;
	private Vector2 gunPos;
	private boolean active;
	private int cost;
	private Group enemyTargetGroup;
	private boolean showRange;
	private ShapeRenderer rangeShape = Resources.getShapeRenderer();
	private Color rangeColor = new Color(1.0f, 1.0f, 1.0f, 0.5f);
	public SupportActor(Pool<SupportActor> pool, TextureRegion textureRegion, Dimension textureSize
						, float range, float attack, Vector2 gunPos, int cost) {
		super(textureRegion, textureSize);
		this.pool = pool;
		this.range = range;
		this.attack = attack;
		this.gunPos = gunPos;
		this.cost = cost;
	}
	@Override
	public void draw(Batch batch, float alpha) {
		batch.end();
		if (showRange) {
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glEnable(GL20.GL_BLEND);

			rangeShape.setProjectionMatrix(this.getParent().getStage().getCamera().combined);
			rangeShape.begin(ShapeType.Filled);
			rangeShape.setColor(rangeColor);
			rangeShape.circle(((Circle) getRangeShape()).x, ((Circle) getRangeShape()).y, ((Circle) getRangeShape()).radius);
			rangeShape.end();

		}
		batch.begin();
		super.draw(batch, alpha);
	}
	/**
	 * Sets the Enemy Group
	 */
	public void setEnemyGroup(Group enemyGroup) {
		this.enemyTargetGroup = enemyGroup;
	}
	public Group getEnemyGroup(){
		return enemyTargetGroup;
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
		Vector2 pos = new Vector2((getPositionCenter().x + gunPos.x), (getPositionCenter().y + gunPos.y));
		return getRotatedCoords(pos);
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
	
}
