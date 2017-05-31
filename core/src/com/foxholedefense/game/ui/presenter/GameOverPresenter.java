package com.foxholedefense.game.ui.presenter;

import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateObserver;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.interfaces.IGameOverView;
import com.foxholedefense.screen.ScreenChanger;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDAudio.FHDSound;
import com.foxholedefense.util.Logger;

/**
 * Presenter for Game Over.
 * 
 * @author Eric
 *
 */
public class GameOverPresenter implements GameUIStateObserver {

	private Player player;
	private ScreenChanger screenChanger;
	private GameUIStateManager uiStateManager;
	private IGameOverView view;
	private FHDAudio audio;

	public GameOverPresenter(GameUIStateManager uiStateManager, ScreenChanger screenChanger, Player player, FHDAudio audio) {
		this.player = player;
		this.screenChanger = screenChanger;
		this.uiStateManager = uiStateManager;
		this.audio = audio;
		uiStateManager.attach(this);
	}

	/**
	 * Set the Game Over view
	 * 
	 * @param view
	 */
	public void setView(IGameOverView view) {
		this.view = view;
		stateChange(uiStateManager.getState());
	}

	/**
	 * Set how many waves have been completed
	 */
	public void setWavesCompleted() {
		view.setWavesCompleted(String.valueOf(player.getWavesCompleted()));
	}

	/**
	 * Start a new game
	 */
	public void newGame() {
		Logger.info("Game Over Presenter: new Game");
		audio.playSound(FHDSound.SMALL_CLICK);
		screenChanger.changeToLevelSelect();

	}

	/**
	 * Change to main menu
	 */
	public void mainMenu() {
		Logger.info("Game Over Presenter: main menu");
		audio.playSound(FHDSound.SMALL_CLICK);
		screenChanger.changeToMenu();

	}

	/**
	 * Change to high scores
	 */
	public void highScores() {
		Logger.info("Game Over Presenter: high scores");
		audio.playSound(FHDSound.SMALL_CLICK);
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
