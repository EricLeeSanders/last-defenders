package com.lastdefenders.game.service.actorplacement;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.game.helper.CollisionDetection;
import com.lastdefenders.game.model.actor.combat.tower.state.states.TowerStateEnum;
import com.lastdefenders.game.model.actor.groups.ActorGroups;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.level.Map;
import com.lastdefenders.game.service.factory.CombatActorFactory;
import com.lastdefenders.game.service.factory.HealthFactory;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * Responsible for placing the Tower on the Stage
 *
 * @author Eric
 */
public class TowerPlacement {

    private Tower currentTower;
    private ActorGroups actorGroups;
    private Map map;
    private CombatActorFactory combatActorFactory;
    private HealthFactory healthFactory;

    public TowerPlacement(Map map, ActorGroups actorGroups, CombatActorFactory combatActorFactory,
        HealthFactory healthFactory) {

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
        currentTower = combatActorFactory.loadTower(type, true);
        currentTower.initialize();
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


        if (currentTower != null) {
            if (!towerCollides()) {
                currentTower.ready();
                return true;
            } else {

                //TODO this is here mostly for testing. Can probably be removed for production
                SnapshotArray<Actor> towers = actorGroups.getTowerGroup().getChildren();

                if (CollisionDetection.collisionWithPath(map.getPathBoundaries(), currentTower)) {
                    //Logger.info("TowerPlacement: tower collides with path");
                } else if (CollisionDetection.collisionWithActors(towers, currentTower)) {
                    //Logger.info("TowerPlacement: tower collides with another Actor");
                } else if(CollisionDetection.outOfMapBoundary(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT, currentTower)){
                    //Logger.info("TowerPlacement: tower is out of bounds");
                }
            }
        } else {
            Logger.error("TowerPlacement: current tower is null!");
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

        return CollisionDetection.collisionWithPath(map.getPathBoundaries(), currentTower) ||
                CollisionDetection.collisionWithActors(towers, currentTower) ||
                CollisionDetection.outOfMapBoundary(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT, currentTower);


    }


    /**
     * Remove the current tower.
     *
     * @param free - if the tower should be freed as well
     */
    public void removeCurrentTower(boolean free) {

        Logger.info("TowerPlacement: removing tower");
        if (isCurrentTower()) {
            if (free) {
                currentTower.freeActor();
            }
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
