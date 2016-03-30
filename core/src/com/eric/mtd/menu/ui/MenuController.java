package com.eric.mtd.menu.ui;


import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.screen.state.ScreenStateManager.ScreenState;

public class MenuController implements IMenuController{
	private ScreenStateManager screenStateManager;
	public MenuController(ScreenStateManager screenStateManager){
		this.screenStateManager = screenStateManager;
	}
	@Override
	public void playGame() {
		screenStateManager.setState(ScreenState.LEVEL_SELECTION);
		
	}

}
