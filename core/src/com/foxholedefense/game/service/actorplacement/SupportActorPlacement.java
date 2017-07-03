package com.foxholedefense.game.service.actorplacement;

import com.badlogic.gdx.math.Vector2;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.support.Apache;
import com.foxholedefense.game.model.actor.support.SupportActor;
import com.foxholedefense.game.service.factory.SupportActorFactory;
import com.foxholedefense.util.Logger;

public class SupportActorPlacement {

    private SupportActor currentSupportActor;
    private ActorGroups actorGroups;
    private SupportActorFactory supportActorFactory;

    public SupportActorPlacement(ActorGroups actorGroups, SupportActorFactory supportActorFactory) {

        this.actorGroups = actorGroups;
        this.supportActorFactory = supportActorFactory;
    }

    public void createSupportActor(String type) {

        Logger.info("SupportActorPlacement: creating supply actor: " + type);
        currentSupportActor = supportActorFactory.loadSupportActor(type);
        currentSupportActor.setPosition(0, 0);
        if (type.equals("LandMine")) {
            actorGroups.getLandmineGroup().addActor(currentSupportActor);
        } else {
            actorGroups.getSupportGroup().addActor(currentSupportActor);
        }
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

    public SupportActor getCurrentSupportActor() {

        return currentSupportActor;
    }

}
