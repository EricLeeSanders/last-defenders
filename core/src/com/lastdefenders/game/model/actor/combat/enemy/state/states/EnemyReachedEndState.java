package com.lastdefenders.game.model.actor.combat.enemy.state.states;

import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateEnum;
import com.lastdefenders.game.model.actor.combat.state.CombatActorState;
import com.lastdefenders.game.model.actor.combat.state.StateTransitioner;
import java.util.Map;

/**
 * Created by Eric on 5/5/2017.
 */

public class EnemyReachedEndState implements CombatActorState {

    private final Enemy enemy;
    private final StateTransitioner<EnemyStateEnum> stateTransitioner;
    private final Player player;

    public EnemyReachedEndState(Enemy enemy, StateTransitioner<EnemyStateEnum> stateTransitioner,
        Player player) {

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
    public void immediateStep() {
        enemy.setActive(false);
        enemy.reachedEnd();
        player.enemyReachedEnd();

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void postState() {

    }
}
