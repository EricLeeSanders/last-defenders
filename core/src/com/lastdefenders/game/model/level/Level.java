package com.lastdefenders.game.model.level;

import com.lastdefenders.game.model.actor.groups.ActorGroups;
import com.lastdefenders.game.model.level.wave.Wave;
import com.lastdefenders.game.model.level.wave.WaveLoader;
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
    private Wave currentWaveEnemies;
    private LevelName activeLevel;
    private WaveLoader waveLoader;
    private ActorGroups actorGroups;

    public Level(LevelName activeLevel, ActorGroups actorGroups, WaveLoader waveLoader) {
        this.activeLevel = activeLevel;
        this.actorGroups = actorGroups;
        this.waveLoader = waveLoader;
    }

    /**
     * Spwan enemies
     */
    public void update(float delta) {

        if (currentWaveEnemies.hasNextEnemy()) {
            delayCount += delta;
            if (delayCount >= enemyDelay) {
                spawnNextEnemy();
            }
        }
    }

    private void spawnNextEnemy() {

        Logger.info("Level: Spawning Enemy");

        delayCount = 0;

        SpawningEnemy spawningEnemy = currentWaveEnemies.nextEnemy();
        actorGroups.getEnemyGroup().addActor(spawningEnemy.getEnemy());

        spawningEnemy.getEnemy().ready();

        enemyDelay = spawningEnemy.getSpawnDelay();

        spawningEnemy.free();
    }

    /**
     * Loads the next wave using the configured wave loader.
     * The loader handles the transition between different wave generation methods automatically.
     */
    public void loadNextWave() {
        currentWave++;

        Logger.info("Level: Loading wave " + currentWave);
        currentWaveEnemies = waveLoader.loadWave(activeLevel, currentWave);

        delayCount = 0;
        enemyDelay = 0;
    }

    /**
     * Fails rather than reporting zero when no wave has been loaded. GameStage treats a count
     * of zero as the wave being over, so a default would award the wave-over money and roll
     * straight into the next wave instead of surfacing the missing loadNextWave() call.
     */
    public int getSpawningEnemiesCount() {

        return currentWaveEnemies.getRemainingEnemyCount();
    }

    public int getCurrentWave() {

        return currentWave;
    }

    public LevelName getActiveLevel(){
        return activeLevel;
    }

}
