package com.foxholedefense.game.model.actor.combat.enemy.state;


import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.enemy.state.EnemyStateManager.EnemyState;
import com.foxholedefense.game.model.actor.combat.enemy.state.states.EnemyAttackingState;
import com.foxholedefense.game.model.actor.combat.enemy.state.states.EnemyDyingState;
import com.foxholedefense.game.model.actor.combat.enemy.state.states.EnemyReachedEndState;
import com.foxholedefense.game.model.actor.combat.enemy.state.states.EnemyRunningState;
import com.foxholedefense.game.model.actor.combat.enemy.state.states.EnemyStandByState;
import com.foxholedefense.game.model.actor.combat.state.CombatActorState;
import com.foxholedefense.game.model.actor.combat.state.StateTransitioner;
import com.foxholedefense.util.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric on 5/5/2017.
 */

public class EnemyStateManager implements StateTransitioner<EnemyState> {


    private Map<EnemyState, CombatActorState> enemyStates = new HashMap<EnemyState, CombatActorState>();
    private EnemyState currentState;
    private Enemy enemy;

    public EnemyStateManager(Enemy enemy){
        this.enemy = enemy;
        initStateObjects();
    }

    private void initStateObjects(){
        enemyStates.put(EnemyState.RUNNING, new EnemyRunningState(enemy, this));
        enemyStates.put(EnemyState.DYING, new EnemyDyingState(enemy, this));
        enemyStates.put(EnemyState.REACHED_END, new EnemyReachedEndState(enemy, this));
        enemyStates.put(EnemyState.ATTACKING, new EnemyAttackingState(enemy, this));
        enemyStates.put(EnemyState.STANDBY, new EnemyStandByState(enemy, this));

    }

    public void update(float delta){
        getCurrentState().update(delta);
    }

    private void swapState(EnemyState oldState, EnemyState newState){
        Logger.info("Swapping states: " + oldState.name() + " to: " + newState.name());
        getState(oldState).postState();
        getState(newState).preState();
        currentState = newState;
    }

    public CombatActorState getState(EnemyState state){
        return enemyStates.get(state);
    }

    public CombatActorState getCurrentState(){
        return enemyStates.get(currentState);
    }

    public EnemyState getCurrentStateName(){
        return currentState;
    }

    @Override
    public void transition(EnemyState state) {
        swapState(currentState, state);
    }

    public void setCurrentState(EnemyState state){
        this.currentState = state;
    }

    @Override
    public void transition(EnemyState state, Map<String, Object> parameters) {
        transition(state);
        getCurrentState().loadParameters(parameters);
    }

    public enum EnemyState {
        ATTACKING,
        RUNNING,
        DYING,
        REACHED_END,
        STANDBY;

    }

}
