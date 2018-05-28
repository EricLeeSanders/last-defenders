package com.lastdefenders.util.action;

import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * This class is for one time actions (actions that are only called once).
 *
 * Created by Eric on 5/26/2018.
 */

public abstract class LDOneTimeAction extends Action {

    public abstract void action();

    @Override
    public boolean act(float delta) {
        action();
        return true;
    }
}
