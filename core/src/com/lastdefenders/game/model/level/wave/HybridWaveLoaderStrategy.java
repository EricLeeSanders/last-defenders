package com.lastdefenders.game.model.level.wave;

import com.badlogic.gdx.utils.Queue;
import com.lastdefenders.game.model.level.Level;
import com.lastdefenders.game.model.level.SpawningEnemy;
import com.lastdefenders.game.model.level.wave.impl.DynamicWaveLoader;
import com.lastdefenders.game.model.level.wave.impl.FileWaveLoader;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.util.Logger;

/**
 * Hybrid wave loading strategy that uses file-based waves for waves 1-100,
 * then transitions to dynamically generated waves for waves 101+.
 *
 * The transition happens automatically and transparently to the caller.
 *
 * @author Eric
 */
public class HybridWaveLoaderStrategy implements WaveLoaderStrategy {

    private final FileWaveLoader fileWaveLoader;
    private final DynamicWaveLoader dynamicWaveLoader;
    private boolean hasTransitioned = false;

    public HybridWaveLoaderStrategy(FileWaveLoader fileWaveLoader, DynamicWaveLoader dynamicWaveLoader) {
        this.fileWaveLoader = fileWaveLoader;
        this.dynamicWaveLoader = dynamicWaveLoader;
    }

    @Override
    public Queue<SpawningEnemy> loadWave(LevelName levelName, int waveNumber) {
        // Use file-based waves for waves 1-100
        if (waveNumber <= Level.FILE_WAVE_LIMIT) {
            Queue<SpawningEnemy> wave = fileWaveLoader.loadWave(levelName, waveNumber);

            // On wave 100, prepare the dynamic loader with this wave as a seed
            if (waveNumber == Level.FILE_WAVE_LIMIT) {
                Logger.info("HybridWaveLoaderStrategy: Preparing dynamic wave generator");
                dynamicWaveLoader.initializeFromSeedWave(wave);
            }

            return wave;
        }

        // Transition to dynamic waves for waves 101+
        if (!hasTransitioned) {
            Logger.info("HybridWaveLoaderStrategy: Transitioning to dynamic wave generation");
            hasTransitioned = true;
        }

        return dynamicWaveLoader.loadWave(levelName, waveNumber);
    }
}
