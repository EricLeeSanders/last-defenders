package com.eric.mtd.game.model.actor.deatheffect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.util.Dimension;

public abstract class DeathEffect extends GameActor implements Pool.Poolable {
	private Pool<DeathEffect> pool;
	private Animation animation;
	private float stateTime;
	private Dimension size;
	public DeathEffect(Pool<DeathEffect> pool, Array<AtlasRegion> regions, Dimension size){
		super(regions.get(0),size);
		this.pool = pool;
		this.size = size;
		animation = new Animation(0.15f, regions);
		animation.setPlayMode(PlayMode.NORMAL);
	}
	public Actor initialize(Vector2 pos){
		setPositionCenter(pos);
		return this;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		stateTime += delta;
	}

	/**
	 * Draws the explosion and frees it when it is done.
	 */
	@Override
	public void draw(Batch batch, float alpha) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		TextureRegion currentFrame = animation.getKeyFrame(stateTime, false);
		batch.draw(currentFrame, this.getX(), this.getY(), size.getWidth(), size.getHeight());
		if (animation.isAnimationFinished(stateTime)) {
			pool.free(this);
		}
	}
	
	@Override
	public void reset() {
		this.clear();
		this.remove();
		stateTime = 0;
	}
}
