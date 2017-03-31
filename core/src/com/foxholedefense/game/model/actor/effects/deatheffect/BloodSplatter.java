package com.foxholedefense.game.model.actor.effects.deatheffect;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.util.datastructures.Dimension;

public class BloodSplatter extends DeathEffect {
	private static final Dimension TEXTURE_SIZE = new Dimension(48,48);
	public BloodSplatter(Pool<DeathEffect> pool, Array<AtlasRegion> atlasRegions) {
		super(pool, atlasRegions, TEXTURE_SIZE);
		setRotation(90);
	}

}
