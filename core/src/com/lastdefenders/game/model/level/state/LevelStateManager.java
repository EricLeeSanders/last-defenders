package com.lastdefenders.game.model.level.state;

import com.lastdefenders.game.model.level.state.LevelStateManager.LevelState;
import com.lastdefenders.state.ObservableStateManager;

/**
 * State Manager class that contains various states that the Level can be in
 *
 * @author Eric
 */
public class LevelStateManager extends ObservableStateManager<LevelState, LevelStateObserver> {


    public LevelStateManager() {

        setState(LevelState.STANDBY);
    }

    protected void notifyObserver(LevelStateObserver observer, LevelState state) {

        observer.stateChange(state);
    }


    public enum LevelState {
        WAVE_IN_PROGRESS,
        STANDBY,
        GAME_OVER
    }
}
