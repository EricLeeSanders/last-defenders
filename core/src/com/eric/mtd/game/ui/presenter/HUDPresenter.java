package com.eric.mtd.game.ui.presenter;

import com.eric.mtd.MTDGame;
import com.eric.mtd.game.model.IPlayerObserver;
import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.level.state.LevelStateManager;
import com.eric.mtd.game.model.level.state.LevelStateManager.LevelState;
import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;
import com.eric.mtd.game.ui.state.IGameUIStateObserver;
import com.eric.mtd.game.ui.view.interfaces.IHUDView;
import com.eric.mtd.state.GameStateManager;
import com.eric.mtd.state.GameStateManager.GameState;
import com.eric.mtd.util.Resources;

/**
 * Presenter for the HUD
 * 
 * @author Eric
 *
 */
public class HUDPresenter implements IGameUIStateObserver, IPlayerObserver {
	private LevelStateManager levelStateManager;
	private GameUIStateManager uiStateManager;
	private GameStateManager gameStateManager;
	private Player player;
	private boolean normalSpeed = true;
	private IHUDView view;

	public HUDPresenter(GameUIStateManager uiStateManager, LevelStateManager levelStateManager, GameStateManager gameStateManager, Player player) {
		this.levelStateManager = levelStateManager;
		this.uiStateManager = uiStateManager;
		uiStateManager.attach(this);
		this.gameStateManager = gameStateManager;
		this.player = player;
		player.attachObserver(this);
	}

	/**
	 * Set the HUD view
	 * 
	 * @param view
	 */
	public void setView(IHUDView view) {
		this.view = view;
		changeUIState(uiStateManager.getState());
		playerAttributeChange();
	}

	/**
	 * Show the options view. Also pause the Game.
	 */
	public void options() {
		uiStateManager.setState(GameUIState.OPTIONS);
		gameStateManager.setState(GameState.PAUSE);

	}

	/**
	 * Quit the game
	 */
	public void quit() {
		// gameStateManager.setState(LevelState.QUIT);

	}

	/**
	 * Start the next wave
	 */
	public void startWave() {
		levelStateManager.setState(LevelState.SPAWNING_ENEMIES);
		uiStateManager.setState(GameUIState.WAVE_IN_PROGRESS);

	}

	/**
	 * Show the Enlist view
	 */
	public void enlist() {
		uiStateManager.setState(GameUIState.ENLISTING);

	}

	/**
	 * Show the Support view
	 */
	public void support() {
		uiStateManager.setState(GameUIState.SUPPORT);

	}

	/**
	 * Change the speed of the game
	 */
	public void changeGameSpeed() {
		if (normalSpeed) {
			MTDGame.gameSpeed = (Resources.DOUBLE_SPEED);
			normalSpeed = false;
		} else {
			MTDGame.gameSpeed = (Resources.NORMAL_SPEED);
			normalSpeed = true;
		}
		view.changeSpeed(normalSpeed);

	}

	@Override
	public void changeUIState(GameUIState state) {
		switch (state) {
		case GAME_OVER:
			view.gameOverState();
			break;
		case OPTIONS:
			view.optionsState();
			break;
		case SUPPORT:
			view.supportState();
			break;
		case ENLISTING:
			view.enlistingState();
			break;
		case WAVE_IN_PROGRESS:
			view.waveInProgressState();
			break;
		case STANDBY:
			view.standByState();
			break;
		default:
			break;
		}
	}

	/**
	 * Receive notifications of Player attribute changes
	 */
	@Override
	public void playerAttributeChange() {
		view.setLives(String.valueOf(player.getLives()));
		view.setMoney(String.valueOf(player.getMoney()));
		view.setWaveCount(String.valueOf(player.getWaveCount()));
	}

}
