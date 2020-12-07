package com.lastdefenders.game.model.actor.combat;

public interface CombatActorStateObserver<S, C> {
    void stateChange(S state, C combatActor);
}
