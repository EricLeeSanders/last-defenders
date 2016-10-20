package com.eric.mtd.game.service.actorplacement;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.support.AirStrike;
import com.eric.mtd.game.model.actor.support.AirStrikeLocation;
import com.eric.mtd.game.model.actor.support.SupportActor;
import com.eric.mtd.game.service.factory.ActorFactory;
import com.eric.mtd.util.Logger;

public class AirStrikePlacement {
	private AirStrike currentAirStrike;
	private ActorGroups actorGroups;
	private ActorFactory actorFactory;
	public AirStrikePlacement(ActorGroups actorGroups, ActorFactory actorFactory) {
		this.actorGroups = actorGroups;
		this.actorFactory = actorFactory;
	}
	public void createAirStrike() {
		currentAirStrike = (AirStrike)actorFactory.loadSupportActor("AirStrike");
		currentAirStrike.setPosition(0, 0);
		actorGroups.getSupportGroup().addActor(currentAirStrike);
		currentAirStrike.setActive(false);
		currentAirStrike.setVisible(false);
	}
	public void addLocation(Vector2 location){
		currentAirStrike.addLocation(location, actorGroups.getSupportGroup());
	}
	public void finishCurrentAirStrike() {
		if (isCurrentAirStrike()) {
			currentAirStrike.beginAirStrike();
			currentAirStrike.setVisible(true);
			currentAirStrike = null;
		}
	}
	
	public void removeCurrentAirStrike(){
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
