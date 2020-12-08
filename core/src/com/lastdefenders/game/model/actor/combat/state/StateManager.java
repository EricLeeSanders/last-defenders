package com.lastdefenders.game.model.actor.combat.state;

import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.game.model.Observerable;
import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.util.Logger;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric on 5/8/2017.
 */

public abstract class StateManager<E extends Enum<E>, T extends CombatActor> implements StateTransitioner<E>,
    Observerable<CombatActorStateObserver<E, T>> {

    private T combatActor;
    private E currentState;
    private SnapshotArray<CombatActorStateObserver<E, T>> observers = new SnapshotArray<>();
    private Map<String, Object> emptyParams = new HashMap<>(); // Used for when no params are passed.
    private Boolean lockedStateSwap = false;

    public StateManager(T combatActor){
        this.combatActor = combatActor;
    }

    @Override
    public void detachObserver(CombatActorStateObserver<E, T> observer){
        observers.removeValue(observer, false);
    }

    @Override
    public void attachObserver(CombatActorStateObserver<E, T> observer){
        observers.add(observer);
    }

    private void removeObservers(){
        observers.clear();
    }

    public void update(float delta) {

        getCurrentState().update(delta);
    }

    @SuppressWarnings("unchecked")
    private void notifyObservers(){
        Logger.info("State Manager: notifying observers");
        Object[] objects = observers.begin();
        for (int i = observers.size - 1; i >= 0; i--) {
            CombatActorStateObserver<E, T> observer = (CombatActorStateObserver<E,T>) objects[i];
            observer.stateChange(getCurrentStateName(), combatActor);
        }
        observers.end();
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
        oldState.name();
        newState.name();

        Logger.info("State Manager: Swapping states (" + combatActor.ID +"): " + oldState.name() + " to: " + newState.name());
        if(lockedStateSwap){
            throw new IllegalStateException("Enemy State Swap Locked");
        }

        lockedStateSwap = true;
        getState(oldState).postState();
        setCurrentState(newState);

        notifyObservers();
        lockedStateSwap = false;

        getCurrentState().immediateStep();
    }

    private void setCurrentState(E state) {
        getState(state).preState();
        this.currentState = state;
    }


    public void reset() {
        removeObservers();
       // this.transition(E.STANDBY);
    }

    protected abstract Map<E, CombatActorState> getStates();
}
