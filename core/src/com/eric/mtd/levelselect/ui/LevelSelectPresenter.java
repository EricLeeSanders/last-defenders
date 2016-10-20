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

	/**
	 * Sets the screen state to the level selected
	 * 
	 * @param level
	 */
	public void playLevel(int level) {
		switch (level) {
		case 1:
			screenStateManager.setState(ScreenState.LEVEL_1_SELECTED);
			break;
		case 2:
			screenStateManager.setState(ScreenState.LEVEL_2_SELECTED);
			break;
		case 3:
			screenStateManager.setState(ScreenState.LEVEL_3_SELECTED);
			break;
		case 4:
			screenStateManager.setState(ScreenState.LEVEL_4_SELECTED);
			break;
		case 5:
			screenStateManager.setState(ScreenState.LEVEL_5_SELECTED);
			break;
		}
	}
}
