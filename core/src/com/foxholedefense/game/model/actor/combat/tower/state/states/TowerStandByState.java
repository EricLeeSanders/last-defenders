package com.foxholedefense.game.model.actor.combat.tower.state.states;

import com.foxholedefense.game.model.actor.combat.state.CombatActorState;
import com.foxholedefense.game.model.actor.combat.state.StateTransitioner;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.combat.tower.state.TowerStateManager.TowerState;

import java.util.Map;

/**
 * Created by Eric on 5/8/2017.
 */

public class TowerStandByState implements CombatActorState {

    private final Tower tower;
    private final StateTransitioner<TowerState> stateTransitioner;

    public TowerStandByState(Tower tower, StateTransitioner<TowerState> stateTransitioner){
        this.tower = tower;
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
