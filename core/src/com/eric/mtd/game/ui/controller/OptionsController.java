package com.eric.mtd.game.ui.controller;

import com.eric.mtd.game.ui.controller.interfaces.IOptionsController;
import com.eric.mtd.game.ui.state.GameUIStateManager;
import com.eric.mtd.game.ui.state.GameUIStateManager.GameUIState;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.screen.state.ScreenStateManager.ScreenState;
import com.eric.mtd.state.GameStateManager;
import com.eric.mtd.state.GameStateManager.GameState;

public class OptionsController implements IOptionsController{
	private GameStateManager gameStateManager;
	private GameUIStateManager uiStateManager;
	private ScreenStateManager screenStateManager;
	public OptionsController(GameUIStateManager uiStateManager, GameStateManager gameStateManager, ScreenStateManager screenStateManager){
		this.uiStateManager = uiStateManager;
		this.gameStateManager = gameStateManager;
		this.screenStateManager = screenStateManager;
	}
	@Override
	public void resumeGame() {
		gameStateManager.setState(GameState.PLAY);
		uiStateManager.setState(GameUIState.STANDBY);
		
	}
	@Override
	public void mainMenu() {
		screenStateManager.setState(ScreenState.MENU);
		
	}
	@Override
	public void newGame() {
		screenStateManager.setState(ScreenState.LEVEL_SELECTION);
		
	}

}
