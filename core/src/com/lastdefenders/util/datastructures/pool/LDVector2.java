package com.lastdefenders.util.datastructures.pool;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Eric on 3/11/2017.
 */

public class LDVector2 extends Vector2 implements LDPoolable {

    private Pool<LDVector2> pool;

    public LDVector2() {

    }

    public LDVector2(float x, float y) {

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

    public void setPool(Pool<LDVector2> pool) {

        this.pool = pool;
    }
}
