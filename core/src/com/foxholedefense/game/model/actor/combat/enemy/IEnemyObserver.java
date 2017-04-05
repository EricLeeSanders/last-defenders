package com.foxholedefense.game.model.actor.combat.enemy;

/**
 * Created by Eric on 2/4/2017.
 */

public interface IEnemyObserver {

    void notifyEnemy(Enemy enemy, EnemyEvent event);

    enum EnemyEvent {
        REACHED_END
    }
}
