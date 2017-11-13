package com.lastdefenders.game.model.actor.combat.event;

import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.game.model.actor.combat.event.events.ArmorDestroyedEvent;
import com.lastdefenders.game.model.actor.combat.event.interfaces.CombatActorEvent;
import com.lastdefenders.game.model.actor.combat.event.interfaces.EventManager;
import com.lastdefenders.game.service.factory.EffectFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric on 5/10/2017.
 */

public class EventManagerImpl implements EventManager {

    private Map<CombatActorEventEnum, CombatActorEvent> events = new HashMap<>();

    public EventManagerImpl(CombatActor combatActor, EffectFactory effectFactory) {

        initEventObjects(combatActor, effectFactory);
    }

    private void initEventObjects(CombatActor combatActor, EffectFactory effectFactory) {

        events.put(CombatActorEventEnum.ARMOR_DESTROYED,
            new ArmorDestroyedEvent(combatActor, effectFactory));
    }

    @Override
    public void sendEvent(CombatActorEventEnum eventEnum) {

        CombatActorEvent event = getEvent(eventEnum);
        event.beginEvent();
    }

    private CombatActorEvent getEvent(CombatActorEventEnum eventEnum) {

        return events.get(eventEnum);
    }
}
