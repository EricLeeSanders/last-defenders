package com.foxholedefense.game.model.actor.effects.texture.animation.death;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.foxholedefense.game.service.factory.EffectFactory.DeathEffectPool;
import com.foxholedefense.util.datastructures.Dimension;

public class VehicleExplosion extends DeathEffect {

    private static final Dimension TEXTURE_SIZE = new Dimension(128, 128);

    public VehicleExplosion(DeathEffectPool<VehicleExplosion> pool,
        Array<AtlasRegion> atlasRegions) {

        super(pool, atlasRegions, TEXTURE_SIZE);
    }
}
