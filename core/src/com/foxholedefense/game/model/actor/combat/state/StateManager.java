package com.foxholedefense.game.model.actor.combat.state;

/**
 * Created by Eric on 5/8/2017.
 */

public interface StateManager<K, V> extends StateTransitioner<K>  {
    void update(float delta);
    V getState(K state);
    V getCurrentState();
    K getCurrentStateName();
}
