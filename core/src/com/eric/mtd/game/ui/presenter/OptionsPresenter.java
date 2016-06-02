package com.eric.mtd.game.ui.presenter;

import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;
import com.eric.mtd.game.ui.state.IGameUIStateObserver;
import com.eric.mtd.game.ui.view.interfaces.IOptionsView;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.screen.state.ScreenStateManager.ScreenState;
import com.eric.mtd.state.GameStateManager;
import com.eric.mtd.state.GameStateManager.GameState;
import com.eric.mtd.util.AudioUtil;

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

	public OptionsPresenter(GameUIStateManager uiStateManager, GameStateManager gameStateManager, ScreenStateManager screenStateManager) {
		this.uiStateManager = uiStateManager;
		uiStateManager.attach(this);
		this.gameStateManager = gameStateManager;
		this.screenStateManager = screenStateManager;
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
		view.setBtnMusicOn(AudioUtil.isMusicEnabled());
		view.setBtnSoundOn(AudioUtil.isSoundEnabled());
	}

	/**
	 * Resume the game
	 */
	public void resumeGame() {
		gameStateManager.setState(GameState.PLAY);
		uiStateManager.setStateReturn();

	}

	/**
	 * Go to main menu
	 */
	public void mainMenu() {
		gameStateManager.setState(GameState.PLAY);
		screenStateManager.setState(ScreenState.MENU);

	}

	/**
	 * Start a new game
	 */
	public void newGame() {
		gameStateManager.setState(GameState.PLAY);
		screenStateManager.setState(ScreenState.LEVEL_SELECTION);

	}
	
	public void soundPressed() {
		AudioUtil.changeSoundEnabled();
		view.setBtnSoundOn(AudioUtil.isSoundEnabled());
	}
	
	public void musicPressed() {
		AudioUtil.changeMusicEnabled();
		view.setBtnMusicOn(AudioUtil.isMusicEnabled());
	}
	
	@Override
	public void changeUIState(GameUIState state) {
		switch (state) {
		case OPTIONS:
			view.optionsState();
			break;
		default:
			view.standByState();
			break;
		}

	}

}
