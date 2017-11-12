package com.lastdefenders.action;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Eric on 3/16/2017.
 */

public class LDSequenceAction extends ParallelAction {

    private int index;

    @Override
    public boolean act(float delta) {

        if (index >= getActions().size) {
            return true;
        }
        @SuppressWarnings("rawtypes")
        Pool pool = getPool();
        setPool(null); // Ensure this action can't be returned to the pool while executing.
        try {
            if (getActions().get(index).act(delta)) {
                if (actor == null) {
                    return true; // This action was removed.
                }
                index++;
                if (index >= getActions().size) {
                    return true;
                }
            }
            return false;
        } finally {
            setPool(pool);
        }
    }

    public Action getCurrentAction() {

        return getActions().get(index);
    }

    public int getIndex() {

        return index;
    }

    @Override
    public void restart() {

        super.restart();
        index = 0;
    }
}
