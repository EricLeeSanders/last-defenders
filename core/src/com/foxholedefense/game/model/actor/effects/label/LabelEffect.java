package com.foxholedefense.game.model.actor.effects.label;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Eric on 4/1/2017.
 */

public abstract class LabelEffect extends Label implements Pool.Poolable {
    protected float stateTime;
    private Pool<LabelEffect> pool;
    private float duration;

    public LabelEffect(Pool<LabelEffect> pool, float duration, Skin skin){
        super("", skin);
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
    public void reset(){
        stateTime = 0;
        remove();
        clear();
    }
}
