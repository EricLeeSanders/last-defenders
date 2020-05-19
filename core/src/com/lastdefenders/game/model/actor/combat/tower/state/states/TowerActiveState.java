package com.lastdefenders.game.model.actor.combat.tower.state.states;

import com.lastdefenders.game.model.actor.combat.state.CombatActorState;
import com.lastdefenders.game.model.actor.combat.state.StateTransitioner;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.state.TowerStateManager.TowerState;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import com.lastdefenders.util.ActorUtil;
import java.util.Map;

/**
 * Created by Eric on 5/8/2017.
 */

public class TowerActiveState implements CombatActorState {

    private final Tower tower;
    private float attackCounter;

    public TowerActiveState(Tower tower, StateTransitioner<TowerState> stateTransitioner) {

        this.tower = tower;
    }

    @Override
    public void loadParameters(Map<String, Object> parameters) {

    }

    @Override
    public void preState() {

        attackCounter = Float.MAX_VALUE; // ready to attack
    }

    @Override
    public void update(float delta) {

        Targetable target = findTarget();
        if (target != null && !target.isDead()) {
            tower.setRotation(
                ActorUtil.calculateRotation(target.getPositionCenter(), tower.getPositionCenter()));
            if (canAttack()) {
                attackCounter = 0;
                tower.attackTarget(target);
            }
        }

        attackCounter += delta;
    }

    private boolean canAttack() {

        return attackCounter >= tower.getAttackSpeed();
    }

    /**
     * Find a target based on the Target Priority
     */
    private Targetable findTarget() {

        return tower.getAI().findTarget(tower, tower.getTargetGroup().getChildren());
    }

    @Override
    public void postState() {

    }
}
