package com.lastdefenders;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.pay.PurchaseManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
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
import com.lastdefenders.sound.AudioHelper;
import com.lastdefenders.sound.AudioManager;
import com.lastdefenders.sound.MusicPlayer;
import com.lastdefenders.sound.SoundPlayer;
import com.lastdefenders.state.GameStateManager;
import com.lastdefenders.state.GameStateManager.GameState;
import com.lastdefenders.state.GameStateObserver;
import com.lastdefenders.store.StoreManager;
import com.lastdefenders.util.ErrorReporter;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.UserPreferences;

public class LDGame extends Game implements ScreenChanger, GameStateObserver {

    private GameStateManager gameStateManager;
    private Resources resources;
    private AudioManager audio;
    private GooglePlayServices playServices;
    private AdControllerHelper adControllerHelper;
    private EventLogger eventLogger;
    private PurchaseManager purchaseManager;
    private StoreManager storeManager;
    private AdController adController;
    private ErrorReporter errorReporter;

    // Needed for launcher without play services
    // TODO remove this
    public LDGame() {

    }

    public LDGame(GooglePlayServices playServices, AdController adController, EventLogger eventLogger,
        PurchaseManager purchaseManager, ErrorReporter errorReporter){

        this.playServices = playServices;
        this.adController = adController;
        this.eventLogger = eventLogger;
        this.purchaseManager = purchaseManager;
        this.errorReporter = errorReporter;
    }

    @Override
    public void create() {

        Logger.info("LDGame: Creating");
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Logger.setErrorReporter(this.errorReporter);
        UserPreferences userPreferences = new UserPreferences();
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        this.resources = new Resources(userPreferences, shapeRenderer);

        AudioHelper audioHelper = new AudioHelper();
        this.audio = new AudioManager(
            new SoundPlayer(userPreferences, audioHelper),
            new MusicPlayer(userPreferences, audioHelper),
            audioHelper,
            userPreferences);
        this.gameStateManager = new GameStateManager();

        this.adControllerHelper = new AdControllerHelper(adController, userPreferences);
        this.storeManager = new StoreManager(purchaseManager, userPreferences);

        Viewport viewport = new ScalingViewport(Scaling.stretch, Resources.VIRTUAL_WIDTH,
            Resources.VIRTUAL_HEIGHT,
            new OrthographicCamera());
        Stage stage = new Stage(viewport);
        GameLoadingScreen loadingScreen = new GameLoadingScreen(gameStateManager, this, resources,
            audio, storeManager, viewport, stage);
        setScreen(loadingScreen);
        gameStateManager.attach(this);
        Gdx.input.setCatchKey(Input.Keys.BACK, false);
    }

    @Override
    public void resume() {

        Logger.info("LDGame: resuming");
        Logger.setErrorReporter(this.errorReporter);
        resources.reload();
        super.resume();
    }

    @Override
    public void dispose() {

        Logger.info("LDGame: disposing");
        resources.dispose();
        audio.disposeAll();
        this.getScreen().dispose();
        purchaseManager.dispose();
        super.dispose();
    }


    @Override
    public void changeToMenu() {

        Logger.info("LDGame: Changing to menu");
        this.getScreen().dispose(); // dispose current screen
        this.setScreen(new MenuScreen(this, gameStateManager, resources, audio, playServices,
            storeManager));
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
            audio, playServices, adControllerHelper, eventLogger, storeManager));
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
