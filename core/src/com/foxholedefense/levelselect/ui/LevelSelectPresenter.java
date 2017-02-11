package com.foxholedefense.levelselect.ui;

import com.foxholedefense.screen.IScreenChanger;

/**
 * Presenter for the Level Select Menu
 * 
 * @author Eric
 *
 */
public class LevelSelectPresenter {
	private IScreenChanger screenChanger;

	public LevelSelectPresenter(IScreenChanger screenChanger) {
		this.screenChanger = screenChanger;
	}

	public void mainMenu(){
		screenChanger.changeToMenu();
	}
	
	/**
	 * Sets the screen state to the level selected
	 * 
	 * @param level
	 */
	public void loadLevel(int level) {
		screenChanger.changeToLevelLoad(level);
	}
}
