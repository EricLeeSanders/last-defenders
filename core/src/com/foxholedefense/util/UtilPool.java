package com.foxholedefense.util;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

/**
 * Created by Eric on 3/11/2017.
 */

public class UtilPool {

    public static FHDVector2 getVector2(float x, float y){
        Pool<FHDVector2> pool = Pools.get(FHDVector2.class);
        FHDVector2 vector2 = pool.obtain();
        vector2.setPool(pool);
        vector2.set(x,y);
        return vector2;
    }

    public static FHDVector2 getVector2(Vector2 vector){
        return getVector2(vector.x, vector.y);
    }

    public static FHDVector2 getVector2(){
        return getVector2(0,0);
    }

    public static void freeObjects(FHDPoolable... objects){
        for(FHDPoolable object : objects ){
            object.free();
        }
    }
}