package com.foxholedefense.game.ui.presenter;

import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.level.state.LevelStateManager;
import com.foxholedefense.game.ui.state.GameUIStateManager;
import com.foxholedefense.game.ui.state.IGameUIStateObserver;
import com.foxholedefense.game.ui.state.GameUIStateManager.GameUIState;
import com.foxholedefense.game.ui.view.interfaces.ILevelCompletedView;
import com.foxholedefense.screen.state.ScreenStateManager;
import com.foxholedefense.screen.state.ScreenStateManager.ScreenState;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDAudio.FHDSound;

public class LevelCompletedPresenter implements IGameUIStateObserver{
	private ILevelCompletedView view;
	private ScreenStateManager screenStateManager;
	private GameStateManager gameStateManager;
	private GameUIStateManager uiStateManager;
	private Player player;
	private FHDAudio audio;
	public LevelCompletedPresenter(Player player, GameStateManager gameStateManager
			, GameUIStateManager uiStateManager, ScreenStateManager screenStateManager, FHDAudio audio){
		
		this.player = player;
		this.screenStateManager = screenStateManager;
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
		screenStateManager.setState(ScreenState.LEVEL_SELECTION);
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
