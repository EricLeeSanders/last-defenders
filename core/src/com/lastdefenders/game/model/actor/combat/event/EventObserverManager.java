package com.lastdefenders.game.model.actor.combat.event;

import com.lastdefenders.util.Logger;
import java.util.HashSet;
import java.util.Set;

public class EventObserverManager<ObserverType extends EventObserver<ObserverableType, EventType>, EventType, ObserverableType> {
    private Set<ObserverType> observers = new HashSet<>();

    public void detachObserver(ObserverType observer) {
        observers.remove(observer);
    }
    public void attachObserver(ObserverType observer) {
        observers.add(observer);
    }

    public void notifyEventObservers(EventType event, ObserverableType observerable){

        Logger.info("EventObserverManager: NotifyEventObservers of Event: " + event);

        Set<ObserverType> observerCopy = new HashSet<>(observers);

        for(ObserverType observer : observerCopy){
            observer.eventNotification(event, observerable);
        }
    }
}
