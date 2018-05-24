package com.lastdefenders.game.model.actor.effects.texture.animation.death;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.lastdefenders.game.model.actor.effects.texture.animation.AnimationEffect;
import com.lastdefenders.game.service.factory.EffectFactory.EffectPool;
import com.lastdefenders.util.datastructures.Dimension;

public class VehicleExplosion extends AnimationEffect {

    private static final Dimension TEXTURE_SIZE = new Dimension(128, 128);
    private static final float FRAME_DURATION = 0.05f;

    public VehicleExplosion(EffectPool<VehicleExplosion> pool, Array<AtlasRegion> atlasRegions) {

        super(pool, atlasRegions, TEXTURE_SIZE, PlayMode.NORMAL, FRAME_DURATION );
    }
}
