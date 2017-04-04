package com.foxholedefense.game.model.actor.effects.texture.animation;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.util.datastructures.Dimension;

/**
 * Created by Eric on 4/2/2017.
 */

public class EnemyCoinEffect extends AnimationEffect {
    private static final Dimension TEXTURE_SIZE = new Dimension(12,12);
    private static final float FRAME_DURATION = 2f;

    public EnemyCoinEffect(Pool<com.foxholedefense.game.model.actor.effects.texture.TextureEffect> pool, Array<AtlasRegion> regions) {
        super(pool, regions, TEXTURE_SIZE, FRAME_DURATION);
    }

    public void initialize(Vector2 pos){
        this.setPositionCenter(pos);
    }
}
