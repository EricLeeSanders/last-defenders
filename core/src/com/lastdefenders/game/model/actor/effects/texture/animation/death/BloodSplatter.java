package com.lastdefenders.game.model.actor.effects.texture.animation.death;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.lastdefenders.game.service.factory.EffectFactory.DeathEffectPool;
import com.lastdefenders.util.datastructures.Dimension;

public class BloodSplatter extends DeathEffect {

    private static final Dimension TEXTURE_SIZE = new Dimension(48, 48);

    public BloodSplatter(DeathEffectPool<BloodSplatter> pool, Array<AtlasRegion> atlasRegions) {

        super(pool, atlasRegions, TEXTURE_SIZE);
    }
}
