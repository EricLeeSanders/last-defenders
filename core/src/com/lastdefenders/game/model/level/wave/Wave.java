package com.lastdefenders.game.model.level.wave;

import com.lastdefenders.game.model.level.SpawningEnemy;

/**
 * A wave of enemies that a {@link com.lastdefenders.game.model.level.Level} spawns from.
 *
 * Implementations decide when the underlying {@link SpawningEnemy} actors are created.
 * File based waves are small enough to build up front, while dynamically generated waves
 * create each enemy on demand so that a high wave doesn't allocate thousands of actors
 * in a single frame.
 *
 * @author Eric
 */
public interface Wave {

    /**
     * @return true if there are enemies left to spawn in this wave
     */
    boolean hasNextEnemy();

    /**
     * Creates/retrieves the next enemy to spawn and removes it from this wave.
     *
     * @return the next enemy to spawn
     * @throws java.util.NoSuchElementException if the wave has no enemies left. Callers are
     * expected to check {@link #hasNextEnemy()} first.
     */
    SpawningEnemy nextEnemy();

    /**
     * @return how many enemies are still waiting to spawn
     */
    int getRemainingEnemyCount();
}
