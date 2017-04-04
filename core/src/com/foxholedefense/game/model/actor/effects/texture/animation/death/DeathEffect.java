package com.foxholedefense.game.model.actor.effects.texture.animation.death;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.util.datastructures.Dimension;

public abstract class DeathEffect extends com.foxholedefense.game.model.actor.effects.texture.animation.AnimationEffect {
	private static final float FRAME_DURATION = 0.05f;

	public DeathEffect(Pool<com.foxholedefense.game.model.actor.effects.texture.TextureEffect> pool, Array<AtlasRegion> regions, Dimension textureSize) {
		super(pool, regions, textureSize, FRAME_DURATION);
	}

	public enum DeathEffectType {
		BLOOD, VEHCILE_EXPLOSION;
	}

}
