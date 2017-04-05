package com.foxholedefense.game.model.actor.effects.texture.animation.death;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.effects.texture.TextureEffect;
import com.foxholedefense.game.model.actor.effects.texture.animation.AnimationEffect;
import com.foxholedefense.game.service.factory.EffectFactory.DeathEffectPool;
import com.foxholedefense.util.datastructures.Dimension;

public abstract class DeathEffect extends AnimationEffect {
	private static final float FRAME_DURATION = 0.05f;

	public DeathEffect(DeathEffectPool<? extends DeathEffect> pool, Array<AtlasRegion> regions, Dimension textureSize) {
		super(pool, regions, textureSize, PlayMode.NORMAL, FRAME_DURATION * regions.size, FRAME_DURATION);
	}

	public enum DeathEffectType {
		BLOOD, VEHCILE_EXPLOSION;
	}

}
