package com.eric.mtd.game.ui.controller;

import com.eric.mtd.game.ui.controller.interfaces.IOptionsController;
import com.eric.mtd.game.ui.state.UIStateManager;
import com.eric.mtd.game.ui.state.UIStateManager.UIState;
import com.eric.mtd.state.GameStateManager;
import com.eric.mtd.state.GameStateManager.GameState;

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
