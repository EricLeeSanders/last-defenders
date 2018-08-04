package com.lastdefenders.game;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.ActorGroups;
import com.lastdefenders.game.model.level.state.LevelStateManager;
import com.lastdefenders.game.ui.GameUIStage;
import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.game.ui.state.GameUIStateManager.GameUIState;
import com.lastdefenders.googleplay.GooglePlayServices;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.screen.AbstractScreen;
import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.state.GameStateManager;
import com.lastdefenders.state.GameStateManager.GameState;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * Game Screen that creates the Game Stage and UI Stage as well as their
 * dependencies.
 *
 * @author Eric
 */
public class GameScreen extends AbstractScreen {

    private GameStage gameStage;
    private GameUIStage gameUIStage;
    private GameStateManager gameStateManager;
    private GameUIStateManager uiStateManager;
    private Resources resources;
    private SpriteBatch spriteBatch;
    private ScreenChanger screenChanger;
    private Viewport gameViewport;
    private Viewport uiViewport;

    public GameScreen(LevelName levelName, GameStateManager gameStateManager, ScreenChanger screenChanger,
        Resources resources, LDAudio audio, GooglePlayServices playServices) {

        super(gameStateManager);
        this.resources = resources;
        this.screenChanger = screenChanger;
        this.gameStateManager = gameStateManager;
        spriteBatch = new SpriteBatch();

        createCameraAndViewports();
        createStages(levelName, audio, playServices);

        super.show();
        audio.turnOffMusic();
        gameStage.loadFirstWave();
        createBackListener();
        gameStateManager.setState(GameState.PLAY);
    }

    private void createCameraAndViewports(){

        OrthographicCamera gameCamera = new OrthographicCamera();
        gameViewport = new StretchViewport(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT, gameCamera);
        addViewport(gameViewport);


        Camera uiCamera = new OrthographicCamera();
        uiViewport = new StretchViewport(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT, uiCamera);
        addViewport(uiViewport);
    }

    private void createStages(LevelName levelName, LDAudio audio, GooglePlayServices playServices ) {

        Player player = new Player();
        ActorGroups actorGroups = new ActorGroups();
        LevelStateManager levelStateManager = new LevelStateManager();
        uiStateManager = new GameUIStateManager(levelStateManager);

        gameStage = new GameStage(levelName, player, actorGroups, audio, levelStateManager,
            uiStateManager, gameViewport, resources, spriteBatch, playServices);
        gameUIStage = new GameUIStage(player, actorGroups.getTowerGroup(), uiStateManager,
            levelStateManager, gameStateManager, screenChanger, super.getInputMultiplexer(),
            uiViewport, resources, audio, gameStage, spriteBatch);
    }
    private void createBackListener() {

        InputProcessor backProcessor = new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {

                if ((keycode == Keys.ESCAPE) || (keycode == Keys.BACK)) {
                    Logger.info("GameScreen: Escape/Back pressed.");
                    if(uiStateManager.getState().equals(GameUIState.GAME_OVER)){
                        screenChanger.changeToMenu();
                    } else {
                        uiStateManager.setState(GameUIState.PAUSE_MENU);
                    }
                }
                return false;
            }
        };

        super.addInputProcessor(backProcessor);
    }

    @Override
    public void resize(int width, int height) {
        gameStage.getViewport().setScreenSize(width, height); // update the size of ViewPort
        gameUIStage.getViewport().setScreenSize(width, height); // update the size of ViewPort
        super.resize(width, height);
    }

    /**
     * If the Game State is not play, then "pause" the Game Stage
     */
    @Override
    public void renderElements(float delta) {

        if (gameStateManager.getState().equals(GameState.PLAY)) {
            if (resources.getGameSpeed() > 0) {
                gameStage.act(delta * resources.getGameSpeed());
            }
        }
        gameViewport.apply();
        gameStage.draw();

        uiViewport.apply();
        gameUIStage.act(delta);
        gameUIStage.draw();

    }

    @Override
    public void pause() {

        Logger.info("Game Screen: pausing");
        uiStateManager.setState(GameUIState.PAUSE_MENU);
        gameStateManager.setState(GameState.PAUSE);
    }

    @Override
    public void resume() {

        Logger.info("Game Screen: resume");
        if (!gameStateManager.getState().equals(GameState.PAUSE)) {
            gameStateManager.setState(GameState.PLAY);
        }
    }

    @Override
    public void dispose() {

        Logger.info("Game Screen Dispose");
        gameStage.dispose();
        gameUIStage.dispose();
        spriteBatch.dispose();
    }
}
