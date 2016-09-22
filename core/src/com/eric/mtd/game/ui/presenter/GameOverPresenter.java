package com.eric.mtd.game.ui.presenter;

import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;
import com.eric.mtd.game.ui.state.IGameUIStateObserver;
import com.eric.mtd.game.ui.view.interfaces.IGameOverView;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.screen.state.ScreenStateManager.ScreenState;
import com.eric.mtd.util.MTDAudio;
import com.eric.mtd.util.MTDAudio.MTDSound;

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
	private MTDAudio audio;
	public GameOverPresenter(GameUIStateManager uiStateManager, ScreenStateManager screenStateManager, Player player, MTDAudio audio) {
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
		audio.playSound(MTDSound.SMALL_CLICK);
		screenStateManager.setState(ScreenState.LEVEL_SELECTION);

	}

	/**
	 * Change to main menu
	 */
	public void mainMenu() {
		audio.playSound(MTDSound.SMALL_CLICK);
		screenStateManager.setState(ScreenState.MENU);

	}

	/**
	 * Change to high scores
	 */
	public void highScores() {
		audio.playSound(MTDSound.SMALL_CLICK);
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
