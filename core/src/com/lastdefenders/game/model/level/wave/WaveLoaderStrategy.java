package com.lastdefenders.game.model.level.wave;

import com.badlogic.gdx.utils.Queue;
import com.lastdefenders.game.model.level.SpawningEnemy;
import com.lastdefenders.levelselect.LevelName;

/**
 * Strategy interface for loading waves.
 * Implementations can provide different wave loading strategies (file-based, dynamic, hybrid, etc.)
 *
 * @author Eric
 */
public interface WaveLoaderStrategy {

    /**
     * Loads a wave for the given level and wave number
     *
     * @param levelName The level to load the wave for
     * @param waveNumber The wave number to load
     * @return A queue of spawning enemies for this wave
     */
    Queue<SpawningEnemy> loadWave(LevelName levelName, int waveNumber);
}
