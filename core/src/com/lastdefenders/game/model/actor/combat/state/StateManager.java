package com.lastdefenders.game.model.actor.combat.state;

import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.util.Logger;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric on 5/8/2017.
 */

public abstract class StateManager<E extends Enum<E>, T extends CombatActor> implements StateTransitioner<E>{

    private T combatActor;
    private E currentState;
    private Map<String, Object> emptyParams = new HashMap<>(); // Used for when no params are passed.
    private Boolean lockedStateSwap = false;

    public StateManager(T combatActor){
        this.combatActor = combatActor;
    }

    public void update(float delta) {

        getCurrentState().update(delta);
    }


    public CombatActorState getState(E state) {

        return getStates().get(state);
    }


    public CombatActorState getCurrentState() {

        return getStates().get(currentState);
    }

    public E getCurrentStateName() {

        return currentState;
    }

    @Override
    public void transition(E state) {
        transition(state, emptyParams);
    }

    @Override
    public void transition(E state, Map<String, Object> parameters) {

        getState(state).loadParameters(parameters);

        if (currentState == null) {
            setCurrentState(state);
        } else {
            swapState(currentState, state);
        }

    }

    private void swapState(E oldState, E newState) {

        Logger.info("State Manager: Swapping states (" + combatActor.ID +"): " + oldState.name() + " to: " + newState.name());
        if(lockedStateSwap){
            throw new IllegalStateException("Enemy State Swap Locked");
        }

        lockedStateSwap = true;
        getState(oldState).postState();
        setCurrentState(newState);

        lockedStateSwap = false;

        getCurrentState().immediateStep();
    }

    private void setCurrentState(E state) {
        getState(state).preState();
        this.currentState = state;
    }

    protected abstract Map<E, CombatActorState> getStates();
}
