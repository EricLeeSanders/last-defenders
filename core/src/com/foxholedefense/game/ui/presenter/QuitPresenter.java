package com.foxholedefense.game.ui.presenter;

import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.state.GameUIStateObserver;
import com.foxholedefense.game.ui.view.interfaces.IQuitView;
import com.foxholedefense.screen.IScreenChanger;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.state.GameStateManager.GameState;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDAudio.FHDSound;
import com.foxholedefense.util.Logger;

/**
 * Created by Eric on 4/8/2017.
 */

public class QuitPresenter implements GameUIStateObserver {

    private GameUIStateManager uiStateManager;
    private GameStateManager gameStateManager;
    private IScreenChanger screenChanger;
    private FHDAudio audio;
    private IQuitView view;
    private boolean keepGamePaused;

    public QuitPresenter(GameUIStateManager uiStateManager, GameStateManager gameStateManager, IScreenChanger screenChanger, FHDAudio audio){
        this.uiStateManager = uiStateManager;
        this.audio = audio;
        this.screenChanger = screenChanger;
        this.gameStateManager = gameStateManager;
        uiStateManager.attach(this);
    }

    public void setView(IQuitView view){
        this.view = view;
    }

    public void resume(){
        Logger.info("QuitPresenter: Resume");
        audio.playSound(FHDSound.SMALL_CLICK);
        uiStateManager.setStateReturn();
        if(!keepGamePaused){
            gameStateManager.setState(GameState.PLAY);
        }
    }

    public void newGame(){
        Logger.info("QuitPresenter: New Game");
        audio.playSound(FHDSound.SMALL_CLICK);
        screenChanger.changeToLevelSelect();
    }

    public void mainMenu() {
        Logger.info("QuitPresenter: Main Menu");
        audio.playSound(FHDSound.SMALL_CLICK);
        screenChanger.changeToMenu();

    }

    public void quit(){
        gameStateManager.setState(GameState.QUIT);
    }

    private void quitState(){
        keepGamePaused = gameStateManager.getState().equals(GameState.PAUSE);
        view.quitState();
        gameStateManager.setState(GameState.PAUSE);
    }

    @Override
    public void stateChange(GameUIState state) {
        switch(state){
            case QUIT_MENU:
                quitState();
                break;
            default:
                view.standByState();
                break;
        }
    }
}
