package com.foxholedefense.game.model.level.wave.impl;

import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.SnapshotArray;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.level.Level;
import com.foxholedefense.game.model.level.Map;
import com.foxholedefense.game.model.level.SpawningEnemy;
import com.foxholedefense.game.service.factory.CombatActorFactory;
import com.foxholedefense.util.Logger;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

/**
 * Created by Eric on 5/25/2017.
 */

public class DynamicWaveLoader extends AbstractWaveLoader {

    private Random random = new Random();
    private java.util.Map<String, Integer> enemyMap = new HashMap<>();

    public DynamicWaveLoader(CombatActorFactory combatActorFactory, Map map) {

        super(combatActorFactory, map);
    }

    /**
     * Init the Wave Loader with a a Queue of initial spawning enemies to start with
     */
    public void initDynamicWaveLoader(Queue<SpawningEnemy> initialSpawningEnemies) {

        for (SpawningEnemy spawningEnemy : initialSpawningEnemies) {
            incrementEnemyMapCount(convertEnemyClassToStringType(
                spawningEnemy.getEnemy().getClass()), 1);
        }
    }

    private String convertEnemyClassToStringType(Class<? extends Enemy> enemyClass) {

        String type = enemyClass.getSimpleName();
        // Remove the enemy from the string (i.e. EnemyRifle -> Rifle)
        type = type.replaceFirst("Enemy", "");

        return type;
    }

    @Override
    public Queue<SpawningEnemy> loadWave(int level, int wave) {

        Logger.info("DynamicWaveGenerator: Generating Wave: " + wave);

        int currentGeneratedWave = wave - Level.MAX_WAVES;

        if ((currentGeneratedWave % 3) == 0) {
            everyThirdWave();
        }

        if ((currentGeneratedWave % 4) == 0) {
            everyFourthWave();
        }
        if ((currentGeneratedWave % 6) == 0) {
            everySixthWave();
        }

        everyWave();

        return createEnemies();
    }

    private Queue<SpawningEnemy> createEnemies() {

        SnapshotArray<SpawningEnemy> enemies = new SnapshotArray<>();

        for (Entry<String, Integer> entry : enemyMap.entrySet()) {
            enemies.addAll(createEnemiesByType(entry.getValue(), entry.getKey()));
        }

        shuffle(enemies);

        Queue<SpawningEnemy> spawningEnemiesQueue = new Queue<>(enemies.size);
        for (SpawningEnemy spawningEnemy : enemies) {
            spawningEnemiesQueue.addFirst(spawningEnemy);
        }

        return spawningEnemiesQueue;
    }

    private SnapshotArray<SpawningEnemy> createEnemiesByType(int n, String type) {

        SnapshotArray<SpawningEnemy> enemies = new SnapshotArray<>();

        for (int i = 0; i < n; i++) {
            int randArmor = random.nextInt(3); //0-2
            boolean armor = (randArmor == 0);
            float randSpawnDelay = random.nextFloat() * 1.5f + 0.25f; // .25 - 1.75

            SpawningEnemy spawningEnemy = loadSpawningEnemy(type, armor, randSpawnDelay);
            enemies.add(spawningEnemy);
        }
        return enemies;
    }

    private void shuffle(SnapshotArray<SpawningEnemy> enemies) {

        int n = enemies.size;
        for (int i = 0; i < n; i++) {
            int rand = i + (int) (Math.random() * (n - i)); // between i and n-1
            swap(enemies, i, rand);
        }
    }

    private void swap(SnapshotArray<SpawningEnemy> enemies, int i, int j) {

        enemies.swap(i, j);
    }

    /**
     * Every wave add 3 rifles or
     * 3 machines guns or 2 snipers
     */
    private void everyWave() {

        int rnd = random.nextInt(3); // 0-2

        if (rnd == 0) {
            incrementEnemyMapCount("Rifle", 3);
        } else if (rnd == 1) {
            incrementEnemyMapCount("MachineGun", 3);
        } else {
            incrementEnemyMapCount("Sniper", 2);
        }
    }

    /**
     * Add a Humvee every third wave
     */
    private void everyThirdWave() {

        incrementEnemyMapCount("Humvee", 1);
    }

    /**
     * Add either 1 rocket launcher
     * or 1 flame thrower and 1 sniper
     * every fourth wave
     */
    private void everyFourthWave() {

        int rnd = random.nextInt(2); // 0 or 1
        if (rnd == 0) {
            incrementEnemyMapCount("RocketLauncher", 1);
        } else {
            incrementEnemyMapCount("FlameThrower", 1);
            incrementEnemyMapCount("Sniper", 1);
        }
    }

    /**
     * Add a Tank every sixth wave
     */
    private void everySixthWave() {

        incrementEnemyMapCount("Tank", 1);
    }

    private void incrementEnemyMapCount(String type, int amount) {

        Integer count = enemyMap.get(type);
        if (count == null) {
            count = 0;
        }
        count += amount;
        enemyMap.put(type, count);
    }
}
