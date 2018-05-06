package com.lastdefenders.game.tutorial.state;

import com.lastdefenders.game.tutorial.state.TutorialStateManager.TutorialState;
import com.lastdefenders.state.StateObserver;

/**
 * Created by Eric on 5/6/2018.
 */

/**
 * An interface that extends the State Observer with no additional
 * methods. This is done so that a class can implement multiple
 * state observers.
 */

public interface TutorialStateObserver extends StateObserver {

    void stateChange(TutorialState state);
}
