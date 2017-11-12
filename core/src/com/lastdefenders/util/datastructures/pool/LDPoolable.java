package com.lastdefenders.util.datastructures.pool;

import com.badlogic.gdx.utils.Pool;

/**
 * Created by Eric on 3/11/2017.
 */

public interface LDPoolable extends Pool.Poolable {

    void free();
}
