package com.foxholedefense.state;

import com.foxholedefense.state.GameStateManager.GameState;

/**
 * An interface that extends the State Observer with no additional
 * methods. This is done so that a class can implement multiple
 * state observers.
 */

public interface GameStateObserver extends StateObserver {
    void stateChange(GameState state);
}
