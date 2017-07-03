package com.foxholedefense.game.ui.presenter;

import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.state.GameUIStateObserver;
import com.foxholedefense.game.ui.view.interfaces.IPauseView;
import com.foxholedefense.screen.ScreenChanger;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.state.GameStateManager.GameState;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDAudio.FHDSound;
import com.foxholedefense.util.Logger;

/**
 * Created by Eric on 4/8/2017.
 */

public class PausePresenter implements GameUIStateObserver {

    private GameUIStateManager uiStateManager;
    private GameStateManager gameStateManager;
    private ScreenChanger screenChanger;
    private FHDAudio audio;
    private IPauseView view;
    private boolean keepGamePaused;

    public PausePresenter(GameUIStateManager uiStateManager, GameStateManager gameStateManager,
        ScreenChanger screenChanger, FHDAudio audio) {

        this.uiStateManager = uiStateManager;
        this.audio = audio;
        this.screenChanger = screenChanger;
        this.gameStateManager = gameStateManager;
        uiStateManager.attach(this);
    }

    public void setView(IPauseView view) {

        this.view = view;
    }

    public void resume() {

        Logger.info("PausePresenter: Resume");
        if (canResume()) {
            audio.playSound(FHDSound.SMALL_CLICK);
            if (!keepGamePaused) {
                gameStateManager.setState(GameState.PLAY);
            }
            uiStateManager.setStateReturn();
        }
    }

    public void newGame() {

        Logger.info("PausePresenter: New Game");
        if (canChangeToNewGame()) {
            audio.playSound(FHDSound.SMALL_CLICK);
            screenChanger.changeToLevelSelect();
        }
    }

    public void mainMenu() {

        Logger.info("PausePresenter: Main Menu");
        if (canChangeToMainMenu()) {
            audio.playSound(FHDSound.SMALL_CLICK);
            screenChanger.changeToMenu();
        }

    }

    public void quit() {

        gameStateManager.setState(GameState.QUIT);
    }

    private boolean canResume() {

        return uiStateManager.getState().equals(GameUIState.PAUSE_MENU);
    }

    private boolean canChangeToNewGame() {

        return uiStateManager.getState().equals(GameUIState.PAUSE_MENU);
    }

    private boolean canChangeToMainMenu() {

        return uiStateManager.getState().equals(GameUIState.PAUSE_MENU);
    }

    private void pauseState() {

        keepGamePaused = gameStateManager.getState().equals(GameState.PAUSE);
        view.pauseState();
        gameStateManager.setState(GameState.PAUSE);
    }

    @Override
    public void stateChange(GameUIState state) {

        switch (state) {
            case PAUSE_MENU:
                pauseState();
                break;
            default:
                view.standByState();
                break;
        }
    }
}
