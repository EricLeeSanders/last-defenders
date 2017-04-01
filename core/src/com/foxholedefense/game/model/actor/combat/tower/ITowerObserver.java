package com.foxholedefense.game.model.actor.combat.tower;

/**
 * Created by Eric on 2/1/2017.
 */

public interface ITowerObserver {

    void notifyTower(Tower tower, TowerEvent event);

    enum TowerEvent {
        KILLED_ENEMY
    }
}
