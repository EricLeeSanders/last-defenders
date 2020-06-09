package com.lastdefenders.game.model.actor.combat.enemy.state;

import com.lastdefenders.game.model.actor.combat.enemy.Enemy;

public interface EnemyStateObserver {
    void stateChange(EnemyStateEnum state, Enemy enemy);
}
