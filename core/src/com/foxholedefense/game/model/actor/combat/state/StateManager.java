package com.foxholedefense.game.model.actor.combat.state;

/**
 * Created by Eric on 5/8/2017.
 */

public interface StateManager<E, V> extends StateTransitioner<E> {

    void update(float delta);

    V getState(E state);

    V getCurrentState();

    E getCurrentStateName();
}
