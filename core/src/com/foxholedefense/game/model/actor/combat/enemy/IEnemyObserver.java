package com.foxholedefense.game.model.actor.combat.enemy;

/**
 * Created by Eric on 2/4/2017.
 */

public interface IEnemyObserver {

    public void notifyEnemy(Enemy enemy, EnemyEvent event);

    public enum EnemyEvent {
        REACHED_END;
    }
}
