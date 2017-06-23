package com.foxholedefense.game.model.level.wave.impl;

import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.level.Map;
import com.foxholedefense.game.model.level.SpawningEnemy;
import com.foxholedefense.game.model.level.wave.WaveLoader;
import com.foxholedefense.game.service.factory.CombatActorFactory;

/**
 * Created by Eric on 5/25/2017.
 */

abstract class AbstractWaveLoader implements WaveLoader {
    private CombatActorFactory combatActorFactory;
    private Map map;

    AbstractWaveLoader(CombatActorFactory combatActorFactory, Map map){

        this.combatActorFactory = combatActorFactory;
        this.map = map;
    }

    SpawningEnemy loadSpawningEnemy(String type, boolean hasArmor, float spawnDelay){

        Enemy enemy = combatActorFactory.loadEnemy(type);

        enemy.setPath(map.getPath());
        enemy.setHasArmor(hasArmor);

        return combatActorFactory.loadSpawningEnemy(enemy, spawnDelay);

    }
}

