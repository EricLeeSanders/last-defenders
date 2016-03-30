package com.eric.mtd.levelselect.ui;

import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.screen.state.ScreenStateManager.ScreenState;

public class LevelSelectController implements ILevelSelectController{
	private ScreenStateManager screenStateManager;
	public LevelSelectController(ScreenStateManager screenStateManager){
		this.screenStateManager = screenStateManager;
	}
	
	@Override
	public void playLevel(int level){
		switch(level){
		case 1:
			screenStateManager.setState(ScreenState.LEVEL_1_SELECTED);
			break;
		case 2:
			screenStateManager.setState(ScreenState.LEVEL_2_SELECTED);
			break;
		}
	}
}
