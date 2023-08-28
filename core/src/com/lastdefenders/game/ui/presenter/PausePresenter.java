package com.lastdefenders.game.ui.presenter;

import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.game.ui.state.GameUIStateManager.GameUIState;
import com.lastdefenders.game.ui.state.GameUIStateObserver;
import com.lastdefenders.game.ui.view.interfaces.IPauseView;
import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.sound.SoundPlayer;
import com.lastdefenders.state.GameStateManager;
import com.lastdefenders.state.GameStateManager.GameState;
import com.lastdefenders.sound.LDSound;
import com.lastdefenders.util.Logger;

/**
 * Created by Eric on 4/8/2017.
 */

public class PausePresenter implements GameUIStateObserver {

    private GameUIStateManager uiStateManager;
    private GameStateManager gameStateManager;
    private ScreenChanger screenChanger;
    private SoundPlayer soundPlayer;
    private IPauseView view;
    private boolean keepGamePaused;

    public PausePresenter(GameUIStateManager uiStateManager, GameStateManager gameStateManager,
        ScreenChanger screenChanger, SoundPlayer soundPlayer) {

        this.uiStateManager = uiStateManager;
        this.soundPlayer = soundPlayer;
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
            soundPlayer.play(LDSound.Type.SMALL_CLICK);
            if (!keepGamePaused) {
                gameStateManager.setState(GameState.PLAY);
            }
            uiStateManager.setStateReturn();
        }
    }

    public void newGame() {

        Logger.info("PausePresenter: New Game");
        if (canChangeToNewGame()) {
            soundPlayer.play(LDSound.Type.SMALL_CLICK);
            screenChanger.changeToLevelSelect();
        }
    }

    public void mainMenu() {

        Logger.info("PausePresenter: Main Menu");
        if (canChangeToMainMenu()) {
            soundPlayer.play(LDSound.Type.SMALL_CLICK);
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
