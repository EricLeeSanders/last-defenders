package com.eric.mtd.game.service.actorplacement;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.eric.mtd.game.helper.CollisionDetection;
import com.eric.mtd.game.model.actor.ActorGroups;
import com.eric.mtd.game.model.actor.health.HealthBar;
import com.eric.mtd.game.model.actor.interfaces.IRotatable;
import com.eric.mtd.game.model.actor.tower.Tower;
import com.eric.mtd.game.model.level.Map;
import com.eric.mtd.game.service.actorfactory.ActorFactory;
import com.eric.mtd.util.Logger;
import com.eric.mtd.util.Resources;

/**
 * Responsible for placing the Tower on the Stage
 * 
 * @author Eric
 *
 */
public class TowerPlacement {
	private Tower currentTower;
	private ActorGroups actorGroups;
	private Map map;
	public TowerPlacement(Map map, ActorGroups actorGroups) {
		this.map = map;
		this.actorGroups = actorGroups;
	}

	/**
	 * Determines if the tower can be rotated or not
	 * 
	 * @return Boolean - tower is Rotatable
	 */
	public boolean isTowerRotatable() {
		if (getCurrentTower() instanceof IRotatable) {
			return true;
		} else {
			return false;

		}
	}

	/**
	 * Create a tower
	 * 
	 * @param type
	 *            - Type of tower
	 */
	public void createTower(String type) {
		currentTower = ActorFactory.loadTower(new Vector2(0, 0), type, actorGroups.getEnemyGroup());
		actorGroups.getTowerGroup().addActor(currentTower);
		currentTower.setVisible(false);
	}

	/**
	 * Move the tower that is still being placed (not an active tower)
	 * 
	 * @param clickCoords
	 *            - Where to move the tower to
	 */
	public void moveTower(Vector2 clickCoords) {
		if (currentTower != null) {
			currentTower.setVisible(true);
			currentTower.setPositionCenter(clickCoords);
			if (Logger.DEBUG)
				System.out.println(currentTower.getPositionCenter());
			currentTower.setShowRange(true);
			if (towerCollides()) {
				currentTower.setRangeColor(1f, 0f, 0f, 0.5f);// Red

			} else {
				currentTower.setRangeColor(1f, 1f, 1f, .5f);
			}
		}
	}

	/**
	 * Rotates the tower
	 * 
	 * @param rotation
	 *            - amount to rotate
	 */
	public void rotateTower(float rotation) {
		if (currentTower != null) {
			currentTower.setRotation(currentTower.getRotation() - rotation);
			if (Logger.DEBUG)
				System.out.println(currentTower.getPositionCenter());
			currentTower.setShowRange(true);
			if (towerCollides()) {
				currentTower.setRangeColor(1f, 0f, 0f, 0.5f);// Red

			} else {
				currentTower.setRangeColor(1f, 1f, 1f, .5f);
			}
		}
	}

	/**
	 * Place the tower and make it active
	 * 
	 * @return boolean - if Tower was successfully placed
	 */
	public boolean placeTower() {
		if (currentTower != null) {
			if (!towerCollides()) {

				currentTower.setShowRange(false);
				currentTower.remove();
				currentTower.setActive(true);
				currentTower.setDead(false);
				actorGroups.getTowerGroup().addActor(currentTower);
				HealthBar healthBar = ActorFactory.loadHealthBar();
				healthBar.setActor(currentTower);
				actorGroups.getHealthBarGroup().addActor(healthBar);
				currentTower = null;
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if tower collides with path or actors
	 * 
	 * @return boolean - if Tower collides
	 */
	private boolean towerCollides() {
		SnapshotArray<Actor> towers = actorGroups.getTowerGroup().getChildren();

		if (CollisionDetection.CollisionWithPath(map.getPathBoundaries(), currentTower)) {
			return true;
		} else if (CollisionDetection.CollisionWithActors(towers, currentTower)) {
			return true;
		}
		return false;

	}

	/**
	 * Remove the current tower
	 */
	public void removeCurrentTower() {
		if (currentTower != null) {
			currentTower.freeActor();
			currentTower = null;
		}
	}

	/**
	 * If there is a current tower being placed
	 * 
	 * @return boolean
	 */
	public boolean isCurrentTower() {
		if (currentTower == null) {
			return false;
		} else {
			return true;
		}
	}

	public Tower getCurrentTower() {
		return currentTower;
	}

}
