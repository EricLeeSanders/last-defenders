package com.lastdefenders.game.ui.presenter;

import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.game.ui.state.GameUIStateManager.GameUIState;
import com.lastdefenders.game.ui.state.GameUIStateObserver;
import com.lastdefenders.game.ui.view.interfaces.IGameOverView;
import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.LDAudio.LDSound;
import com.lastdefenders.util.Logger;

/**
 * Presenter for Game Over.
 *
 * @author Eric
 */
public class GameOverPresenter implements GameUIStateObserver {

    private Player player;
    private ScreenChanger screenChanger;
    private GameUIStateManager uiStateManager;
    private IGameOverView view;
    private LDAudio audio;

    public GameOverPresenter(GameUIStateManager uiStateManager, ScreenChanger screenChanger,
        Player player, LDAudio audio) {

        this.player = player;
        this.screenChanger = screenChanger;
        this.uiStateManager = uiStateManager;
        this.audio = audio;
        uiStateManager.attach(this);
    }

    /**
     * Set the Game Over view
     */
    public void setView(IGameOverView view) {

        this.view = view;
        stateChange(uiStateManager.getState());
    }

    /**
     * Set how many waves have been completed
     */
    private void setWavesCompleted() {

        view.setWavesCompleted(String.valueOf(player.getWavesCompleted()));
    }

    /**
     * Start a new game
     */
    public void newGame() {

        audio.playSound(LDSound.SMALL_CLICK);
        if (canSwitchToNewGame()) {
            Logger.info("Game Over Presenter: new Game");
            screenChanger.changeToLevelSelect();
        }
    }

    /**
     * Change to main menu
     */
    public void mainMenu() {

        audio.playSound(LDSound.SMALL_CLICK);
        if (canSwitchToMainMenu()) {
            Logger.info("Game Over Presenter: main menu");
            screenChanger.changeToMenu();
        }
    }

    /**
     * Change to high scores
     */
    public void highScores() {

        Logger.info("Game Over Presenter: high scores");
        audio.playSound(LDSound.SMALL_CLICK);
    }

    /**
     * Can only switch to New Game if the GAMEUIState == GAME_OVER
     */
    private boolean canSwitchToNewGame() {

        return uiStateManager.getState().equals(GameUIState.GAME_OVER);
    }

    /**
     * Can only switch to Main Menu if the GAMEUIState == GAME_OVER
     */
    private boolean canSwitchToMainMenu() {

        return uiStateManager.getState().equals(GameUIState.GAME_OVER);
    }

    @Override
    public void stateChange(GameUIState state) {

        switch (state) {
            case GAME_OVER:
                view.gameOverState();
                setWavesCompleted();
                break;
            default:
                view.standByState();
                break;
        }

    }

}
