package com.lastdefenders.game.service.actorplacement;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.lastdefenders.game.model.actor.ActorGroups;
import com.lastdefenders.game.model.actor.support.Apache;
import com.lastdefenders.game.model.actor.support.CombatSupportActor;
import com.lastdefenders.game.service.factory.SupportActorFactory;
import com.lastdefenders.util.Logger;

public class SupportActorPlacement {

    private CombatSupportActor currentSupportActor;
    private SupportActorFactory supportActorFactory;

    public SupportActorPlacement(SupportActorFactory supportActorFactory) {

        this.supportActorFactory = supportActorFactory;
    }

    public <T extends Actor> void createSupportActor(Class<T> type) {

        Logger.info("SupportActorPlacement: creating supply actor: " + type.getSimpleName());
        currentSupportActor = (CombatSupportActor) supportActorFactory.loadSupportActor(type, true);
        currentSupportActor.setPosition(0, 0);
        currentSupportActor.setActive(false);
        currentSupportActor.setVisible(false);

    }

    public void moveSupportActor(Vector2 clickCoords) {

        if (currentSupportActor != null) {
            currentSupportActor.setVisible(true);
            currentSupportActor.setShowRange(true);
            currentSupportActor.setPositionCenter(clickCoords);
        }
    }

    public void placeSupportActor() {

        Logger.info("SupportActorPlacement: trying to place Support Actor");
        if (currentSupportActor != null) {
            //If it is an Apache that is being placed, then we need to call it's initialize method
            if (currentSupportActor instanceof Apache) {
                ((Apache) currentSupportActor).initialize(currentSupportActor.getPositionCenter());
            }
            currentSupportActor.setActive(true);
            currentSupportActor.setShowRange(false);
            currentSupportActor = null;
            Logger.info("SupportActorPlacement: placing Support Actor");
        }
    }


    public void removeCurrentSupportActor() {

        Logger.info("SupportActorPlacement: removing Support Actor");
        if (isCurrentSupportActor()) {
            currentSupportActor.freeActor();
            currentSupportActor = null;
        }
    }

    public boolean isCurrentSupportActor() {

        return (currentSupportActor != null);
    }

    public CombatSupportActor getCurrentSupportActor() {

        return currentSupportActor;
    }

}
