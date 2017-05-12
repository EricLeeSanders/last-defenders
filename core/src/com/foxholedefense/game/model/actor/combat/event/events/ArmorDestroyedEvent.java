package com.foxholedefense.game.model.actor.combat.event.events;

import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.event.interfaces.CombatActorEvent;
import com.foxholedefense.game.model.actor.effects.label.ArmorDestroyedEffect;
import com.foxholedefense.game.service.factory.EffectFactory;

/**
 * Created by Eric on 5/10/2017.
 */

public class ArmorDestroyedEvent implements CombatActorEvent {

    private final CombatActor combatActor;
    private final EffectFactory effectFactory;

    public ArmorDestroyedEvent(CombatActor combatActor, EffectFactory effectFactory){
        this.combatActor = combatActor;
        this.effectFactory = effectFactory;
    }

    @Override
    public void beginEvent() {
        effectFactory.loadLabelEffect(ArmorDestroyedEffect.class).initialize(combatActor);
    }
}
