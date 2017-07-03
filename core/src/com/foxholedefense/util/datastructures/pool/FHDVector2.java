package com.foxholedefense.util.datastructures.pool;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Eric on 3/11/2017.
 */

public class FHDVector2 extends Vector2 implements FHDPoolable {
    private Pool<FHDVector2> pool;

    public FHDVector2() {
    }

    public FHDVector2(float x, float y) {
        super(x, y);
    }

    @Override
    public void reset() {
        set(0, 0);
    }

    @Override
    public void free() {
        pool.free(this);
    }

    public void setPool(Pool<FHDVector2> pool) {
        this.pool = pool;
    }
}
