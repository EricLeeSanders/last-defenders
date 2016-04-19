package com.eric.mtd.menu.ui;

import com.eric.mtd.screen.state.ScreenStateManager;
import com.eric.mtd.screen.state.ScreenStateManager.ScreenState;

/**
 * Presenter class for the Main Menu
 * 
 * @author Eric
 *
 */
public class MenuPresenter {
	private ScreenStateManager screenStateManager;

	public MenuPresenter(ScreenStateManager screenStateManager) {
		this.screenStateManager = screenStateManager;
	}

	public void playGame() {
		screenStateManager.setState(ScreenState.LEVEL_SELECTION);

	}

}
