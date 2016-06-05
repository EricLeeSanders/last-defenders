package com.eric.mtd.game.ui.presenter;

import com.eric.mtd.game.model.Player;
import com.eric.mtd.game.model.level.state.LevelStateManager;
import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;
import com.eric.mtd.game.ui.state.IGameUIStateObserver;
import com.eric.mtd.game.ui.view.interfaces.ILevelCompletedView;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.screen.state.ScreenStateManager.ScreenState;
import com.eric.mtd.state.GameStateManager;

public class LevelCompletedPresenter implements IGameUIStateObserver{
	private ILevelCompletedView view;
	private ScreenStateManager screenStateManager;
	private GameStateManager gameStateManager;
	private GameUIStateManager uiStateManager;
	private Player player;
	public LevelCompletedPresenter(Player player, GameStateManager gameStateManager
			, GameUIStateManager uiStateManager, ScreenStateManager screenStateManager){
		
		this.player = player;
		this.screenStateManager = screenStateManager;
		this.gameStateManager = gameStateManager;
		this.uiStateManager = uiStateManager;
		
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
		screenStateManager.setState(ScreenState.LEVEL_SELECTION);
	}
	/**
	 * Continue the level
	 */
	public void continueLevel() {
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
