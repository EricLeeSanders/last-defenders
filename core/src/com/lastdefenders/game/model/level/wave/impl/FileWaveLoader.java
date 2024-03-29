package com.lastdefenders.game.model.level.wave.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Queue;
import com.lastdefenders.game.model.level.Map;
import com.lastdefenders.game.model.level.SpawningEnemy;
import com.lastdefenders.game.service.factory.CombatActorFactory;
import com.lastdefenders.levelselect.LevelName;


/**
 * Created by Eric on 5/25/2017.
 */

public class FileWaveLoader extends AbstractWaveLoader {

    public FileWaveLoader(CombatActorFactory combatActorFactory, Map map) {

        super(combatActorFactory, map);
    }

    @Override
    public Queue<SpawningEnemy> loadWave(LevelName levelName, int wave) {

        Queue<SpawningEnemy> spawningEnemies = new Queue<>();

        JsonValue json = new JsonReader().parse(
            Gdx.files.internal("game/levels/" + levelName.toString() + "/waves/wave" + wave + ".json"));
        JsonValue enemiesJson = json.get("wave");

        for (JsonValue enemyJson : enemiesJson.iterator()) {

            String type = enemyJson.getString("name");
            boolean armor = enemyJson.getBoolean("armor");
            float spawnDelay = enemyJson.getFloat("delay");

            SpawningEnemy spawningEnemy = loadSpawningEnemy(type, armor, spawnDelay);

            spawningEnemies.addLast(spawningEnemy);

        }

        return spawningEnemies;
    }
}
