package com.lastdefenders.game.model.level;

import com.badlogic.gdx.utils.Queue;
import com.lastdefenders.game.model.actor.ActorGroups;
import com.lastdefenders.game.model.actor.health.ArmorIcon;
import com.lastdefenders.game.model.actor.health.HealthBar;
import com.lastdefenders.game.model.level.wave.WaveLoader;
import com.lastdefenders.game.model.level.wave.impl.DynamicWaveLoader;
import com.lastdefenders.game.model.level.wave.impl.FileWaveLoader;
import com.lastdefenders.game.service.factory.HealthFactory;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.util.Logger;

public class Level {

    public static final int MAX_WAVES = 20;

    private float delayCount = 0;
    private float enemyDelay = 0f;
    private int currentWave = 0;
    private Queue<SpawningEnemy> spawningEnemyQueue;
    private LevelName activeLevel;
    private WaveLoader waveLoader;
    private DynamicWaveLoader dynamicWaveLoader;
    private ActorGroups actorGroups;
    private HealthFactory healthFactory;

    public Level(LevelName activeLevel, ActorGroups actorGroups, HealthFactory healthFactory,
        FileWaveLoader fileWaveLoader, DynamicWaveLoader dynamicWaveLoader) {

        this.activeLevel = activeLevel;
        this.actorGroups = actorGroups;
        this.healthFactory = healthFactory;
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

        HealthBar healthBar = healthFactory.loadHealthBar();
        healthBar.setActor(spawningEnemy.getEnemy());
        ArmorIcon armorIcon = healthFactory.loadArmorIcon();
        armorIcon.setActor(spawningEnemy.getEnemy());

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
        if (currentWave == MAX_WAVES + 1) {
            waveLoader = dynamicWaveLoader;
        }
        spawningEnemyQueue = waveLoader.loadWave(activeLevel, currentWave);
        delayCount = 0;
        enemyDelay = 0;

        // Once we reach the MAX WAVES, initialize the dynamic wave loader
        if (currentWave == MAX_WAVES) {
            dynamicWaveLoader.initDynamicWaveLoader(spawningEnemyQueue);
        }
    }

    public int getSpawningEnemiesCount() {

        return spawningEnemyQueue.size;
    }

    public int getCurrentWave() {

        return currentWave;
    }

    public LevelName getActiveLevel(){
        return activeLevel;
    }

}
