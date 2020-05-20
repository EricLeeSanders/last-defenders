package com.lastdefenders.game.model.actor.combat.state.states;

import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.game.model.actor.combat.state.CombatActorState;
import com.lastdefenders.game.model.actor.combat.state.StateTransitioner;
import com.lastdefenders.game.service.factory.EffectFactory;
import java.util.Map;

/**
 * Created by Eric on 5/10/2017.
 */

public class CombatActorDeadState<E> implements CombatActorState {

    private final CombatActor combatActor;
    private final StateTransitioner<E> stateTransitioner;
    private final E transitionState;
    private final EffectFactory effectFactory;

    public CombatActorDeadState(CombatActor combatActor, StateTransitioner<E> stateTransitioner,
        E transitionState, EffectFactory effectFactory) {

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
        // Has to be put in preState because Update is never called (CombatActor is removed from stage)
        effectFactory.loadDeathEffect(combatActor.getDeathEffectType(), true)
            .initialize(combatActor.getPositionCenter());

            //.initialize(combatActor.getPositionCenter());
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void postState() {

    }
}
