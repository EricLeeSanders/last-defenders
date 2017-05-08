package com.foxholedefense.game.model.actor.combat.enemy.state.states;

import com.foxholedefense.game.model.actor.ai.EnemyAI;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.enemy.state.EnemyStateManager.EnemyState;
import com.foxholedefense.game.model.actor.combat.state.CombatActorState;
import com.foxholedefense.game.model.actor.combat.state.StateTransitioner;
import com.foxholedefense.game.model.actor.interfaces.ITargetable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric on 5/5/2017.
 */

public class EnemyRunningState implements CombatActorState {

    private final Enemy enemy;
    private final StateTransitioner<EnemyState> stateTransitioner;
    private float movementAnimationStateTime, findTargetDelayCounter;
    private Map<String, Object> attackTransitionParameters = new HashMap<String, Object>();

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
        if(hasEnemyReachedEnd()){
            stateTransitioner.transition(EnemyState.REACHED_END);
            return;
        }

        if (isReadyToFindTarget()) {
            ITargetable target = findTarget();
            if(target != null){
                attackTransitionParameters.put("target", target);
                stateTransitioner.transition(EnemyState.ATTACKING, attackTransitionParameters);
                return;
            }
        } else {
            findTargetDelayCounter += delta;
        }
    }

    private boolean isReadyToFindTarget(){
        return findTargetDelayCounter >= Enemy.FIND_TARGET_DELAY;
    }


    /**
     * Finds a tower to attack.
     */
    private ITargetable findTarget() {
        return EnemyAI.findNearestTower(enemy, enemy.getTargetGroup().getChildren());
    }


    private boolean hasEnemyReachedEnd(){
        return (enemy.getActions().size == 0
                && !enemy.isDead()
                && enemy.isActive());

    }

    @Override
    public void postState() {

    }
}