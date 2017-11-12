package com.lastdefenders.screen;

import com.lastdefenders.levelselect.LevelName;

/**
 * Created by Eric on 1/30/2017.
 */

public interface ScreenChanger {

    void changeToMenu();

    void changeToLevelSelect();

    void changeToLevel(LevelName level);

    void changeToLevelLoad(LevelName level);
}
