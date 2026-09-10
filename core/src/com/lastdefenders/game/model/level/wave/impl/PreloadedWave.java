package com.lastdefenders.game.model.level.wave.impl;

import com.badlogic.gdx.utils.Queue;
import com.lastdefenders.game.model.level.SpawningEnemy;
import com.lastdefenders.game.model.level.wave.Wave;

/**
 * A wave whose enemies are all created up front and held in a queue.
 *
 * Used for the hand authored wave files, which are small enough that building them
 * in one pass is not a concern.
 *
 * @author Eric
 */
public class PreloadedWave implements Wave {

    private final Queue<SpawningEnemy> spawningEnemies;

    public PreloadedWave(Queue<SpawningEnemy> spawningEnemies) {

        this.spawningEnemies = spawningEnemies;
    }

    @Override
    public boolean hasNextEnemy() {

        return spawningEnemies.size > 0;
    }

    @Override
    public SpawningEnemy nextEnemy() {

        return spawningEnemies.removeFirst();
    }

    @Override
    public int getRemainingEnemyCount() {

        return spawningEnemies.size;
    }

    /**
     * The enemies still waiting to spawn. Only a preloaded wave can report this, which is
     * why the dynamic loader seeds from this type rather than from the Wave interface.
     */
    public Queue<SpawningEnemy> getPendingEnemies() {

        return spawningEnemies;
    }
}
