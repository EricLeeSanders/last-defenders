package com.eric.mtd.game.service.actorplacement;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.support.Apache;
import com.eric.mtd.game.model.actor.support.Sandbag;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.util.Logger;

public class ApachePlacement {
	private Vector2 clickCoords;
	private Apache currentApache;
	private ActorGroups actorGroups;

	public ApachePlacement(ActorGroups actorGroups) {
		this.actorGroups = actorGroups;
	}

	public void createApache() {
		currentApache = ActorFactory.loadApache(new Vector2(0, 0));
		actorGroups.getSupportGroup().addActor(currentApache);
		currentApache.setActive(false);
		currentApache.setVisible(false);
	}

	public void moveApache(Vector2 clickCoords) {
		if (currentApache != null) {
			if (!currentApache.isVisible()) {
				currentApache.setVisible(true);
			}
			currentApache.setPositionCenter(clickCoords);
			if (Logger.DEBUG)
				System.out.println(currentApache.getPositionCenter());
		}
	}

	public boolean placeApache() {
		if (currentApache != null) {
			currentApache.setActive(true);
			currentApache = null;
			return true;
		}
		return false;
	}



	public void removeCurrentApache() {
		if (isCurrentApache()) {
			currentApache.freeActor();
			currentApache = null;
		}
	}

	public boolean isCurrentApache() {
		return (currentApache != null);
	}

	public Apache getCurrentApache() {
		return currentApache;
	}

}
