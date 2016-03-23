package com.eric.mtd.ui.controller;

import com.eric.mtd.model.Player;
import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.screen.state.ScreenStateManager.ScreenState;
import com.eric.mtd.ui.controller.interfaces.IGameOverController;

public class GameOverController implements IGameOverController{
	private Player player;
	private ScreenStateManager screenStateManager;
	public GameOverController(ScreenStateManager screenStateManager, Player player){
		this.player = player;
		this.screenStateManager = screenStateManager;
	}
	@Override
	public int getWavesCompleted() {
		// TODO Auto-generated method stub
		return player.getWaveCount();
	}
	@Override
	public void newGame() {
		screenStateManager.setState(ScreenState.LEVEL_SELECTION);
		
	}
	@Override
	public void mainMenu() {
		screenStateManager.setState(ScreenState.MENU);
		
	}
	@Override
	public void highScores() {
		// TODO Auto-generated method stub
		
	}

}
