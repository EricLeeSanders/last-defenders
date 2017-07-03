package com.foxholedefense.action;

import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

/**
 * An Action class for enemies. Rotates the actor to the waypoint before moving.
 * Created by Eric on 3/13/2017.
 */

public class WaypointAction extends MoveToAction {

    private float rotation;

    public void setRotation(float rotation) {

        this.rotation = rotation;
    }

    @Override
    protected void begin() {

        super.begin();
        getTarget().setRotation(rotation);
    }
}

