package com.eric.mtd.game.ui.presenter;

import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;
import com.eric.mtd.game.ui.state.IGameUIStateObserver;
import com.eric.mtd.game.ui.view.interfaces.IOptionsView;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.screen.state.ScreenStateManager.ScreenState;
import com.eric.mtd.state.GameStateManager;
import com.eric.mtd.state.GameStateManager.GameState;
import com.eric.mtd.util.MTDAudio;
import com.eric.mtd.util.MTDAudio.MTDSound;

/**
 * Presenter for the Options View
 * 
 * @author Eric
 *
 */
public class OptionsPresenter implements IGameUIStateObserver {
	private GameStateManager gameStateManager;
	private GameUIStateManager uiStateManager;
	private ScreenStateManager screenStateManager;
	private IOptionsView view;
	private MTDAudio audio;
	public OptionsPresenter(GameUIStateManager uiStateManager, GameStateManager gameStateManager, ScreenStateManager screenStateManager, MTDAudio audio) {
		this.uiStateManager = uiStateManager;
		uiStateManager.attach(this);
		this.gameStateManager = gameStateManager;
		this.screenStateManager = screenStateManager;
		this.audio = audio;
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
	}

	/**
	 * Resume the game
	 */
	public void resumeGame() {
		audio.playSound(MTDSound.SMALL_CLICK);
		gameStateManager.setState(GameState.PLAY);
		uiStateManager.setStateReturn();

	}

	/**
	 * Go to main menu
	 */
	public void mainMenu() {
		audio.playSound(MTDSound.SMALL_CLICK);
		gameStateManager.setState(GameState.PLAY);
		screenStateManager.setState(ScreenState.MENU);

	}

	/**
	 * Start a new game
	 */
	public void newGame() {
		audio.playSound(MTDSound.SMALL_CLICK);
		gameStateManager.setState(GameState.PLAY);
		screenStateManager.setState(ScreenState.LEVEL_SELECTION);

	}
	
	public void soundPressed() {
		audio.playSound(MTDSound.SMALL_CLICK);
		audio.changeSoundEnabled();
		view.setBtnSoundOn(audio.isSoundEnabled());
	}
	
	public void musicPressed() {
		audio.playSound(MTDSound.SMALL_CLICK);
		audio.changeMusicEnabled();
		view.setBtnMusicOn(audio.isMusicEnabled());
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
