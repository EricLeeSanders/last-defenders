package com.foxholedefense.game.model.actor.combat.tower.state.states;

import com.foxholedefense.game.model.actor.combat.state.CombatActorState;
import com.foxholedefense.game.model.actor.combat.state.StateTransitioner;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.combat.tower.state.TowerStateManager.TowerState;
import com.foxholedefense.game.model.actor.effects.texture.animation.EnemyCoinEffect;
import com.foxholedefense.game.service.factory.EffectFactory;

import java.util.Map;

/**
 * Created by Eric on 5/8/2017.
 */

public class TowerDyingState implements CombatActorState {

    private final Tower tower;
    private final StateTransitioner<TowerState> stateTransitioner;
    private final EffectFactory effectFactory;

    public TowerDyingState(Tower tower, StateTransitioner<TowerState> stateTransitioner, EffectFactory effectFactory){
        this.tower = tower;
        this.stateTransitioner = stateTransitioner;
        this.effectFactory = effectFactory;
    }

    @Override
    public void loadParameters(Map<String, Object> parameters) {

    }

    @Override
    public void preState() {
        effectFactory.loadDeathEffect(tower.getDeathEffectType()).initialize(tower.getPositionCenter());
        stateTransitioner.transition(TowerState.STANDBY);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void postState() {

    }
}
