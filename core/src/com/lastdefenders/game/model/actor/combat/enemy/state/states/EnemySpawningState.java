package com.lastdefenders.game.model.actor.combat.enemy.state.states;

import com.badlogic.gdx.math.Vector2;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateEnum;
import com.lastdefenders.game.model.actor.combat.state.CombatActorState;
import com.lastdefenders.game.model.actor.combat.state.StateTransitioner;
import java.util.Map;

public class EnemySpawningState implements CombatActorState {

    private Enemy enemy;
    private StateTransitioner<EnemyStateEnum> stateTransitioner;

    public EnemySpawningState(Enemy enemy, StateTransitioner<EnemyStateEnum> stateTransitioner) {

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
    public void immediateStep() {

    }

    @Override
    public void update(float delta) {

        Vector2 enemyPosCenter = enemy.getPositionCenter();
        if(enemyPosCenter.x >= 0 && enemyPosCenter.y >= 0){
            stateTransitioner.transition(EnemyStateEnum.RUNNING);
        }
    }

    @Override
    public void postState() {
        enemy.setActive(true);
    }
}
