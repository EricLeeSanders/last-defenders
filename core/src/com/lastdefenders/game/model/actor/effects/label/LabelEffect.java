package com.lastdefenders.game.model.actor.effects.label;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Pool;
import com.lastdefenders.game.service.factory.EffectFactory.EffectPool;

/**
 * Created by Eric on 4/1/2017.
 */

public abstract class LabelEffect extends Label implements Pool.Poolable {

    float stateTime;
    private EffectPool<? extends Actor> pool;

    LabelEffect(EffectPool<? extends Actor> pool, Skin skin) {

        super("", skin);
        this.pool = pool;
    }

    @Override
    public void act(float delta) {

        super.act(delta);
        stateTime += delta;
    }

    /**
     * This method should not be called more than once.
     * Otherwise, the object is placed in the pool twice.
     */
    void free() {

        pool.free(this);
    }

    public EffectPool<? extends Actor> getPool(){
        return pool;
    }

    @Override
    public void reset() {

        stateTime = 0;
        remove();
        clear();
    }
}
