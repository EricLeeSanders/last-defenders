package com.lastdefenders.game.model.level.wave;

import com.lastdefenders.levelselect.LevelName;

/**
 * Created by Eric on 5/25/2017.
 */

public interface WaveLoader {

    Wave loadWave(LevelName levelName, int wave);
}
