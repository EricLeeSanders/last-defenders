package com.foxholedefense.game.ui.state;

import com.foxholedefense.game.model.level.state.LevelStateManager;
import com.foxholedefense.game.model.level.state.LevelStateManager.LevelState;
import com.foxholedefense.game.model.level.state.LevelStateObserver;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.state.ObservableStateManager;
import com.foxholedefense.util.Logger;

public class GameUIStateManager extends
    ObservableStateManager<GameUIState, GameUIStateObserver> implements LevelStateObserver {

    private LevelStateManager levelStateManager;

    public GameUIStateManager(LevelStateManager levelStateManager) {

        this.levelStateManager = levelStateManager;
        levelStateManager.attach(this);
        setState(GameUIState.STANDBY);
    }

    // Determine the state to return to
    public void setStateReturn() {

        syncWithLevelState();
    }

    private void syncWithLevelState() {

        Logger.info("Game UI State: syncWithLevelState");
        switch (levelStateManager.getState()) {
            case WAVE_IN_PROGRESS:
                setState(GameUIState.WAVE_IN_PROGRESS);
                break;
            case STANDBY:
            default:
                setState(GameUIState.STANDBY);
                break;
        }
    }

    @Override
    public void stateChange(LevelState state) {

        Logger.info("Game UI State: changeLevelState: " + state.name());
        switch (state) {
            case STANDBY:
                if (getState() == GameUIState.WAVE_IN_PROGRESS) {
                    setState(GameUIState.STANDBY);
                }
            default:
        }
    }

    @Override
    protected void notifyObserver(GameUIStateObserver observer, GameUIState state) {

        observer.stateChange(state);
    }

    public enum GameUIState {
        ENLISTING,
        SUPPORT,
        INSPECTING,
        HIGH_SCORES,
        PAUSE_MENU,
        OPTIONS,
        STANDBY,
        GAME_OVER,
        DEBUG,
        PLACING_TOWER,
        PLACING_SUPPORT,
        PLACING_AIRSTRIKE,
        PLACING_SUPPLYDROP,
        WAVE_IN_PROGRESS,
        LEVEL_COMPLETED

    }
}
