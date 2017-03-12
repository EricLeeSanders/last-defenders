package com.foxholedefense.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import static com.badlogic.gdx.Input.Keys.T;

/**
 * Created by Eric on 3/11/2017.
 */

public class FHDVector2 extends Vector2 implements FHDPoolable {
    private Pool<FHDVector2> pool;

    @Override
    public void reset() {
        set(0,0);
    }

    @Override
    public void free(){
        pool.free(this);
    }

    public void setPool(Pool<FHDVector2> pool) {
        this.pool = pool;
    }
}
