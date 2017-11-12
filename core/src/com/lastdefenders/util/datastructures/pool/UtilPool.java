package com.lastdefenders.util.datastructures.pool;


import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.lastdefenders.action.LDSequenceAction;
import com.lastdefenders.action.WaypointAction;

/**
 * Created by Eric on 3/11/2017.
 */

public class UtilPool {

    public static LDSequenceAction getSequenceAction() {

        Pool<LDSequenceAction> pool = Pools.get(LDSequenceAction.class);
        return pool.obtain();
    }

    public static WaypointAction getWaypointAction(float x, float y, float duration, float rotation,
        Interpolation interpolation) {

        Pool<WaypointAction> pool = Pools.get(WaypointAction.class);
        WaypointAction waypointAction = pool.obtain();
        waypointAction.setRotation(rotation);
        waypointAction.setPosition(x, y, Align.center);
        waypointAction.setDuration(duration);
        waypointAction.setInterpolation(interpolation);
        waypointAction.setPool(pool);
        return waypointAction;
    }

    public static LDVector2 getVector2(float x, float y) {

        Pool<LDVector2> pool = Pools.get(LDVector2.class);
        LDVector2 vector2 = pool.obtain();
        vector2.setPool(pool);
        vector2.set(x, y);
        return vector2;
    }

    public static LDVector2 getVector2(Vector2 vector) {

        return getVector2(vector.x, vector.y);
    }

    public static LDVector2 getVector2() {

        return getVector2(0, 0);
    }

    public static void freeObjects(LDPoolable... objects) {

        for (LDPoolable object : objects) {
            object.free();
        }
    }
}
