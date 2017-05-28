package com.foxholedefense.game.model.level.wave.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Queue;
import com.foxholedefense.game.model.level.Map;
import com.foxholedefense.game.model.level.SpawningEnemy;
import com.foxholedefense.game.service.factory.CombatActorFactory;


/**
 * Created by Eric on 5/25/2017.
 */

public class FileWaveLoader extends AbstractWaveLoader {

    public FileWaveLoader(CombatActorFactory combatActorFactory, Map map) {
        super(combatActorFactory, map);
    }

    @Override
    public Queue<SpawningEnemy> loadWave(int level, int wave) {

        Queue<SpawningEnemy> spawningEnemies = new Queue<SpawningEnemy>();

        JsonValue json = new JsonReader().parse(Gdx.files.internal("game/levels/level" + level + "/waves/wave" + wave + ".json"));
        JsonValue enemiesJson = json.get("wave");

        for (JsonValue enemyJson : enemiesJson.iterator()) {

            String type = enemyJson.getString("enemy");
            boolean armor = enemyJson.getBoolean("armor");
            float spawnDelay = enemyJson.getFloat("delay");

            SpawningEnemy spawningEnemy = loadSpawningEnemy(type, armor, spawnDelay);

            spawningEnemies.addFirst(spawningEnemy);

        }

        return spawningEnemies;
    }
}
