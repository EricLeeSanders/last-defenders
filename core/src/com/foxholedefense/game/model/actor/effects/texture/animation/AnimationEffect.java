package com.foxholedefense.game.model.actor.effects.texture.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.effects.texture.TextureEffect;
import com.foxholedefense.util.datastructures.Dimension;

/**
 * Created by Eric on 4/2/2017.
 */

public class AnimationEffect extends TextureEffect {
    private Animation animation;

    public AnimationEffect(Pool<TextureEffect> pool, Array<AtlasRegion> regions, Dimension textureSize, PlayMode playMode, float duration, float frameDuration) {
        super(pool, textureSize, duration);
        animation = new Animation(frameDuration, regions);
        animation.setPlayMode(playMode);
    }

    public void initialize(Vector2 pos){
        setPositionCenter(pos);
    }

    @Override
    public void act(float delta) {
        if (animation.getPlayMode() != PlayMode.LOOP &&
                animation.isAnimationFinished(stateTime)) {
            free();
            return;
        }
        super.act(delta);
        setTextureRegion(animation.getKeyFrame(stateTime, false));
    }

}
