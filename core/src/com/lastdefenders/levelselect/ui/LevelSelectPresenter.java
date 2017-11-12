package com.lastdefenders.levelselect.ui;

import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.util.Logger;

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
    public void loadLevel(LevelName level) {

        Logger.info("Level select presenter: load level");
        screenChanger.changeToLevelLoad(level);
    }
}
