package com.lastdefenders.game.service.actorplacement;

import com.lastdefenders.game.model.actor.ActorGroups;
import com.lastdefenders.game.model.actor.support.AirStrike;
import com.lastdefenders.game.model.actor.support.AirStrikeLocation;
import com.lastdefenders.game.service.factory.SupportActorFactory;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.datastructures.pool.LDVector2;

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

    public void addLocation(LDVector2 location) {

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

    public AirStrike getCurrentAirStrike(){

        return currentAirStrike;
    }

    public boolean isCurrentAirStrike() {

        return (currentAirStrike != null);
    }

    public boolean isReadyToBegin() {

        return isCurrentAirStrike()
            && currentAirStrike.isReadyToBegin();
    }
}
