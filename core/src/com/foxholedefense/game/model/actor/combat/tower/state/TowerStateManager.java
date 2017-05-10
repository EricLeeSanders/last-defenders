package com.foxholedefense.game.model.actor.combat.tower.state;

import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.combat.state.CombatActorState;
import com.foxholedefense.game.model.actor.combat.state.StateManager;
import com.foxholedefense.game.model.actor.combat.state.StateTransitioner;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.combat.tower.state.TowerStateManager.TowerState;
import com.foxholedefense.game.model.actor.combat.tower.state.states.TowerActiveState;
import com.foxholedefense.game.model.actor.combat.tower.state.states.TowerDyingState;
import com.foxholedefense.game.model.actor.combat.tower.state.states.TowerStandByState;
import com.foxholedefense.game.service.factory.EffectFactory;
import com.foxholedefense.util.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric on 5/8/2017.
 */

public class TowerStateManager implements StateManager<TowerState, CombatActorState>{

    private Map<TowerState, CombatActorState> towerStates = new HashMap<TowerState, CombatActorState>();
    private TowerState currentState;
    private final Tower tower;
    private final EffectFactory effectFactory;

    public TowerStateManager(Tower tower, EffectFactory effectFactory){
        this.tower = tower;
        this.effectFactory = effectFactory;
        initStateObjects();
        currentState = TowerState.STANDBY;
    }

    @Override
    public void update(float delta) {
        getCurrentState().update(delta);
    }

    private void initStateObjects(){
        towerStates.put(TowerState.ACTIVE, new TowerActiveState(tower, this));
        towerStates.put(TowerState.DYING, new TowerDyingState(tower, this, effectFactory));
        towerStates.put(TowerState.STANDBY, new TowerStandByState(tower, this));
    }

    @Override
    public CombatActorState getState(TowerState state) {
        return towerStates.get(state);
    }

    @Override
    public CombatActorState getCurrentState() {
        return towerStates.get(currentState);
    }

    private void setCurrentState(TowerState state){
        this.currentState = state;
    }

    @Override
    public TowerState getCurrentStateName() {
        return currentState;
    }

    private void swapState(TowerState oldState, TowerState newState){
        Logger.info("Swapping states: " + oldState.name() + " to: " + newState.name());
        getState(oldState).postState();
        getState(newState).preState();
        currentState = newState;
    }

    @Override
    public void transition(TowerState state) {
        if(currentState == null){
            setCurrentState(state);
        } else {
            swapState(currentState, state);
        }
    }

    @Override
    public void transition(TowerState state, Map<String, Object> parameters) {
        getState(state).loadParameters(parameters);
        transition(state);
    }

    public enum TowerState {
        ACTIVE,
        DYING,
        STANDBY;
    }
}
