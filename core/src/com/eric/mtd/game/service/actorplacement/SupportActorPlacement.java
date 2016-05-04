package com.eric.mtd.game.service.actorplacement;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.support.Apache;
import com.eric.mtd.game.model.actor.support.SupportActor;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.util.Logger;

public class SupportActorPlacement {
	private SupportActor currentSupportActor;
	private ActorGroups actorGroups;

	public SupportActorPlacement(ActorGroups actorGroups) {
		this.actorGroups = actorGroups;
	}

	public void createSupportActor(String type) {
		currentSupportActor = ActorFactory.loadSupportActor(new Vector2(0, 0), type, actorGroups.getEnemyGroup());
		actorGroups.getSupportGroup().addActor(currentSupportActor);
		currentSupportActor.setActive(false);
		currentSupportActor.setVisible(false);
		
	}

	public void moveSupportActor(Vector2 clickCoords) {
		if (currentSupportActor != null) {
			if (!currentSupportActor.isVisible()) {
				currentSupportActor.setVisible(true);
			}
			currentSupportActor.setShowRange(true);
			currentSupportActor.setPositionCenter(clickCoords);
			if (Logger.DEBUG)
				System.out.println(currentSupportActor.getPositionCenter());
		}
	}

	public boolean placeSupportActor() {
		if (currentSupportActor != null) {
			//If it is an Apache that is being placed, then we need to call it's initialize method
			if(currentSupportActor instanceof Apache){
				((Apache)currentSupportActor).initialize(currentSupportActor.getPositionCenter());
			}
			currentSupportActor.setActive(true);
			currentSupportActor.setShowRange(false);
			currentSupportActor = null;
			return true;
		}
		return false;
	}



	public void removeCurrentSupportActor() {
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
