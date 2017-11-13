package com.lastdefenders.game.model.actor.combat.enemy.state;


import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateManager.EnemyState;
import com.lastdefenders.game.model.actor.combat.enemy.state.states.EnemyAttackingState;
import com.lastdefenders.game.model.actor.combat.enemy.state.states.EnemyDyingState;
import com.lastdefenders.game.model.actor.combat.enemy.state.states.EnemyReachedEndState;
import com.lastdefenders.game.model.actor.combat.enemy.state.states.EnemyRunningState;
import com.lastdefenders.game.model.actor.combat.state.CombatActorState;
import com.lastdefenders.game.model.actor.combat.state.StateManager;
import com.lastdefenders.game.model.actor.combat.state.states.CombatActorStandByState;
import com.lastdefenders.game.service.factory.EffectFactory;
import com.lastdefenders.util.Logger;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric on 5/5/2017.
 */

public class EnemyStateManager implements StateManager<EnemyState, CombatActorState> {


    private Map<EnemyState, CombatActorState> enemyStates = new HashMap<>();
    private EnemyState currentState;

    public EnemyStateManager(Enemy enemy, EffectFactory effectFactory, Player player) {

        initStateObjects(enemy, effectFactory, player);
        currentState = EnemyState.STANDBY;
    }

    private void initStateObjects(Enemy enemy, EffectFactory effectFactory, Player player) {

        enemyStates.put(EnemyState.RUNNING, new EnemyRunningState(enemy, this));
        enemyStates.put(EnemyState.DYING, new EnemyDyingState(enemy, this, effectFactory, player));
        enemyStates.put(EnemyState.REACHED_END, new EnemyReachedEndState(enemy, this, player));
        enemyStates.put(EnemyState.ATTACKING, new EnemyAttackingState(enemy, this));
        enemyStates.put(EnemyState.STANDBY, new CombatActorStandByState());

    }

    @Override
    public void update(float delta) {

        getCurrentState().update(delta);
    }

    private void swapState(EnemyState oldState, EnemyState newState) {

        Logger.info("Swapping states: " + oldState.name() + " to: " + newState.name());
        getState(oldState).postState();
        currentState = newState;
        getState(newState).preState();
    }

    @Override
    public CombatActorState getState(EnemyState state) {

        return enemyStates.get(state);
    }

    @Override
    public CombatActorState getCurrentState() {

        return enemyStates.get(currentState);
    }

    private void setCurrentState(EnemyState state) {

        this.currentState = state;
    }

    @Override
    public EnemyState getCurrentStateName() {

        return currentState;
    }

    @Override
    public void transition(EnemyState state) {

        if (currentState == null) {
            setCurrentState(state);
        } else {
            swapState(currentState, state);
        }
    }

    @Override
    public void transition(EnemyState state, Map<String, Object> parameters) {

        getState(state).loadParameters(parameters);
        transition(state);
    }

    public enum EnemyState {
        ATTACKING,
        DYING,
        RUNNING,
        REACHED_END,
        STANDBY

    }
}
