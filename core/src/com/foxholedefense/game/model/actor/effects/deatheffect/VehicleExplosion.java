package com.foxholedefense.game.model.actor.effects.deatheffect;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.util.Dimension;

public class VehicleExplosion extends DeathEffect{
	public VehicleExplosion(Pool<DeathEffect> pool, Array<AtlasRegion> atlasRegions){
		super(pool, atlasRegions);
		setRotation(90);
	}
}