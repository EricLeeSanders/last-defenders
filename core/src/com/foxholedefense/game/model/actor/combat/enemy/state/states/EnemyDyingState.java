package com.foxholedefense.game.model.actor.combat.enemy.state.states;

import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.enemy.state.EnemyStateManager.EnemyState;
import com.foxholedefense.game.model.actor.combat.state.CombatActorState;
import com.foxholedefense.game.model.actor.combat.state.StateTransitioner;

import java.util.Map;

/**
 * Created by Eric on 5/5/2017.
 */

public class EnemyDyingState implements CombatActorState {

    private final Enemy enemy;
    private final StateTransitioner<EnemyState> stateTransitioner;

    public EnemyDyingState(Enemy enemy, StateTransitioner<EnemyState> stateTransitioner) {
        this.enemy = enemy;
        this.stateTransitioner = stateTransitioner;
    }

    @Override
    public void loadParameters(Map<String, Object> parameters) {

    }

    @Override
    public void preState() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void postState() {

    }
}
