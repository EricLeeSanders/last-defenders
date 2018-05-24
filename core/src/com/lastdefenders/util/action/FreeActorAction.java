package com.lastdefenders.util.action;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Eric on 5/21/2018.
 */

public class FreeActorAction extends Action {
    
    private boolean freed;
    private Pool<Actor> pool;

    public FreeActorAction setActorPool(Pool<Actor> pool){
        this.pool = pool;
        
        return this;
    }

    public boolean act (float delta) {
        if (!freed) {
            freed = true;
            pool.free(getActor());
        }
        return true;
    }

    public void restart () {
        freed = false;
    }
}
