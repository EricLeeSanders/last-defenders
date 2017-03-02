package com.foxholedefense.levelselect.ui;

import com.foxholedefense.screen.IScreenChanger;
import com.foxholedefense.util.Logger;

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
		Logger.info("Level select presenter: main menu");
		screenChanger.changeToMenu();
	}
	
	/**
	 * Sets the screen state to the level selected
	 * 
	 * @param level
	 */
	public void loadLevel(int level) {
		Logger.info("Level select presenter: load level");
		screenChanger.changeToLevelLoad(level);
	}
}
