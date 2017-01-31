package com.foxholedefense.game.model.actor.support;

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
import com.foxholedefense.game.model.actor.GameActor;
import com.foxholedefense.game.model.actor.interfaces.IAttacker;
import com.foxholedefense.game.model.actor.projectile.Bullet;
import com.foxholedefense.game.service.factory.ActorFactory;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.Dimension;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;

public class SupportActor extends GameActor implements Pool.Poolable, IAttacker{
	private Pool<SupportActor> pool;
	private float range, attack;
	private Vector2 gunPos;
	private boolean active;
	private int cost;
	private Group getTargetGroup;
	private boolean showRange;
	private TextureRegion rangeTexture;

	public SupportActor(Pool<SupportActor> pool, Group targetGroup, TextureRegion textureRegion, TextureRegion rangeTexture, Dimension textureSize
						, float range, float attack, Vector2 gunPos, int cost) {
		super(textureSize);
		this.pool = pool;
		this.range = range;
		this.attack = attack;
		this.gunPos = gunPos;
		this.cost = cost;
		this.getTargetGroup = targetGroup;
		this.rangeTexture = rangeTexture;
		setTextureRegion(textureRegion);
	}
	@Override
	public void draw(Batch batch, float alpha) {
		if (isShowRange()) {
			drawRange(batch);
		}
		super.draw(batch, alpha);
	}

	protected void drawRange(Batch batch){
		float width = range * 2;
		float height = range * 2;
		float x = ActorUtil.calcXBotLeftFromCenter(getPositionCenter().x, width);
		float y = ActorUtil.calcYBotLeftFromCenter(getPositionCenter().y, height);
		batch.draw(rangeTexture,x, y, getOriginX(), getOriginY(), width, height, 1, 1, 0);
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

}
