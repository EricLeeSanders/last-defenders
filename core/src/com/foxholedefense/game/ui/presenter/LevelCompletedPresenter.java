package com.foxholedefense.game.ui.presenter;

import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.IGameUIStateObserver;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.interfaces.ILevelCompletedView;
import com.foxholedefense.screen.IScreenChanger;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDAudio.FHDSound;

public class LevelCompletedPresenter implements IGameUIStateObserver{
	private ILevelCompletedView view;
	private IScreenChanger screenChanger;
	private GameStateManager gameStateManager;
	private GameUIStateManager uiStateManager;
	private Player player;
	private FHDAudio audio;
	public LevelCompletedPresenter(Player player, GameStateManager gameStateManager
			, GameUIStateManager uiStateManager, IScreenChanger screenChanger, FHDAudio audio){
		
		this.player = player;
		this.screenChanger = screenChanger;
		this.gameStateManager = gameStateManager;
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
		changeUIState(uiStateManager.getState());
	}
	
	/**
	 * Change to level select
	 */
	public void levelSelect() {
		audio.playSound(FHDSound.SMALL_CLICK);
		screenChanger.changeToLevelSelect();
	}

	/**
	 * Change to main menu
	 */
	public void mainMenu() {
		audio.playSound(FHDSound.SMALL_CLICK);
		screenChanger.changeToMenu();
	}
	/**
	 * Continue the level
	 */
	public void continueLevel() {
		audio.playSound(FHDSound.SMALL_CLICK);
		uiStateManager.setState(GameUIState.STANDBY);
	}
	
	@Override
	public void changeUIState(GameUIState state) {
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
