package com.lastdefenders.game.model.actor.combat.enemy.state.states;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.game.model.actor.ai.EnemyAI;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateEnum;
import com.lastdefenders.game.model.actor.combat.state.CombatActorState;
import com.lastdefenders.game.model.actor.combat.state.StateTransitioner;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric on 5/5/2017.
 */

public class EnemyRunningState implements CombatActorState {

    private static final float MIN_FIND_TARGET_DELAY = 1f;
    private static final float MAX_FIND_TARGET_DELAY = 2.0f;

    private final Enemy enemy;
    private final StateTransitioner<EnemyStateEnum> stateTransitioner;
    private float movementAnimationStateTime;
    private float findTargetDelayCounter;
    private float findTargetDelay;
    private Map<String, Object> attackTransitionParameters = new HashMap<>();

    private float minTargetDelay = 0;

    public EnemyRunningState(Enemy enemy, StateTransitioner<EnemyStateEnum> stateTransitioner) {

        this.enemy = enemy;
        this.stateTransitioner = stateTransitioner;
    }

    @Override
    public void loadParameters(Map<String, Object> parameters) {

    }

    @Override
    public void preState() {

        if(enemy.getState() != null && enemy.getState().equals(EnemyStateEnum.SPAWNING)){
            minTargetDelay = 0;
        } else {
            minTargetDelay = enemy.getAttackSpeed();
        }
        movementAnimationStateTime = 0;
        findTargetDelayCounter = 0;
        createFindTargetDelay();
    }

    @Override
    public void immediateStep() {

    }

    @Override
    public void update(float delta) {

        movementAnimationStateTime += delta;
        enemy.animationStep(movementAnimationStateTime);
        if (hasEnemyReachedEnd()) {
            stateTransitioner.transition(EnemyStateEnum.REACHED_END);
            return;
        }

        if ( isReadyToFindTarget()) {
            minTargetDelay = 0;
            createFindTargetDelay();
            findTargetDelayCounter = 0;
            Targetable target = findTarget();
            if (target != null) {
                attackTransitionParameters.put("target", target);
                stateTransitioner.transition(EnemyStateEnum.ATTACKING, attackTransitionParameters);
            }
        } else {
            findTargetDelayCounter += delta;
        }
    }

    private boolean isReadyToFindTarget() {

        return findTargetDelayCounter >= findTargetDelay;
    }

        /**
         * Finds a tower to attack.
         */

    private Targetable findTarget() {

        SnapshotArray<Tower> children = enemy.getEnemyGroup().getCastedChildren();
        return EnemyAI.findRandomTowerInRange(enemy, children);
    }


    private boolean hasEnemyReachedEnd() {

        return (enemy.getActions().size == 0
            && !enemy.isDead()
            && enemy.isActive());

    }

    @Override
    public void postState() {
        attackTransitionParameters.clear();
    }

    private void createFindTargetDelay() {
        float min = minTargetDelay;
        float max = min + MAX_FIND_TARGET_DELAY;

        findTargetDelay = MathUtils.random(min, max);

    }
}
