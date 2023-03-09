package com.lastdefenders.game.ui.presenter;

import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.game.ui.state.GameUIStateManager.GameUIState;
import com.lastdefenders.game.ui.state.GameUIStateObserver;
import com.lastdefenders.game.ui.view.interfaces.IGameOverView;
import com.lastdefenders.googleplay.GooglePlayLeaderboard;
import com.lastdefenders.googleplay.GooglePlayServices;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.sound.LDAudio;
import com.lastdefenders.sound.LDAudio.LDSound;
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
    private GooglePlayServices playServices;
    private LevelName currentLevel;

    public GameOverPresenter(GameUIStateManager uiStateManager, ScreenChanger screenChanger,
        GooglePlayServices playServices, Player player, LevelName currentLevel, LDAudio audio) {

        this.player = player;
        this.screenChanger = screenChanger;
        this.uiStateManager = uiStateManager;
        this.audio = audio;
        this.playServices = playServices;
        this.currentLevel = currentLevel;
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

    private void gameOverState(){
        view.gameOverState();
        setWavesCompleted();
        audio.playGameEndingMusic();
    }

    /**
     * Is signed in to Google Play Services
     */
    public boolean isGPSAvailable(){
        return playServices.isDeviceCompatible();
    }

    /**
     * Show leaderboard for current level
     */
    public void leaderboard() {

        audio.playSound(LDSound.SMALL_CLICK);
        if(canViewLeaderboard()){
            Logger.info("Game Over Presenter: Show leaderboard");
            GooglePlayLeaderboard leaderboard = GooglePlayLeaderboard.findByLevelName(currentLevel);
            playServices.showLeaderboard(leaderboard);
        }
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

    private boolean canViewLeaderboard(){

        return isGPSAvailable() && uiStateManager.getState().equals(GameUIState.GAME_OVER);
    }

    @Override
    public void stateChange(GameUIState state) {

        switch (state) {
            case GAME_OVER:
                gameOverState();
                break;
            default:
                view.standByState();
                break;
        }

    }

}
