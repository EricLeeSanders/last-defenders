package com.foxholedefense.game.model.actor.deatheffect;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.util.Dimension;

public class VehicleExplosion extends DeathEffect{
	public VehicleExplosion(Pool<DeathEffect> pool, Array<AtlasRegion> atlasRegions){
		super(pool, atlasRegions, new Dimension(128,128));
	}
}