package com.eric.mtd.game.model.actor.support;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.eric.mtd.game.model.actor.GameActor;
import com.eric.mtd.util.Logger;

public class SupportActor extends GameActor implements Pool.Poolable{
	private Pool<SupportActor> pool;
	private boolean active;
	private int cost;
	public SupportActor(Pool<SupportActor> pool, TextureRegion textureRegion, Vector2 textureSize
						, int cost) {
		super(textureRegion, textureSize);
		this.pool = pool;
		this.cost = cost;
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

	@Override
	public void reset() {
		this.setActive(false);
		this.setRotation(0);
		this.clear();
		this.remove();
	}	
}
