package com.foxholedefense.game.ui.presenter;

import com.badlogic.gdx.math.Vector2;
import com.foxholedefense.game.model.IPlayerObserver;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.ICombatActorObserver;
import com.foxholedefense.game.model.level.Level;
import com.foxholedefense.game.model.level.state.LevelStateManager;
import com.foxholedefense.game.model.level.state.LevelStateManager.LevelState;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.IGameUIStateObserver;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.interfaces.IHUDView;
import com.foxholedefense.game.ui.view.interfaces.IMessageDisplayer;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.state.GameStateManager.GameState;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.FHDAudio.FHDSound;

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
	private IHUDView view;
	private FHDAudio audio;
	public HUDPresenter(GameUIStateManager uiStateManager, LevelStateManager levelStateManager, GameStateManager gameStateManager, Player player, FHDAudio audio) {
		this.levelStateManager = levelStateManager;
		this.uiStateManager = uiStateManager;
		uiStateManager.attach(this);
		this.gameStateManager = gameStateManager;
		this.player = player;
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
	 * Pauses the game
	 */
	public void pause() {
		Logger.info("HUD Presenter: pause");
		audio.playSound(FHDSound.SMALL_CLICK);
		gameStateManager.setState(GameState.PAUSE);
	}

	/**
	 * Resumes the game
	 */
	public void resume() {
		Logger.info("HUD Presenter: resume");
		audio.playSound(FHDSound.SMALL_CLICK);
		gameStateManager.setState(GameState.PLAY);
	}

	/**
	 * Show the options view.
	 */
	public void options() {
		Logger.info("HUD Presenter: options");
		audio.playSound(FHDSound.SMALL_CLICK);
		uiStateManager.setState(GameUIState.OPTIONS);
	}

	/**
	 * Start the next wave
	 */
	public void startWave() {
		Logger.info("HUD Presenter: starting wave");
		audio.playSound(FHDSound.SMALL_CLICK);
		levelStateManager.setState(LevelState.WAVE_IN_PROGRESS);
		uiStateManager.setState(GameUIState.WAVE_IN_PROGRESS);

	}

	/**
	 * Show the Enlist view
	 */
	public void enlist() {
		Logger.info("HUD Presenter: enlist");
		audio.playSound(FHDSound.SMALL_CLICK);
		uiStateManager.setState(GameUIState.ENLISTING);

	}

	/**
	 * Show the Support view
	 */
	public void support() {
		Logger.info("HUD Presenter: support");
		audio.playSound(FHDSound.SMALL_CLICK);
		uiStateManager.setState(GameUIState.SUPPORT);

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
		Logger.info("HUD Presenter: playerAttributeChange");
		view.setLives(String.valueOf(player.getLives()));
		view.setMoney(String.valueOf(player.getMoney()));
		setWaveCount();
	}
	
	private void setWaveCount(){
		Logger.info("HUD Presenter: setWaveCount");
		int waveCount = player.getWavesCompleted();
		if(waveCount >= Level.MAX_WAVES){
			view.setWaveCount(String.valueOf(player.getWaveCount()));
		} else {
			view.setWaveCount(String.valueOf(player.getWaveCount()) + "/" + Level.MAX_WAVES);
		}
	}

}
