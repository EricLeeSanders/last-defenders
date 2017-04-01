package com.foxholedefense.screen;

/**
 * Created by Eric on 1/30/2017.
 */

public interface IScreenChanger {
    void changeToMenu();
    void changeToLevelSelect();
    void changeToLevel(int level);
    void changeToLevelLoad(int level);
}
