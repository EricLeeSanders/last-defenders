package com.foxholedefense.game.ui.presenter;

import com.foxholedefense.game.model.PlayerObserver;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.level.Level;
import com.foxholedefense.game.model.level.state.LevelStateManager;
import com.foxholedefense.game.model.level.state.LevelStateManager.LevelState;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateObserver;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.interfaces.IHUDView;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.state.GameStateManager.GameState;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.FHDAudio.FHDSound;

/**
 * Presenter for the HUD
 * 
 * @author Eric
 *
 */
public class HUDPresenter implements GameUIStateObserver, PlayerObserver {

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
		stateChange(uiStateManager.getState());
		playerAttributeChange();
	}

	/**
	 * Pauses the game
	 */
	public void pause() {

		audio.playSound(FHDSound.SMALL_CLICK);
		if(canPauseGame()) {
			Logger.info("HUD Presenter: pause");
			gameStateManager.setState(GameState.PAUSE);
		}
	}

	/**
	 * Resumes the game
	 */
	public void resume() {

		audio.playSound(FHDSound.SMALL_CLICK);
		if(canResumeGame()) {
			Logger.info("HUD Presenter: resume");
			gameStateManager.setState(GameState.PLAY);
		}
	}

	/**
	 * Show the options view.
	 */
	public void options() {

		audio.playSound(FHDSound.SMALL_CLICK);
		if(canViewOptions()) {
			Logger.info("HUD Presenter: options");
			uiStateManager.setState(GameUIState.OPTIONS);
		}
	}

	/**
	 * Start the next wave
	 */
	public void startWave() {

		audio.playSound(FHDSound.SMALL_CLICK);
		if(canStartWave()) {
			Logger.info("HUD Presenter: starting wave");
			levelStateManager.setState(LevelState.WAVE_IN_PROGRESS);
			uiStateManager.setState(GameUIState.WAVE_IN_PROGRESS);
		}
	}

	/**
	 * Show the Enlist view
	 */
	public void enlist() {

		audio.playSound(FHDSound.SMALL_CLICK);
		if(canEnlist()) {
			Logger.info("HUD Presenter: enlist");
			uiStateManager.setState(GameUIState.ENLISTING);
		}
	}

	/**
	 * Show the Support view
	 */
	public void addSupport() {

		audio.playSound(FHDSound.SMALL_CLICK);
		if(canAddSupport()) {
			Logger.info("HUD Presenter: addSupport");
			uiStateManager.setState(GameUIState.SUPPORT);
		}
	}

	public boolean isGamePaused(){

		return gameStateManager.getState().equals(GameState.PAUSE);
	}

	/**
	 * Can only pause the game when the wave is in progress and the game state is play
	 * @return
     */
	private boolean canPauseGame(){

		return uiStateManager.getState().equals(GameUIState.WAVE_IN_PROGRESS)
				&& gameStateManager.getState().equals(GameState.PLAY);
	}

	/**
	 * Can only resume the game when the wave is in progress and the game is paused
	 * @return
     */
	private boolean canResumeGame(){

		return uiStateManager.getState().equals(GameUIState.WAVE_IN_PROGRESS)
				&& isGamePaused();
	}

	/**
	 * Can only view options if the UI State is in Standby or Wave in Progress
	 * @return
     */
	private boolean canViewOptions(){

		return uiStateManager.getState().equals(GameUIState.WAVE_IN_PROGRESS)
				|| uiStateManager.getState().equals(GameUIState.STANDBY);
	}

	/**
	 * Can only start a wave if the the UI State is in Standby
	 * @return
     */
	private boolean canStartWave(){

		return uiStateManager.getState().equals(GameUIState.STANDBY);
	}

	/**
	 * Can only enlist if the UI State is in Standby or Wave in Progress
	 * @return
     */
	private boolean canEnlist(){

		return uiStateManager.getState().equals(GameUIState.WAVE_IN_PROGRESS)
				|| uiStateManager.getState().equals(GameUIState.STANDBY);
	}

	/**
	 * Can only add Support if the UI State is in Standby or Wave in Progress
	 * @return
	 */
	private boolean canAddSupport(){

		return uiStateManager.getState().equals(GameUIState.WAVE_IN_PROGRESS)
				|| uiStateManager.getState().equals(GameUIState.STANDBY);
	}

	@Override
	public void stateChange(GameUIState state) {

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
			case PAUSE_MENU:
				view.quitState();
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
		int waveCount = player.getWaveCount();
		if(waveCount > Level.MAX_WAVES){
			view.setWaveCount(String.valueOf(waveCount));
		} else {
			view.setWaveCount(String.valueOf(waveCount + "/" + Level.MAX_WAVES));
		}
	}

}
