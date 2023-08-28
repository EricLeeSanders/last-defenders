package com.lastdefenders.menu;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.googleplay.GooglePlayServices;
import com.lastdefenders.screen.AbstractScreen;
import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.sound.AudioManager;
import com.lastdefenders.sound.AudioPlayer;
import com.lastdefenders.sound.LDMusic;
import com.lastdefenders.sound.MusicPlayer;
import com.lastdefenders.state.GameStateManager;
import com.lastdefenders.state.GameStateManager.GameState;
import com.lastdefenders.store.StoreManager;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * Screen class for the Main Menu. Creates the view and stage
 *
 * @author Eric
 */
public class MenuScreen extends AbstractScreen {

    private MenuStage stage;
    private GameStateManager gameStateManager;
    private Viewport viewport;

    public MenuScreen(ScreenChanger screenChanger, GameStateManager gameStateManager,
        Resources resources, AudioManager audio, GooglePlayServices playServices,
        StoreManager storeManager) {

        super(gameStateManager);
        this.gameStateManager = gameStateManager;
        createStageAndViewport(screenChanger, resources, audio, playServices, storeManager);
        audio.getMusicPlayer().play(LDMusic.Type.MENU);

        createBackListener();
    }

    private void createStageAndViewport(ScreenChanger screenChanger, Resources resources,
        AudioManager audio, GooglePlayServices playServices, StoreManager storeManager){

        viewport = new ScalingViewport(Scaling.stretch, Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT,
            new OrthographicCamera());
        addViewport(viewport);
        stage = new MenuStage(screenChanger, resources, audio, viewport, playServices, storeManager);
        addInputProcessor(stage);
    }

    private void createBackListener() {

        InputProcessor backProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {

                boolean handled = false;
                if ((keycode == Keys.ESCAPE) || (keycode == Keys.BACK)) {
                    Logger.info("MenuScreen: Escape/Back pressed.");
                    handled = stage.handleBack();
                    if(!handled){
                        gameStateManager.setState(GameState.QUIT);
                    }
                }
                return handled;
            }
        };
        super.addInputProcessor(backProcessor);
    }

    @Override
    public void show() {

        Logger.info("Menu Screen: show");
        super.show();

    }

    @Override
    public void renderElements(float delta) {

        stage.act(delta);
        viewport.apply();
        stage.draw();

    }

    @Override
    public void dispose() {

        super.dispose();
        Logger.info("Menu Screen: Dispose");
        stage.dispose();

    }
}
