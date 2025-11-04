package com.lastdefenders.game.model.level;

import com.badlogic.gdx.utils.Queue;
import com.lastdefenders.game.model.actor.groups.ActorGroups;
import com.lastdefenders.game.model.level.wave.WaveLoaderStrategy;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.util.Logger;

/**
 * Represents a game level that manages wave spawning and progression.
 *
 * @author Eric
 */
public class Level {

    public static final int WAVE_LEVEL_WIN_LIMIT = 20;
    public static final int FILE_WAVE_LIMIT = 100;

    private float delayCount = 0;
    private float enemyDelay = 0f;
    private int currentWave = 0;
    private Queue<SpawningEnemy> spawningEnemyQueue;
    private LevelName activeLevel;
    private WaveLoaderStrategy waveLoaderStrategy;
    private ActorGroups actorGroups;

    public Level(LevelName activeLevel, ActorGroups actorGroups, WaveLoaderStrategy waveLoaderStrategy) {
        this.activeLevel = activeLevel;
        this.actorGroups = actorGroups;
        this.waveLoaderStrategy = waveLoaderStrategy;
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

        spawningEnemy.getEnemy().ready();

        enemyDelay = spawningEnemy.getSpawnDelay();

        spawningEnemy.free();
    }

    /**
     * Loads the next wave using the configured wave loader strategy.
     * The strategy handles the transition between different wave generation methods automatically.
     */
    public void loadNextWave() {
        currentWave++;

        Logger.info("Level: Loading wave " + currentWave);
        spawningEnemyQueue = waveLoaderStrategy.loadWave(activeLevel, currentWave);

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
