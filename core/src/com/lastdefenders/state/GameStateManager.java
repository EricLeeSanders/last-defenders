package com.lastdefenders.state;

import com.lastdefenders.state.GameStateManager.GameState;

/**
 * Game State Manager class that manages the state of the game
 *
 * @author Eric
 */
public class GameStateManager extends ObservableStateManager<GameState, GameStateObserver> {

    public GameStateManager() {

        setState(GameState.PLAY);
    }

    @Override
    protected void notifyObserver(GameStateObserver observer, GameState state) {

        observer.stateChange(state);
    }

    public enum GameState {
        PLAY,
        PAUSE,
        QUIT
    }
}
