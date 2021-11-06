package com.lastdefenders;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lastdefenders.ads.AdController;
import com.lastdefenders.ads.AdControllerHelper;
import com.lastdefenders.game.GameScreen;
import com.lastdefenders.googleplay.GooglePlayServices;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.levelselect.LevelSelectScreen;
import com.lastdefenders.load.GameLoadingScreen;
import com.lastdefenders.load.LevelLoadingScreen;
import com.lastdefenders.log.EventLogger;
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
    private AdControllerHelper adControllerHelper;
    private EventLogger eventLogger;

    // Needed for launcher without play services
    // TODO remove this
    public LDGame() {

    }

    public LDGame(GooglePlayServices playServices, AdController adController, EventLogger eventLogger){

        this.playServices = playServices;
        this.adControllerHelper = new AdControllerHelper(adController);
        this.eventLogger = eventLogger;
    }

    @Override
    public void create() {

        Logger.info("LDGame: Creating");
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        UserPreferences userPreferences = new UserPreferences();
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        resources = new Resources(userPreferences, shapeRenderer);
        audio = new LDAudio(userPreferences);
        gameStateManager = new GameStateManager();
        GameLoadingScreen loadingScreen = new GameLoadingScreen(gameStateManager, this, resources,
            audio);
        setScreen(loadingScreen);
        gameStateManager.attach(this);
        Gdx.input.setCatchKey(Input.Keys.BACK, false);
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
        this.setScreen(new GameScreen(level, gameStateManager, this, resources,
            audio, playServices, adControllerHelper, eventLogger));
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
