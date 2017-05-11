package com.foxholedefense.game.model.actor.combat.state;

import java.util.Map;

/**
 * Created by Eric on 5/6/2017.
 */

public interface StateTransitioner<E> {
    void transition(E state);
    void transition(E state, Map<String, Object> parameters);
}
