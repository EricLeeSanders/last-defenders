package com.eric.mtd.ui.controller;

import com.eric.mtd.state.GameStateManager;
import com.eric.mtd.state.GameStateManager.GameState;
import com.eric.mtd.ui.controller.interfaces.IOptionsController;
import com.eric.mtd.ui.state.UIStateManager;
import com.eric.mtd.ui.state.UIStateManager.UIState;

public class OptionsController implements IOptionsController{
	private GameStateManager gameStateManager;
	private UIStateManager uiStateManager;
	public OptionsController(UIStateManager uiStateManager, GameStateManager gameStateManager){
		this.uiStateManager = uiStateManager;
		this.gameStateManager = gameStateManager;
	}
	@Override
	public void resumeGame() {
		gameStateManager.setState(GameState.PLAY);
		uiStateManager.setState(UIState.STANDBY);
		
	}

}
