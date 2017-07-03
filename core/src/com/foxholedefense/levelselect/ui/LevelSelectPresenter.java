package com.foxholedefense.levelselect.ui;

import com.foxholedefense.screen.ScreenChanger;
import com.foxholedefense.util.Logger;

/**
 * Presenter for the Level Select Menu
 *
 * @author Eric
 */
public class LevelSelectPresenter {

    private ScreenChanger screenChanger;

    public LevelSelectPresenter(ScreenChanger screenChanger) {

        this.screenChanger = screenChanger;
    }

    public void mainMenu() {

        Logger.info("Level select presenter: main menu");
        screenChanger.changeToMenu();
    }

    /**
     * Sets the screen state to the level selected
     */
    public void loadLevel(int level) {

        Logger.info("Level select presenter: load level");
        screenChanger.changeToLevelLoad(level);
    }
}
