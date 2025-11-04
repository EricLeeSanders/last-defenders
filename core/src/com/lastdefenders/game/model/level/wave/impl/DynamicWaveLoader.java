package com.lastdefenders.game.model.level.wave.impl;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.lastdefenders.game.model.level.Level;
import com.lastdefenders.game.model.level.Map;
import com.lastdefenders.game.model.level.SpawningEnemy;
import com.lastdefenders.game.service.factory.CombatActorFactory;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.util.Logger;

/**
 * Dynamically generates waves based on a seed wave pattern.
 * Uses linear difficulty scaling instead of exponential growth.
 *
 * Difficulty increases by 10% per wave after wave 100.
 * For example:
 * - Wave 101: 110% of base difficulty
 * - Wave 110: 200% of base difficulty
 * - Wave 120: 300% of base difficulty
 *
 * @author Eric
 */
public class DynamicWaveLoader extends AbstractWaveLoader {

    private static final float DIFFICULTY_INCREASE_PER_WAVE = 0.10f; // 10% increase per wave
    private Array<SpawningEnemySnapshot> seedWavePattern;

    public DynamicWaveLoader(CombatActorFactory combatActorFactory, Map map) {
        super(combatActorFactory, map);
    }

    @Override
    public Queue<SpawningEnemy> loadWave(LevelName levelName, int wave) {
        Logger.info("DynamicWaveLoader: Generating Wave " + wave);

        if(seedWavePattern == null || seedWavePattern.size == 0){
            throw new IllegalStateException("DynamicWaveLoader: Must be initialized with a seed wave first. Call initializeFromSeedWave().");
        }

        // Calculate difficulty scaling based on waves beyond FILE_WAVE_LIMIT (100)
        int wavesAboveLimit = wave - Level.FILE_WAVE_LIMIT;
        float difficultyMultiplier = 1.0f + (wavesAboveLimit * DIFFICULTY_INCREASE_PER_WAVE);

        // Calculate how many enemies to spawn
        int baseEnemyCount = seedWavePattern.size;
        int scaledEnemyCount = Math.round(baseEnemyCount * difficultyMultiplier);

        Logger.info("DynamicWaveLoader: Base enemies: " + baseEnemyCount +
                    ", Scaled enemies: " + scaledEnemyCount +
                    " (difficulty multiplier: " + String.format("%.2f", difficultyMultiplier) + "x)");

        // Generate enemy queue with scaled difficulty
        Queue<SpawningEnemy> enemyQueue = new Queue<>();
        Array<SpawningEnemySnapshot> shuffledPattern = new Array<>(seedWavePattern);
        shuffledPattern.shuffle();

        for(int i = 0; i < scaledEnemyCount; i++){
            // Cycle through the shuffled pattern if we need more enemies than the pattern size
            SpawningEnemySnapshot snapshot = shuffledPattern.get(i % shuffledPattern.size);

            SpawningEnemy spawningEnemy = loadSpawningEnemy(
                snapshot.getName(),
                snapshot.hasArmor(),
                snapshot.getSpawnDelay()
            );

            enemyQueue.addLast(spawningEnemy);
        }

        return enemyQueue;
    }

    /**
     * Initializes the dynamic wave loader with a seed wave pattern.
     * This pattern will be used as the basis for all dynamically generated waves.
     * Must be called before loadWave() can be used.
     *
     * @param seedWave The wave to use as a pattern for dynamic generation
     */
    public void initializeFromSeedWave(Queue<SpawningEnemy> seedWave){
        Logger.info("DynamicWaveLoader: Initializing with seed wave of " + seedWave.size + " enemies");

        Array<SpawningEnemySnapshot> seedSnapshot = new Array<>();

        // Create snapshots of the seed wave pattern
        for(SpawningEnemy spawningEnemy : seedWave){
            SpawningEnemySnapshot snapshot = new SpawningEnemySnapshot(spawningEnemy);
            seedSnapshot.add(snapshot);
        }

        this.seedWavePattern = seedSnapshot;
    }

    /**
     * Creates a Snapshot of a SpawningEnemy. This is important because the SpawningEnemy is reset after each wave.
     */
    private class SpawningEnemySnapshot {
        private String name;
        private float spawnDelay;
        private boolean armor;

        public SpawningEnemySnapshot(SpawningEnemy spawningEnemy) {

            this.name = spawningEnemy.getEnemy().getClass().getSimpleName().split("Enemy")[1];
            this.spawnDelay = spawningEnemy.getSpawnDelay();
            this.armor = spawningEnemy.getEnemy().hasArmor();
        }

        public String getName() {

            return name;
        }

        public float getSpawnDelay() {

            return spawnDelay;
        }

        public boolean hasArmor() {

            return armor;
        }
    }


}

