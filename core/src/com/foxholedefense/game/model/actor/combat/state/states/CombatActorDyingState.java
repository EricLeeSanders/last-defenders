package com.foxholedefense.game.model.actor.combat.state.states;

import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.state.CombatActorState;
import com.foxholedefense.game.model.actor.combat.state.StateTransitioner;
import com.foxholedefense.game.service.factory.EffectFactory;

import java.util.Map;

/**
 * Created by Eric on 5/10/2017.
 */

public class CombatActorDyingState<E> implements CombatActorState {

    private final CombatActor combatActor;
    private final StateTransitioner<E> stateTransitioner;
    private final E transitionState;
    private final EffectFactory effectFactory;

    public CombatActorDyingState(CombatActor combatActor, StateTransitioner<E> stateTransitioner, E transitionState, EffectFactory effectFactory){
        this.combatActor = combatActor;
        this.stateTransitioner = stateTransitioner;
        this.transitionState = transitionState;
        this.effectFactory = effectFactory;
    }

    @Override
    public void loadParameters(Map<String, Object> parameters) {

    }

    @Override
    public void preState() {
        // Has to be put in preState because Update is never called (Enemy is removed from stage)
        effectFactory.loadDeathEffect(combatActor.getDeathEffectType()).initialize(combatActor.getPositionCenter());
        stateTransitioner.transition(transitionState);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void postState() {

    }
}
