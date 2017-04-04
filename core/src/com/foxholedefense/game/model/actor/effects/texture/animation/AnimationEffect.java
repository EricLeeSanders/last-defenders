package com.foxholedefense.game.model.actor.effects.texture.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.util.datastructures.Dimension;

/**
 * Created by Eric on 4/2/2017.
 */

public class AnimationEffect extends com.foxholedefense.game.model.actor.effects.texture.TextureEffect {
    private Animation animation;

    public AnimationEffect(Pool<com.foxholedefense.game.model.actor.effects.texture.TextureEffect> pool, Array<AtlasRegion> regions, Dimension textureSize, float frameDuration) {
        super(pool,textureSize,frameDuration * regions.size);
        animation = new Animation(frameDuration, regions);
        animation.setPlayMode(PlayMode.NORMAL);
    }

    public void initialize(Vector2 pos){
        setPositionCenter(pos);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (animation.isAnimationFinished(stateTime)) {
            free();
        }
        setTextureRegion(animation.getKeyFrame(stateTime, false));
    }

}
