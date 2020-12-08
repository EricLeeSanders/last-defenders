package com.lastdefenders.game.model.actor.combat.event;

import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.game.model.actor.combat.event.events.CombatActorEventEnum;

public interface CombatActorEventObserver {
    void combatActorEvent(CombatActorEventEnum event, CombatActor combatActor);
}
