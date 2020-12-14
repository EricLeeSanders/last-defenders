package com.lastdefenders.game.model.actor.combat.event;

public interface EventObserver<ObserverableType, EventType> {
    void eventNotification(EventType event, ObserverableType observerable);
}
