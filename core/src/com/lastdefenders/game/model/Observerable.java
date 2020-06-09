package com.lastdefenders.game.model;

public interface Observerable<O> {
    void detachObserver(O observer);

    void attachObserver(O observer);
}
