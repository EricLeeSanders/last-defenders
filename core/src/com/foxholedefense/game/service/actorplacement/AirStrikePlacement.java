package com.foxholedefense.game.service.actorplacement;

import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.support.AirStrike;
import com.foxholedefense.game.model.actor.support.AirStrikeLocation;
import com.foxholedefense.game.service.factory.SupportActorFactory;
import com.foxholedefense.util.datastructures.pool.FHDVector2;
import com.foxholedefense.util.Logger;

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
		currentAirStrike = (AirStrike)supportActorFactory.loadSupportActor("AirStrike");
		currentAirStrike.setPosition(0, 0);
		actorGroups.getSupportGroup().addActor(currentAirStrike);
		currentAirStrike.setActive(false);
		currentAirStrike.setVisible(false);
	}
	public void addLocation(FHDVector2 location){
		Logger.info("AirStrikePlacement: addLocation");
		AirStrikeLocation airStrikeLocation = new AirStrikeLocation(location,AirStrike.AIRSTRIKE_RADIUS, actorFactory.getLoadedTextures().get("range-black") );
		currentAirStrike.addLocation(airStrikeLocation);
		actorGroups.getSupportGroup().addActor(airStrikeLocation);
	}
	public void finishCurrentAirStrike() {
		Logger.info("AirStrikePlacement: finishing current air strike");
		if (isCurrentAirStrike()) {
			currentAirStrike.beginAirStrike();
			currentAirStrike.setVisible(true);
			currentAirStrike = null;
		}
	}
	
	public void removeCurrentAirStrike(){
		Logger.info("AirStrikePlacement: removing current airstrike");
		if (isCurrentAirStrike()) {
			currentAirStrike.freeActor();
			currentAirStrike = null;
		}
	}

	public boolean isCurrentAirStrike() {
		return (currentAirStrike != null);
	}

	public AirStrike getCurrentAirStrike() {
		return currentAirStrike;
	}
}
