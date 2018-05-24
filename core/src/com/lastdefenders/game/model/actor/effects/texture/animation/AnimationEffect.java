package com.lastdefenders.game.model.actor.effects.texture.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.game.model.actor.effects.texture.TextureEffect;
import com.lastdefenders.game.service.factory.EffectFactory.EffectPool;
import com.lastdefenders.util.datastructures.Dimension;

/**
 * Created by Eric on 4/2/2017.
 */

public abstract class AnimationEffect extends TextureEffect {

    private Animation<TextureRegion> animation;
    private float stateTime;

    protected AnimationEffect(EffectPool<? extends AnimationEffect> pool, Array<AtlasRegion> regions,
        Dimension textureSize, PlayMode playMode, float frameDuration) {

        super(pool);
        animation = new Animation<TextureRegion>(frameDuration, regions);
        animation.setPlayMode(playMode);
        setSize(textureSize);
    }

    @Override
    public void act(float delta) {
        stateTime += delta;
        if (animation.getPlayMode() != PlayMode.LOOP &&
            animation.isAnimationFinished(stateTime)) {
            free();
            return;
        }
        setTextureRegion(animation.getKeyFrame(stateTime, false));
        super.act(delta);
    }

    @Override
    public void reset() {
        super.reset();
        stateTime = 0;
    }
}
