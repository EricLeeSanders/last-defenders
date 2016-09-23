package com.eric.mtd.game.ui.presenter;

import com.eric.mtd.MTDGame;
import com.eric.mtd.game.model.IPlayerObserver;
import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.level.Level;
import com.eric.mtd.game.model.level.state.LevelStateManager;
import com.eric.mtd.game.model.level.state.LevelStateManager.LevelState;
import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;
import com.eric.mtd.game.ui.state.IGameUIStateObserver;
import com.eric.mtd.game.ui.view.interfaces.IHUDView;
import com.eric.mtd.state.GameStateManager;
import com.eric.mtd.state.GameStateManager.GameState;
import com.eric.mtd.util.MTDAudio;
import com.eric.mtd.util.Resources;
import com.eric.mtd.util.MTDAudio.MTDSound;

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
	private boolean doubleSpeedEnabled = false;
	private IHUDView view;
	private MTDAudio audio;
	private Resources resources;
	public HUDPresenter(GameUIStateManager uiStateManager, LevelStateManager levelStateManager, GameStateManager gameStateManager, Player player, Resources resources, MTDAudio audio) {
		this.levelStateManager = levelStateManager;
		this.uiStateManager = uiStateManager;
		uiStateManager.attach(this);
		this.gameStateManager = gameStateManager;
		this.player = player;
		this.resources = resources;
		this.audio = audio;
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
		audio.playSound(MTDSound.SMALL_CLICK);
		uiStateManager.setState(GameUIState.OPTIONS);
		gameStateManager.setState(GameState.PAUSE);

	}

	/**
	 * Start the next wave
	 */
	public void startWave() {
		audio.playSound(MTDSound.SMALL_CLICK);
		levelStateManager.setState(LevelState.WAVE_IN_PROGRESS);
		uiStateManager.setState(GameUIState.WAVE_IN_PROGRESS);

	}

	/**
	 * Show the Enlist view
	 */
	public void enlist() {
		audio.playSound(MTDSound.SMALL_CLICK);
		uiStateManager.setState(GameUIState.ENLISTING);

	}

	/**
	 * Show the Support view
	 */
	public void support() {
		audio.playSound(MTDSound.SMALL_CLICK);
		uiStateManager.setState(GameUIState.SUPPORT);

	}

	/**
	 * Change the speed of the game
	 */
	public void changeGameSpeed() {
		audio.playSound(MTDSound.SMALL_CLICK);
		if (doubleSpeedEnabled) {
			resources.setGameSpeed(Resources.NORMAL_SPEED);
			doubleSpeedEnabled = false;
		} else {
			resources.setGameSpeed(Resources.DOUBLE_SPEED);
			doubleSpeedEnabled = true;
		}
		view.changeSpeed(doubleSpeedEnabled);

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
		case INSPECTING:
			view.inspectingState();
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
		setWaveCount();
	}
	
	private void setWaveCount(){
		int waveCount = player.getWavesCompleted();
		if(waveCount >= Level.MAX_WAVES){
			view.setWaveCount(String.valueOf(player.getWaveCount()));
		} else {
			view.setWaveCount(String.valueOf(player.getWaveCount()) + "/" + Level.MAX_WAVES);
		}
	}

}
