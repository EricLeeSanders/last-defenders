package com.lastdefenders.game.model.level.wave.impl;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.lastdefenders.game.model.level.Map;
import com.lastdefenders.game.model.level.SpawningEnemy;
import com.lastdefenders.game.service.factory.CombatActorFactory;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.util.Logger;

/**
 * Created by Eric on 5/25/2017.
 */

public class DynamicWaveLoader extends AbstractWaveLoader {

    private Array<SpawningEnemySnapshot> waveHistory;

    public DynamicWaveLoader(CombatActorFactory combatActorFactory, Map map) {

        super(combatActorFactory, map);
    }

    @Override
    public Queue<SpawningEnemy> loadWave(LevelName levelName, int wave) {

        Logger.info("DynamicWaveGenerator: Generating Wave: " + wave);

        if(waveHistory == null || waveHistory.size == 0){
            throw new IllegalStateException("DynamicWaveGenerator: currentWave must be loaded.");
        }

        /*
        Create Spawning Enemies from the waveHistory. Create a duplicate and load a duplicate so that
        the waves are constantly doubling.
         */
        Array<SpawningEnemySnapshot> duplicatedSnapshots = new Array<>();
        Queue<SpawningEnemy> enemyQueue = new Queue<>();
        waveHistory.shuffle();

        for(SpawningEnemySnapshot snapshot : waveHistory){
            SpawningEnemy spawningEnemy = loadSpawningEnemy(snapshot.getName(), snapshot.hasArmor(), snapshot.getSpawnDelay());
            SpawningEnemy spawningEnemyDouble = loadSpawningEnemy(snapshot.getName(), snapshot.hasArmor(), snapshot.getSpawnDelay());
            enemyQueue.addFirst(spawningEnemy);
            enemyQueue.addFirst(spawningEnemyDouble);

            // Create new snapshot to duplicate the wave history
            SpawningEnemySnapshot duplicateSnapshot = new SpawningEnemySnapshot(spawningEnemy);
            duplicatedSnapshots.add(duplicateSnapshot);
        }

        waveHistory.addAll(duplicatedSnapshots);
        return enemyQueue;


    }

    /**
     * Loads the current wave into the Wave Loader. This is required to be done before the Level switches
     * to the DynamicWaveLoader.
     * @param queue
     */
    public void loadCurrentWaveQueue( Queue<SpawningEnemy>  queue){

        Array<SpawningEnemySnapshot> currentWaveSnapshot = new Array<>();

        /*
        Load the current queue and create snapshots of the spawning enemies.
         */
        for(SpawningEnemy spawningEnemy : queue){
            SpawningEnemySnapshot snapshot = new SpawningEnemySnapshot(spawningEnemy);
            currentWaveSnapshot.add(snapshot);
        }

        this.waveHistory = currentWaveSnapshot;
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

