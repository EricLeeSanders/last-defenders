package com.lastdefenders.game.model.level.wave;

import com.badlogic.gdx.utils.Queue;
import com.lastdefenders.game.model.level.Level;
import com.lastdefenders.game.model.level.SpawningEnemy;
import com.lastdefenders.game.model.level.wave.impl.DynamicWaveLoader;
import com.lastdefenders.game.model.level.wave.impl.FileWaveLoader;
import com.lastdefenders.game.model.level.wave.impl.PreloadedWave;
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
public class HybridWaveLoaderStrategy implements WaveLoader {

    private final FileWaveLoader fileWaveLoader;
    private final DynamicWaveLoader dynamicWaveLoader;
    private boolean hasTransitioned = false;

    public HybridWaveLoaderStrategy(FileWaveLoader fileWaveLoader, DynamicWaveLoader dynamicWaveLoader) {
        this.fileWaveLoader = fileWaveLoader;
        this.dynamicWaveLoader = dynamicWaveLoader;
    }

    @Override
    public Wave loadWave(LevelName levelName, int waveNumber) {
        // Use file-based waves for waves 1-100
        if (waveNumber <= Level.FILE_WAVE_LIMIT) {
            PreloadedWave wave = fileWaveLoader.loadWave(levelName, waveNumber);

            // On the last file wave, prepare the dynamic loader with this wave as a seed.
            // Snapshotting every file wave would allocate a snapshot per enemy on every wave
            // transition to keep a seed that only the last wave ever provides.
            if (waveNumber == Level.FILE_WAVE_LIMIT) {
                seedDynamicLoader(wave, waveNumber);
            }

            return wave;
        }

        // Transition to dynamic waves for waves 101+
        if (!hasTransitioned) {
            Logger.info("HybridWaveLoaderStrategy: Transitioning to dynamic wave generation");
            hasTransitioned = true;
        }

        if (!dynamicWaveLoader.isInitialized()) {
            // Waves are requested in order, so wave FILE_WAVE_LIMIT has already been through
            // seedDynamicLoader by now. Getting here means it had no enemies to seed from.
            throw new IllegalStateException("HybridWaveLoaderStrategy: Cannot generate wave "
                + waveNumber + ". " + levelName + " wave " + Level.FILE_WAVE_LIMIT
                + " contains no enemies to seed from.");
        }

        return dynamicWaveLoader.loadWave(levelName, waveNumber);
    }

    /**
     * Seeds the dynamic loader from a wave that is about to be played.
     *
     * An empty wave is logged and left alone rather than throwing here, since this runs
     * mid-game and the loader is not needed until the first dynamic wave. loadWave reports it
     * then, by which point there is actually something to fail.
     */
    private void seedDynamicLoader(PreloadedWave wave, int waveNumber) {

        Queue<SpawningEnemy> pendingEnemies = wave.getPendingEnemies();

        if (pendingEnemies.size == 0) {
            Logger.info("HybridWaveLoaderStrategy: Wave " + waveNumber
                + " is empty. Deferring dynamic seeding.");
            return;
        }

        dynamicWaveLoader.initializeFromSeedWave(pendingEnemies, waveNumber);
    }

}
