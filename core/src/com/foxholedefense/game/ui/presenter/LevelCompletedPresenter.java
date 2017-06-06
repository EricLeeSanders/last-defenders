package com.foxholedefense.game.ui.presenter;

import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.GameUIStateObserver;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.interfaces.ILevelCompletedView;
import com.foxholedefense.screen.ScreenChanger;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDAudio.FHDSound;
import com.foxholedefense.util.Logger;

public class LevelCompletedPresenter implements GameUIStateObserver {

	private ILevelCompletedView view;
	private ScreenChanger screenChanger;
	private GameUIStateManager uiStateManager;
	private FHDAudio audio;

	public LevelCompletedPresenter(GameUIStateManager uiStateManager, ScreenChanger screenChanger, FHDAudio audio){
		
		this.screenChanger = screenChanger;
		this.uiStateManager = uiStateManager;
		this.audio = audio;
		
		uiStateManager.attach(this);
	}
	
	/**
	 * Set the Level Completed view
	 * 
	 * @param view
	 */
	public void setView(ILevelCompletedView view) {
		this.view = view;
		stateChange(uiStateManager.getState());
	}
	
	/**
	 * Change to level select
	 */
	public void levelSelect() {

		if(canChangeToLevelSelect()) {
			Logger.info("Level Completed Presenter: level select");
			audio.playSound(FHDSound.SMALL_CLICK);
			screenChanger.changeToLevelSelect();
		}
	}

	/**
	 * Change to main menu
	 */
	public void mainMenu() {

		if(canChangeToMainMenu()) {
			Logger.info("Level Completed Presenter: main menu");
			audio.playSound(FHDSound.SMALL_CLICK);
			screenChanger.changeToMenu();
		}
	}
	/**
	 * Continue the level
	 */
	public void continueLevel() {

		if(canContinueLevel()) {
			Logger.info("Level Completed Presenter: continue level");
			audio.playSound(FHDSound.SMALL_CLICK);
			uiStateManager.setState(GameUIState.STANDBY);
		}
	}

	private boolean canChangeToLevelSelect(){

		return uiStateManager.getState().equals(GameUIState.LEVEL_COMPLETED);
	}

	private boolean canChangeToMainMenu(){

		return uiStateManager.getState().equals(GameUIState.LEVEL_COMPLETED);
	}

	private boolean canContinueLevel(){

		return uiStateManager.getState().equals(GameUIState.LEVEL_COMPLETED);
	}
	
	@Override
	public void stateChange(GameUIState state) {

		switch(state) {
		case LEVEL_COMPLETED:
			view.levelCompletedState();
			break;
		default:
			view.standByState();
			break;
		}
		
	}

}
