package com.lastdefenders.game.model.actor.combat.enemy.state;


import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.state.states.EnemyAttackingState;
import com.lastdefenders.game.model.actor.combat.enemy.state.states.EnemyDeadState;
import com.lastdefenders.game.model.actor.combat.enemy.state.states.EnemyReachedEndState;
import com.lastdefenders.game.model.actor.combat.enemy.state.states.EnemyRunningState;
import com.lastdefenders.game.model.actor.combat.enemy.state.states.EnemySpawningState;
import com.lastdefenders.game.model.actor.combat.state.CombatActorState;
import com.lastdefenders.game.model.actor.combat.state.StateManager;
import com.lastdefenders.game.model.actor.combat.state.states.CombatActorStandByState;
import com.lastdefenders.game.service.factory.EffectFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric on 5/5/2017.
 */

public class EnemyStateManager extends StateManager<EnemyStateEnum, Enemy> {

    private Map<EnemyStateEnum, CombatActorState> enemyStates = new HashMap<>();

    public EnemyStateManager(Enemy enemy, EffectFactory effectFactory, Player player) {
        super(enemy);
        enemyStates.put(EnemyStateEnum.RUNNING, new EnemyRunningState(enemy, this));
        enemyStates.put(EnemyStateEnum.DEAD, new EnemyDeadState(enemy, effectFactory, player));
        enemyStates.put(EnemyStateEnum.REACHED_END, new EnemyReachedEndState(enemy, this, player));
        enemyStates.put(EnemyStateEnum.ATTACKING, new EnemyAttackingState(enemy, this));
        enemyStates.put(EnemyStateEnum.STANDBY, new CombatActorStandByState());
        enemyStates.put(EnemyStateEnum.SPAWNING, new EnemySpawningState(enemy, this));
    }


    @Override
    protected Map<EnemyStateEnum, CombatActorState> getStates() {

        return enemyStates;
    }
}
