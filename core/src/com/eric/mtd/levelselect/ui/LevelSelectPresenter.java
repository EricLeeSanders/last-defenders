package com.eric.mtd.levelselect.ui;

import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.screen.state.ScreenStateManager.ScreenState;

/**
 * Presenter for the Level Select Menu
 * 
 * @author Eric
 *
 */
public class LevelSelectPresenter {
	private ScreenStateManager screenStateManager;

	public LevelSelectPresenter(ScreenStateManager screenStateManager) {
		this.screenStateManager = screenStateManager;
	}

	public void mainMenu(){
		screenStateManager.setState(ScreenState.MENU);
	}
	
	/**
	 * Sets the screen state to the level selected
	 * 
	 * @param level
	 */
	public void loadLevel(int level) {
		switch (level) {
		case 1:
			screenStateManager.setState(ScreenState.LOAD_LEVEL_1);
			break;
		case 2:
			screenStateManager.setState(ScreenState.LOAD_LEVEL_2);
			break;
		case 3:
			screenStateManager.setState(ScreenState.LOAD_LEVEL_3);
			break;
		case 4:
			screenStateManager.setState(ScreenState.LOAD_LEVEL_4);
			break;
		case 5:
			screenStateManager.setState(ScreenState.LOAD_LEVEL_5);
			break;
		}
	}
}
