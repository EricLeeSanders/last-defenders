package com.foxholedefense.game.ui.state;

import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.state.StateObserver;


/**
 * An interface that extends the State Observer with no additional
 * methods. This is done so that a class can implement multiple
 * state observers.
 */

public interface GameUIStateObserver extends StateObserver {
    void stateChange(GameUIState state);

}
