package com.eric.mtd.game.service.actorplacement;

//import java.lang.reflect.Field;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.perks.Sandbag;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.util.Logger;

public class SandbagPlacement {
	private Vector2 clickCoords;
	private TiledMap tiledMap;
	private Sandbag currentSandbag;
	private Player player;
	private ActorGroups actorGroups;

	public SandbagPlacement(Player player, TiledMap tiledMap, ActorGroups actorGroups) {
		this.tiledMap = tiledMap;
		this.player = player;
		this.actorGroups = actorGroups;
	}

	public void createSandbag() {
		currentSandbag = ActorFactory.loadSandbag(new Vector2(0, 0));
		// stage.addActor(currentSandbag);
		currentSandbag.setVisible(false);
	}

	public void moveSandbag(Vector2 clickCoords) {
		if (currentSandbag != null) {
			if (!currentSandbag.isVisible()) {
				currentSandbag.setVisible(true);
			}
			currentSandbag.setPositionCenter(clickCoords);
			if (Logger.DEBUG)
				System.out.println(currentSandbag.getPositionCenter());
			if (sandbagCollides()) {
				if (Logger.DEBUG)
					System.out.println("Sandbag collides");

			}
		}
	}

	public void rotateSandbag(float rotation) {
		if (currentSandbag != null) {
			currentSandbag.setRotation(currentSandbag.getRotation() - rotation);// rotate
																				// clockwise
			if (Logger.DEBUG)
				System.out.println(currentSandbag.getPositionCenter());
			if (sandbagCollides()) {
				if (Logger.DEBUG)
					System.out.println("Sandbag collides");

			}
		}
	}

	public boolean placeSandbag() {
		if (currentSandbag != null) {
			if (!sandbagCollides()) {

				currentSandbag.remove();
				// currentSandbag.setActive(true);
				actorGroups.getSandbagGroup().addActor(currentSandbag);

				// player.spendMoney(currentSandbag.getCost());
				currentSandbag = null;
				return true;
			}
		}
		return false;
	}

	public boolean sandbagCollides() {
		SnapshotArray<Actor> towers = actorGroups.getTowerGroup().getChildren();
		SnapshotArray<Actor> sandbags = actorGroups.getSandbagGroup().getChildren();
		SnapshotArray<Actor> actors = new SnapshotArray<Actor>();
		actors.addAll(towers);
		actors.addAll(sandbags);
		/*
		 * if(CollisionDetection.CollisionWithPath(tiledMap, currentSandbag)){
		 * return true; } else if(CollisionDetection.CollisionWithActors(actors,
		 * currentSandbag)){ return true; }
		 */
		return false;

	}

	public void removeCurrentSandbag() {
		if (currentSandbag != null) {
			currentSandbag.freeActor();
			currentSandbag.remove();
			currentSandbag = null;
		}
	}

	public boolean isCurrentSandbag() {
		if (currentSandbag == null) {
			return false;
		} else {
			return true;
		}
	}

	public Sandbag getCurrentSandbag() {
		return currentSandbag;
	}

}
