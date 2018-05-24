package com.lastdefenders.game.model.actor.effects.texture;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.lastdefenders.game.model.actor.GameActor;
import com.lastdefenders.game.service.factory.EffectFactory.EffectPool;
import com.lastdefenders.util.datastructures.Dimension;

/**
 * Created by Eric on 5/21/2018.
 */

public abstract class TextureEffect extends GameActor implements Pool.Poolable  {

    private EffectPool<? extends Actor> pool;

    public TextureEffect(EffectPool<? extends Actor> pool){
        this.pool = pool;
    }

    public TextureEffect(EffectPool<? extends Actor> pool, Dimension textureSize){
        super(textureSize);
        this.pool = pool;
    }

    public void setPool(EffectPool<? extends Actor> pool){
        this.pool = pool;
    }

    public EffectPool<? extends Actor> getPool(){
        return pool;
    }

    public void initialize(Vector2 pos) {

        setPositionCenter(pos);
    }

    protected void free(){
        pool.free(this);
    }

    @Override
    public void reset() {
        clear();
        remove();
    }
}
