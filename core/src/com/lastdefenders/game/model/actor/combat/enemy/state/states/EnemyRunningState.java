package com.lastdefenders.game.model.actor.combat.enemy.state.states;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.game.model.actor.ai.EnemyAI;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateManager.EnemyState;
import com.lastdefenders.game.model.actor.combat.state.CombatActorState;
import com.lastdefenders.game.model.actor.combat.state.StateTransitioner;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric on 5/5/2017.
 */

public class EnemyRunningState implements CombatActorState {

    private final Enemy enemy;
    private final StateTransitioner<EnemyState> stateTransitioner;
    private float movementAnimationStateTime;
    private float findTargetDelayCounter;
    private Map<String, Object> attackTransitionParameters = new HashMap<>();

    public EnemyRunningState(Enemy enemy, StateTransitioner<EnemyState> stateTransitioner) {

        this.enemy = enemy;
        this.stateTransitioner = stateTransitioner;
    }

    @Override
    public void loadParameters(Map<String, Object> parameters) {

    }

    @Override
    public void preState() {

        movementAnimationStateTime = 0;
        findTargetDelayCounter = 0;
    }

    @Override
    public void update(float delta) {

        movementAnimationStateTime += delta;
        enemy.animationStep(movementAnimationStateTime);
        if (hasEnemyReachedEnd()) {
            stateTransitioner.transition(EnemyState.REACHED_END);
            return;
        }

        if ( isReadyToFindTarget()) {
            Targetable target = findTarget();
            if (target != null) {
                attackTransitionParameters.put("target", target);
                stateTransitioner.transition(EnemyState.ATTACKING, attackTransitionParameters);
            }
        } else {
            findTargetDelayCounter += delta;
        }
    }

    private boolean isReadyToFindTarget() {

        return findTargetDelayCounter >= enemy.getFindTargetDelay();
    }


    /**
     * Finds a tower to attack.
     */
    private Targetable findTarget() {

        SnapshotArray<Actor> children = enemy.getTargetGroup().getChildren();
        return EnemyAI.findNearestTower(enemy, children);
    }


    private boolean hasEnemyReachedEnd() {

        return (enemy.getActions().size == 0
            && !enemy.isDead()
            && enemy.isActive());

    }

    @Override
    public void postState() {

    }
}
