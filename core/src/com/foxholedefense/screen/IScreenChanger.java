package com.foxholedefense.screen;

/**
 * Created by Eric on 1/30/2017.
 */

public interface IScreenChanger {
    public void changeToMenu();
    public void changeToLevelSelect();
    public void changeToLevel(int level);
    public void changeToLevelLoad(int level);
}
