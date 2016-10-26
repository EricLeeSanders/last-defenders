package com.foxholedefense.game.ui.presenter;

import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.IGameUIStateObserver;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.interfaces.IGameOverView;
import com.foxholedefense.screen.state.ScreenStateManager;
import com.foxholedefense.screen.state.ScreenStateManager.ScreenState;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDAudio.FHDSound;

/**
 * Presenter for Game Over.
 * 
 * @author Eric
 *
 */
public class GameOverPresenter implements IGameUIStateObserver {
	private Player player;
	private ScreenStateManager screenStateManager;
	private GameUIStateManager uiStateManager;
	private IGameOverView view;
	private FHDAudio audio;
	public GameOverPresenter(GameUIStateManager uiStateManager, ScreenStateManager screenStateManager, Player player, FHDAudio audio) {
		this.player = player;
		this.screenStateManager = screenStateManager;
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
		changeUIState(uiStateManager.getState());
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
		audio.playSound(FHDSound.SMALL_CLICK);
		screenStateManager.setState(ScreenState.LEVEL_SELECTION);

	}

	/**
	 * Change to main menu
	 */
	public void mainMenu() {
		audio.playSound(FHDSound.SMALL_CLICK);
		screenStateManager.setState(ScreenState.MENU);

	}

	/**
	 * Change to high scores
	 */
	public void highScores() {
		audio.playSound(FHDSound.SMALL_CLICK);
	}

	@Override
	public void changeUIState(GameUIState state) {
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
