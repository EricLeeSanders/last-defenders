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
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.util.Logger;

public class AirStrikePlacement {
	private AirStrike currentAirStrike;
	private ActorGroups actorGroups;
	public AirStrikePlacement(ActorGroups actorGroups) {
		this.actorGroups = actorGroups;
	}
	public void createAirStrike() {
		currentAirStrike = (AirStrike)ActorFactory.loadSupportActor(new Vector2(0, 0), "AirStrike", actorGroups.getEnemyGroup(), actorGroups.getProjectileGroup());
		actorGroups.getSupportGroup().addActor(currentAirStrike);
		currentAirStrike.setActive(false);
		currentAirStrike.setVisible(false);
	}
	public void addLocation(Vector2 location){
		if(Logger.DEBUG)System.out.println("AirStrike added");
		currentAirStrike.addLocation(location, actorGroups.getSupportGroup());
	}
	public void finishCurrentAirStrike() {
		if (isCurrentAirStrike()) {
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
