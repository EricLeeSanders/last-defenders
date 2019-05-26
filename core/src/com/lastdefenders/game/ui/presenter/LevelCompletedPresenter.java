package com.lastdefenders.game.ui.presenter;

import com.lastdefenders.game.model.level.state.LevelStateManager;
import com.lastdefenders.game.model.level.state.LevelStateManager.LevelState;
import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.game.ui.state.GameUIStateManager.GameUIState;
import com.lastdefenders.game.ui.state.GameUIStateObserver;
import com.lastdefenders.game.ui.view.interfaces.ILevelCompletedView;
import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.LDAudio.LDSound;
import com.lastdefenders.util.Logger;

public class LevelCompletedPresenter implements GameUIStateObserver {

    private ILevelCompletedView view;
    private ScreenChanger screenChanger;
    private GameUIStateManager uiStateManager;
    private LevelStateManager levelStateManager;
    private LDAudio audio;

    public LevelCompletedPresenter(GameUIStateManager uiStateManager, LevelStateManager levelStateManager,
        ScreenChanger screenChanger, LDAudio audio) {

        this.screenChanger = screenChanger;
        this.uiStateManager = uiStateManager;
        this.levelStateManager = levelStateManager;
        this.audio = audio;

        uiStateManager.attach(this);
    }

    /**
     * Set the Level Completed view
     */
    public void setView(ILevelCompletedView view) {

        this.view = view;
        stateChange(uiStateManager.getState());
    }

    /**
     * Change to level select
     */
    public void levelSelect() {

        if (canChangeToLevelSelect()) {
            Logger.info("Level Completed Presenter: level select");
            audio.playSound(LDSound.SMALL_CLICK);
            screenChanger.changeToLevelSelect();
        }
    }

    /**
     * Change to main menu
     */
    public void mainMenu() {

        if (canChangeToMainMenu()) {
            Logger.info("Level Completed Presenter: main menu");
            audio.playSound(LDSound.SMALL_CLICK);
            screenChanger.changeToMenu();
        }
    }

    /**
     * Continue the level
     */
    public void continueLevel() {

        if (canContinueLevel()) {
            Logger.info("Level Completed Presenter: continue level");
            audio.playSound(LDSound.SMALL_CLICK);
            uiStateManager.setState(GameUIState.STANDBY);
            levelStateManager.setState(LevelState.STANDBY);
        }
    }

    private boolean canChangeToLevelSelect() {

        return uiStateManager.getState().equals(GameUIState.LEVEL_COMPLETED);
    }

    private boolean canChangeToMainMenu() {

        return uiStateManager.getState().equals(GameUIState.LEVEL_COMPLETED);
    }

    private boolean canContinueLevel() {

        return uiStateManager.getState().equals(GameUIState.LEVEL_COMPLETED);
    }

    @Override
    public void stateChange(GameUIState state) {

        switch (state) {
            case LEVEL_COMPLETED:
                view.levelCompletedState();
                break;
            default:
                view.standByState();
                break;
        }

    }

}
