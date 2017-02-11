package com.foxholedefense.game.ui.presenter;

import com.badlogic.gdx.Preferences;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.IGameUIStateObserver;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.interfaces.IOptionsView;
import com.foxholedefense.screen.IScreenChanger;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.state.GameStateManager.GameState;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.FHDAudio.FHDSound;

/**
 * Presenter for the Options View
 * 
 * @author Eric
 *
 */
public class OptionsPresenter implements IGameUIStateObserver {
	private GameStateManager gameStateManager;
	private GameUIStateManager uiStateManager;
	private IScreenChanger screenChanger;
	private IOptionsView view;
	private FHDAudio audio;
	private Resources resources;
	public OptionsPresenter(GameUIStateManager uiStateManager, GameStateManager gameStateManager, IScreenChanger screenChanger, Resources resources, FHDAudio audio) {
		this.uiStateManager = uiStateManager;
		uiStateManager.attach(this);
		this.gameStateManager = gameStateManager;
		this.screenChanger = screenChanger;
		this.audio = audio;
		this.resources = resources;
	}

	/**
	 * Set the options view
	 * 
	 * @param view
	 */
	public void setView(IOptionsView view) {
		this.view = view;
		initView();
	}
	
	private void initView(){
		changeUIState(uiStateManager.getState());
		view.setBtnMusicOn(audio.isMusicEnabled());
		view.setBtnSoundOn(audio.isSoundEnabled());
		view.setBtnShowRangesOn(isShowRangesEnabled());
	}

	/**
	 * Resume the game
	 */
	public void resumeGame() {
		audio.playSound(FHDSound.SMALL_CLICK);
		gameStateManager.setState(GameState.PLAY);
		uiStateManager.setStateReturn();

	}

	/**
	 * Go to main menu
	 */
	public void mainMenu() {
		audio.playSound(FHDSound.SMALL_CLICK);
		gameStateManager.setState(GameState.PLAY);
		screenChanger.changeToMenu();

	}

	/**
	 * Start a new game
	 */
	public void newGame() {
		audio.playSound(FHDSound.SMALL_CLICK);
		gameStateManager.setState(GameState.PLAY);
		screenChanger.changeToLevelSelect();

	}
	
	private boolean isShowRangesEnabled(){
		Preferences prefs = resources.getUserPreferences().getPreferences();
		return prefs.getBoolean("showRanges", false);
	}
	
	public void showRangesPressed() {
		Preferences prefs = resources.getUserPreferences().getPreferences();
		boolean isShowRangesEnabled = isShowRangesEnabled();
		prefs.putBoolean("showRanges", !isShowRangesEnabled);
		prefs.flush();
		view.setBtnShowRangesOn(!isShowRangesEnabled);
	}
	
	public void soundPressed() {
		audio.playSound(FHDSound.SMALL_CLICK);
		audio.changeSoundEnabled();
		view.setBtnSoundOn(audio.isSoundEnabled());
	}

	
	public void musicPressed() {
		audio.playSound(FHDSound.SMALL_CLICK);
		audio.changeMusicEnabled();
		view.setBtnMusicOn(audio.isMusicEnabled());
	}

	public void speedChanged(float val){ resources.setGameSpeed(val);}

	public float getGameSpeed(){
		return resources.getGameSpeed();
	}

	public void volumeChanged(float vol){
		audio.setMasterVolume(vol);
	}
	
	public float getMasterVolume(){
		return audio.getMasterVolume();
	}
	
	@Override
	public void changeUIState(GameUIState state) {
		switch (state) {
		case OPTIONS:
			view.optionsState();
			break;
		default:
			audio.saveMasterVolume();
			view.standByState();
			break;
		}

	}

}
