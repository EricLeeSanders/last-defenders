package com.foxholedefense.game.model.level.state;

import com.foxholedefense.game.model.level.state.LevelStateManager.LevelState;
import com.foxholedefense.state.StateObserver;


/**
 * An interface that extends the State Observer with no additional
 * methods. This is done so that a class can implement multiple
 * state observers.
 */

public interface LevelStateObserver extends StateObserver {
    void stateChange(LevelState state);
}
