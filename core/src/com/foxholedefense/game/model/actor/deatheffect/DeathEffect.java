package com.foxholedefense.game.model.actor.deatheffect;

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
import com.foxholedefense.game.model.actor.GameActor;
import com.foxholedefense.util.Dimension;

public abstract class DeathEffect extends GameActor implements Pool.Poolable {
	private Pool<DeathEffect> pool;
	private Animation animation;
	private float stateTime;
	private Dimension size;
	public DeathEffect(Pool<DeathEffect> pool, Array<AtlasRegion> regions, Dimension size){
		super(regions.get(0),size);
		this.pool = pool;
		this.size = size;
		animation = new Animation(0.05f, regions);
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
		if (animation.isAnimationFinished(stateTime)) {
			pool.free(this);
		}
		setTextureRegion(animation.getKeyFrame(stateTime, false));
	}

	@Override
	public void reset() {
		this.clear();
		this.remove();
		stateTime = 0;
	}
}
