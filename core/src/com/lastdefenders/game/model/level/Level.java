package com.lastdefenders.game.model.level;

import com.badlogic.gdx.utils.Queue;
import com.lastdefenders.game.model.actor.groups.ActorGroups;
import com.lastdefenders.game.model.level.wave.WaveLoader;
import com.lastdefenders.game.model.level.wave.impl.DynamicWaveLoader;
import com.lastdefenders.game.model.level.wave.impl.FileWaveLoader;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.util.Logger;

public class Level {

    public static final int WAVE_LEVEL_WIN_LIMIT = 20;
    static final int FILE_WAVE_LIMIT = 100;

    private float delayCount = 0;
    private float enemyDelay = 0f;
    private int currentWave = 0;
    private Queue<SpawningEnemy> spawningEnemyQueue;
    private LevelName activeLevel;
    private WaveLoader waveLoader;
    private DynamicWaveLoader dynamicWaveLoader;
    private ActorGroups actorGroups;

    public Level(LevelName activeLevel, ActorGroups actorGroups, FileWaveLoader fileWaveLoader,
        DynamicWaveLoader dynamicWaveLoader) {

        this.activeLevel = activeLevel;
        this.actorGroups = actorGroups;
        this.waveLoader = fileWaveLoader;
        this.dynamicWaveLoader = dynamicWaveLoader;
    }

    /**
     * Spwan enemies
     */
    public void update(float delta) {

        if (spawningEnemyQueue.size > 0) {
            delayCount += delta;
            if (delayCount >= enemyDelay) {
                spawnNextEnemy();
            }
        }
    }

    private void spawnNextEnemy() {

        Logger.info("Level: Spawning Enemy");

        delayCount = 0;

        SpawningEnemy spawningEnemy = spawningEnemyQueue.removeFirst();
        actorGroups.getEnemyGroup().addActor(spawningEnemy.getEnemy());

        spawningEnemy.getEnemy().init();

        enemyDelay = spawningEnemy.getSpawnDelay();

        spawningEnemy.free();
    }

    /**
     * Loads the wave
     */
    public void loadNextWave() {

        currentWave++;

        //Switch the wave loader when we reach MAX_WAVES
        if (currentWave == FILE_WAVE_LIMIT + 1) {
            Logger.info("Level: Switching to DynamicWaveLoader");
            waveLoader = dynamicWaveLoader;
        }
        spawningEnemyQueue = waveLoader.loadWave(activeLevel, currentWave);

        // Prep the dynamicWaveLoader when we are on the last file wave
        if(currentWave == FILE_WAVE_LIMIT){

            dynamicWaveLoader.loadCurrentWaveQueue(spawningEnemyQueue);
        }

        delayCount = 0;
        enemyDelay = 0;
    }

    public int getSpawningEnemiesCount() {

        return spawningEnemyQueue.size;
    }

    public Queue<SpawningEnemy> getSpawningEnemyQueue(){
        return spawningEnemyQueue;
    }

    public int getCurrentWave() {

        return currentWave;
    }

    public LevelName getActiveLevel(){
        return activeLevel;
    }

}
