package com.foxholedefense.util;


import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.foxholedefense.action.FHDSequenceAction;
import com.foxholedefense.action.WaypointAction;
import com.foxholedefense.game.model.actor.GameActor;

/**
 * Created by Eric on 3/11/2017.
 */

public class UtilPool {

    public static FHDSequenceAction getSequenceAction(){
        Pool<FHDSequenceAction> pool = Pools.get(FHDSequenceAction.class);
        FHDSequenceAction sequenceAction = pool.obtain();
        return sequenceAction;
    }

    public static WaypointAction getWaypointAction(float x, float y, float duration, float rotation, Interpolation interpolation){
        Pool<WaypointAction> pool = Pools.get(WaypointAction.class);
        WaypointAction waypointAction = pool.obtain();
        waypointAction.setRotation(rotation);
        waypointAction.setPosition(x,y);
        waypointAction.setDuration(duration);
        waypointAction.setInterpolation(interpolation);
        waypointAction.setPool(pool);
        return waypointAction;
    }

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
