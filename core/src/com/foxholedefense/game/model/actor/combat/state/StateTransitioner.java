package com.foxholedefense.game.model.actor.combat.state;

import java.util.Map;

/**
 * Created by Eric on 5/6/2017.
 */

public interface StateTransitioner<T> {
    void transition(T state);
    void transition(T state, Map<String, Object> parameters);
}
