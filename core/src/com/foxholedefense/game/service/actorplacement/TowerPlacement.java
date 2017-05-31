package com.foxholedefense.game.service.actorplacement;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.helper.CollisionDetection;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.health.ArmorIcon;
import com.foxholedefense.game.model.actor.health.HealthBar;
import com.foxholedefense.game.model.actor.interfaces.IRotatable;
import com.foxholedefense.game.model.level.Map;
import com.foxholedefense.game.service.factory.CombatActorFactory;
import com.foxholedefense.game.service.factory.HealthFactory;
import com.foxholedefense.util.Logger;

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
	private CombatActorFactory combatActorFactory;
	private HealthFactory healthFactory;

	public TowerPlacement(Map map, ActorGroups actorGroups, CombatActorFactory combatActorFactory, HealthFactory healthFactory) {
		this.map = map;
		this.actorGroups = actorGroups;
		this.combatActorFactory = combatActorFactory;
		this.healthFactory = healthFactory;
	}

	/**
	 * Create a tower
	 * 
	 * @param type - Type of tower
	 */
	public void createTower(String type) {
		Logger.info("TowerPlacement: creating tower: " + type);
		currentTower = combatActorFactory.loadTower(type);
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
			currentTower.setTowerColliding(towerCollides());
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
			currentTower.setTowerColliding(towerCollides());
		}
	}

	/**
	 * Place the tower and make it active
	 * 
	 * @return boolean - if Tower was successfully placed
	 */
	public boolean placeTower() {
		Logger.info("TowerPlacement: trying to place tower");
		if (currentTower != null) {
			if (!towerCollides()) {
				currentTower.init();
				HealthBar healthBar = healthFactory.loadHealthBar();
				healthBar.setActor(currentTower);
				ArmorIcon armorIcon = healthFactory.loadArmorIcon();
				armorIcon.setActor(currentTower);
				currentTower = null;
				Logger.info("TowerPlacement: placing tower");
				return true;
			} else {

				//TODO this is here mostly for testing. Can probably be removed for production
				SnapshotArray<Actor> towers = actorGroups.getTowerGroup().getChildren();

				if (CollisionDetection.collisionWithPath(map.getPathBoundaries(), currentTower)) {
					Logger.info("TowerPlacement: tower collides with path");
				} else if (CollisionDetection.collisionWithActors(towers, currentTower)) {
					Logger.info("TowerPlacement: tower collides with another Actor");
				}
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

		if (CollisionDetection.collisionWithPath(map.getPathBoundaries(), currentTower)) {
			return true;
		} else if (CollisionDetection.collisionWithActors(towers, currentTower)) {
			return true;
		}
		return false;

	}

	/**
	 * Remove the current tower
	 */
	public void removeCurrentTower() {
		Logger.info("TowerPlacement: removing tower");
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
		return currentTower != null;
	}

	public Tower getCurrentTower() {
		return currentTower;
	}

}
