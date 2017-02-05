package com.foxholedefense.game.model.actor.combat.tower;

/**
 * Created by Eric on 2/1/2017.
 */

public interface ITowerObserver {

    public void notifyTower(Tower tower, TowerEvent event);

    public enum TowerEvent {
        KILLED_ENEMY;
    }
}
