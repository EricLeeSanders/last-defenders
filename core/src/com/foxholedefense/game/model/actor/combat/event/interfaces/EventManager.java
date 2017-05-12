package com.foxholedefense.game.model.actor.combat.event.interfaces;

/**
 * Created by Eric on 5/10/2017.
 */

public interface EventManager{
    void sendEvent(CombatActorEventEnum event);

    enum CombatActorEventEnum {
        ARMOR_DESTROYED;
    }

}
