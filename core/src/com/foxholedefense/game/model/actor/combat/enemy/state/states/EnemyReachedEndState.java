package com.foxholedefense.game.model.actor.combat.enemy.state.states;

import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.enemy.state.EnemyStateManager.EnemyState;
import com.foxholedefense.game.model.actor.combat.state.CombatActorState;
import com.foxholedefense.game.model.actor.combat.state.StateTransitioner;

import java.util.Map;

/**
 * Created by Eric on 5/5/2017.
 */

public class EnemyReachedEndState implements CombatActorState {

    private final Enemy enemy;
    private final StateTransitioner<EnemyState> stateTransitioner;
    private final Player player;

    public EnemyReachedEndState(Enemy enemy, StateTransitioner<EnemyState> stateTransitioner, Player player) {
        this.enemy = enemy;
        this.stateTransitioner = stateTransitioner;
        this.player = player;
    }

    @Override
    public void loadParameters(Map<String, Object> parameters) {

    }

    @Override
    public void preState() {

    }

    @Override
    public void update(float delta) {
        enemy.reachedEnd();
        player.enemyReachedEnd();
        stateTransitioner.transition(EnemyState.STANDBY);
    }

    @Override
    public void postState() {

    }
}
