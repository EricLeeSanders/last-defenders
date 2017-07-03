package com.foxholedefense.game.model.level.wave;

import com.badlogic.gdx.utils.Queue;
import com.foxholedefense.game.model.level.SpawningEnemy;

/**
 * Created by Eric on 5/25/2017.
 */

public interface WaveLoader {

    Queue<SpawningEnemy> loadWave(int level, int wave);
}
