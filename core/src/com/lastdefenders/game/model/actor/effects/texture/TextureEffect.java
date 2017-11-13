package com.lastdefenders.game.model.actor.effects.texture;

import com.badlogic.gdx.utils.Pool;
import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.util.datastructures.Dimension;

/**
 * Created by Eric on 4/3/2017.
 */

public abstract class TextureEffect extends GameActor implements Pool.Poolable {

    protected float stateTime;
    private float duration;
    private Pool<TextureEffect> pool;

    protected TextureEffect(Pool<TextureEffect> pool, Dimension textureSize, float duration) {

        this(pool, duration);
        setSize(textureSize);

    }

    private TextureEffect(Pool<TextureEffect> pool, float duration) {

        this.pool = pool;
        this.duration = duration;
    }

    @Override
    public void act(float delta) {

        super.act(delta);
        stateTime += delta;
        if (stateTime >= duration) {
            free();
        }
    }

    /**
     * This method should not be called more than once.
     * Otherwise, the object is placed in the pool twice.
     */
    protected void free() {

        pool.free(this);
    }


    @Override
    public void reset() {

        clear();
        remove();
        stateTime = 0;
    }
}
