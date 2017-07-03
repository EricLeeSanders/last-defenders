package com.foxholedefense.game.service.actorplacement;

import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.support.AirStrike;
import com.foxholedefense.game.model.actor.support.AirStrikeLocation;
import com.foxholedefense.game.service.factory.SupportActorFactory;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.datastructures.pool.FHDVector2;

public class AirStrikePlacement {

    private AirStrike currentAirStrike;
    private ActorGroups actorGroups;
    private SupportActorFactory supportActorFactory;

    public AirStrikePlacement(ActorGroups actorGroups, SupportActorFactory supportActorFactory) {

        this.actorGroups = actorGroups;
        this.supportActorFactory = supportActorFactory;
    }

    public void createAirStrike() {

        Logger.info("AirStrikePlacement: creating air strike");
        currentAirStrike = (AirStrike) supportActorFactory.loadSupportActor("AirStrike");
        currentAirStrike.setPosition(0, 0);
        actorGroups.getSupportGroup().addActor(currentAirStrike);
        currentAirStrike.setActive(false);
        currentAirStrike.setVisible(false);
    }

    public void addLocation(FHDVector2 location) {

        Logger.info("AirStrikePlacement: addLocation");
        AirStrikeLocation airStrikeLocation = supportActorFactory
            .loadAirStrikeLocation(location, AirStrike.AIRSTRIKE_RADIUS);
        currentAirStrike.addLocation(airStrikeLocation);
    }

    public void finishCurrentAirStrike() {

        if (isCurrentAirStrike() && currentAirStrike.isReadyToBegin()) {
            Logger.info("AirStrikePlacement: finishing current air strike");
            currentAirStrike.beginAirStrike();
            currentAirStrike.setVisible(true);
            currentAirStrike = null;
        }
    }

    public void removeCurrentAirStrike() {

        if (isCurrentAirStrike()) {
            Logger.info("AirStrikePlacement: removing current airstrike");
            currentAirStrike.freeActor();
            currentAirStrike = null;
        }
    }

    public boolean isCurrentAirStrike() {

        return (currentAirStrike != null);
    }

    public boolean isReadyToBegin() {

        return isCurrentAirStrike()
            && currentAirStrike.isReadyToBegin();
    }
}
