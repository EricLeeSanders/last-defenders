package com.lastdefenders;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.lastdefenders.game.GameScreen;
import com.lastdefenders.googleplay.GooglePlayServices;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.levelselect.LevelSelectScreen;
import com.lastdefenders.load.GameLoadingScreen;
import com.lastdefenders.load.LevelLoadingScreen;
import com.lastdefenders.menu.MenuScreen;
import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.state.GameStateManager;
import com.lastdefenders.state.GameStateManager.GameState;
import com.lastdefenders.state.GameStateObserver;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.UserPreferences;

public class LDGame extends Game implements ScreenChanger, GameStateObserver {

    private GameStateManager gameStateManager;
    private Resources resources;
    private LDAudio audio;
    private GooglePlayServices playServices;

    // Needed for launcher without play services
    // TODO remove this
    public LDGame() {

    }

    public LDGame(GooglePlayServices playServices) {

        this.playServices = playServices;
        //playServices.signIn();
    }

    @Override
    public void create() {

        Logger.info("LDGame: Creating");
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        UserPreferences userPreferences = new UserPreferences();
        resources = new Resources(userPreferences);
        audio = new LDAudio(userPreferences);
        gameStateManager = new GameStateManager();
        GameLoadingScreen loadingScreen = new GameLoadingScreen(gameStateManager, this, resources,
            audio);
        setScreen(loadingScreen);
        gameStateManager.attach(this);
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void resume() {

        Logger.info("LDGame: resuming");
        resources.reload();
        super.resume();
    }

    @Override
    public void dispose() {

        Logger.info("LDGame: disposing");
        resources.dispose();
        audio.dispose();
        this.getScreen().dispose();
        super.dispose();
    }


    @Override
    public void changeToMenu() {

        Logger.info("LDGame: Changing to menu");
        this.getScreen().dispose(); // dispose current screen
        this.setScreen(new MenuScreen(this, gameStateManager, resources, audio, playServices));
    }

    @Override
    public void changeToLevelSelect() {

        Logger.info("LDGame: Changing to level select");
        this.getScreen().dispose(); // dispose current screen
        this.setScreen(new LevelSelectScreen(this, gameStateManager, resources, audio, playServices));
    }

    @Override
    public void changeToLevelLoad(LevelName level) {

        Logger.info("LDGame: Changing to level load: " + level.toString());
        this.getScreen().dispose(); // dispose current screen
        this.setScreen(new LevelLoadingScreen(gameStateManager, this, resources, level));
    }

    @Override
    public void changeToLevel(LevelName level) {

        Logger.info("LDGame: Changing to level: " + level.toString());
        this.getScreen().dispose(); // dispose current screen
        this.setScreen(new GameScreen(level, gameStateManager, this, resources, audio, playServices));
    }

    @Override
    public void stateChange(GameState state) {

        switch (state) {
            case QUIT:
                Gdx.app.exit();
                break;
        }
    }
}
