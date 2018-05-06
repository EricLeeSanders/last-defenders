package com.lastdefenders.game.tutorial.state;

import com.lastdefenders.game.tutorial.state.TutorialStateManager.TutorialState;
import com.lastdefenders.state.ObservableStateManager;

/**
 * Created by Eric on 5/6/2018.
 */

public class TutorialStateManager extends ObservableStateManager<TutorialState, TutorialStateObserver>{

    @Override
    protected void notifyObserver(TutorialStateObserver observer, TutorialState state) {
        observer.stateChange(state);
    }

    public enum TutorialState {
        PLACE_FIRST_SOLDIER;
    }
}
