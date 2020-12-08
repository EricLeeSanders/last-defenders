package com.lastdefenders.game.model.actor.combat.state;

public interface CombatActorStateObserver<S, C> {
    void stateChange(S state, C combatActor);
}
