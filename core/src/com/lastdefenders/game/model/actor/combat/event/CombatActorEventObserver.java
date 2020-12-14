package com.lastdefenders.game.model.actor.combat.event;

import com.lastdefenders.game.model.actor.combat.CombatActor;

public interface CombatActorEventObserver extends
    EventObserver<CombatActorEventObserver, CombatActor> {

    @Override
    void eventNotification(CombatActor event, CombatActorEventObserver observerable);

    //void combatActorEvent(CombatActorEventEnum event, CombatActor combatActor);
}
