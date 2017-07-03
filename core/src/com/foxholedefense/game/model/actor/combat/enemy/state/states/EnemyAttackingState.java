package com.foxholedefense.game.model.actor.combat.enemy.state.states;

import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.enemy.state.EnemyStateManager.EnemyState;
import com.foxholedefense.game.model.actor.combat.state.CombatActorState;
import com.foxholedefense.game.model.actor.combat.state.StateTransitioner;
import com.foxholedefense.game.model.actor.interfaces.Targetable;

import java.util.Map;

/**
 * Created by Eric on 5/5/2017.
 */

public class EnemyAttackingState implements CombatActorState {

    private final Enemy enemy;
    private final StateTransitioner<EnemyState> stateTransitioner;
    private float movementDelayCounter, attackCounter;
    private Targetable target;

    public EnemyAttackingState(Enemy enemy, StateTransitioner<EnemyState> stateTransitioner) {
        this.enemy = enemy;
        this.stateTransitioner = stateTransitioner;
    }

    @Override
    public void loadParameters(Map<String, Object> parameters) {
        Object targetObj = parameters.get("target");
        if (targetObj == null || !(targetObj instanceof Targetable)) {
            throw new IllegalStateException("Must have a target to be in this state");
        }

        target = (Targetable) targetObj;
    }

    @Override
    public void preState() {
        movementDelayCounter = 0;
        attackCounter = 100;
        enemy.preAttack();
    }

    @Override
    public void update(float delta) {

        movementDelayCounter += delta;
        if (movementDelayCounter >= Enemy.MOVEMENT_DELAY) {
            stateTransitioner.transition(EnemyState.RUNNING);
            return;
        }

        if (canAttack()) {
            if (target != null && !target.isDead()) {
                enemy.attack(target);
                attackCounter = 0;
            }
        } else {
            attackCounter += delta;
        }
    }

    private boolean canAttack() {
        return attackCounter >= enemy.getAttackSpeed();
    }

    @Override
    public void postState() {
        enemy.postAttack();
    }
}
