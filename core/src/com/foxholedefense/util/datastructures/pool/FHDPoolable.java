package com.foxholedefense.util.datastructures.pool;

import com.badlogic.gdx.utils.Pool;

import static com.badlogic.gdx.Input.Keys.T;

/**
 * Created by Eric on 3/11/2017.
 */

public interface FHDPoolable extends Pool.Poolable{
    void free();
}
