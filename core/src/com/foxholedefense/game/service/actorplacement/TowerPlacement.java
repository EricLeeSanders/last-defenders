package com.foxholedefense.game.service.actorplacement;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.helper.CollisionDetection;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.health.HealthBar;
import com.foxholedefense.game.model.actor.interfaces.IRotatable;
import com.foxholedefense.game.model.level.Map;
import com.foxholedefense.game.service.factory.ActorFactory;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

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
	private ActorFactory actorFactory;
	public TowerPlacement(Map map, ActorGroups actorGroups, ActorFactory actorFactory) {
		this.map = map;
		this.actorGroups = actorGroups;
		this.actorFactory = actorFactory;
	}

	/**
	 * Determines if the tower can be rotated or not
	 * 
	 * @return Boolean - tower is Rotatable
	 */
	public boolean isTowerRotatable() {
		return (getCurrentTower() instanceof IRotatable);
	}

	/**
	 * Create a tower
	 * 
	 * @param type - Type of tower
	 */
	public void createTower(String type) {
		currentTower = actorFactory.loadTower(type);
		currentTower.setPosition(0, 0);
		actorGroups.getTowerGroup().addActor(currentTower);
		currentTower.setVisible(false);
	}

	/**
	 * Move the tower that is still being placed (not an active tower)
	 * 
	 * @param clickCoords - Where to move the tower to
	 */
	public void moveTower(Vector2 clickCoords) {
		if (currentTower != null) {
			currentTower.setVisible(true);
			currentTower.setPositionCenter(clickCoords);
			currentTower.setShowRange(true);
			if (towerCollides()) {
				currentTower.setRangeColor(1f, 0f, 0f, 0.75f);// Red

			} else {
				currentTower.setRangeColor(1f, 1f, 1f, 0.75f);
			}
		}
	}

	/**
	 * Rotates the tower
	 * 
	 * @param rotation - amount to rotate
	 */
	public void rotateTower(float rotation) {
		if (currentTower != null) {
			currentTower.setRotation(currentTower.getRotation() - rotation);
			if (towerCollides()) {
				currentTower.setRangeColor(1f, 0f, 0f, 0.75f);// Red

			} else {
				currentTower.setRangeColor(1f, 1f, 1f, 0.75f);
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
				currentTower.setActive(true);
				currentTower.setDead(false);
				HealthBar healthBar = actorFactory.loadHealthBar();
				healthBar.setActor(currentTower);
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
		if (isCurrentTower()) {
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
		return (currentTower != null);
	}

	public Tower getCurrentTower() {
		return currentTower;
	}

}