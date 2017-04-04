package com.foxholedefense.game.model.actor.effects.texture;

import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.GameActor;
import com.foxholedefense.util.datastructures.Dimension;

/**
 * Created by Eric on 4/3/2017.
 */

public class TextureEffect extends GameActor implements Pool.Poolable {
    protected float stateTime;
    private float duration;
    private Pool<TextureEffect> pool;

    public TextureEffect(Pool<TextureEffect> pool, Dimension textureSize, float duration){
        this(pool, duration);
        setSize(textureSize);

    }

    public TextureEffect(Pool<TextureEffect> pool, float duration){
        this.pool = pool;
        this.duration = duration;
    }

    @Override
    public void act(float delta){
        super.act(delta);
        stateTime += delta;
        if (stateTime >= duration){
            pool.free(this);
        }
    }

    protected void free(){
        pool.free(this);
    }


    @Override
    public void reset() {
        clear();
        remove();
        stateTime = 0;
    }
}
