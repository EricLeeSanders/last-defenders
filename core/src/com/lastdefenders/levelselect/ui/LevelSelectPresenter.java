package com.lastdefenders.levelselect.ui;

import com.lastdefenders.googleplay.GooglePlayLeaderboard;
import com.lastdefenders.googleplay.GooglePlayServices;
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
    private GooglePlayServices playServices;

    public LevelSelectPresenter(ScreenChanger screenChanger, GooglePlayServices playServices) {

        this.screenChanger = screenChanger;
        this.playServices = playServices;
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

    public void showAchievements(){
        playServices.showAchievements();
    }

    public void showLeaderboardForLevel(LevelName levelName){

        GooglePlayLeaderboard leaderboard = GooglePlayLeaderboard.findByLevelName(levelName);
        playServices.showLeaderboard(leaderboard);
    }

    public void showAllLeaderboards(){

        playServices.showLeaderboards();
    }

    public boolean isGooglePlayServicesAvailable(){
        return playServices.isGooglePlayServicesAvailable();
    }
}
