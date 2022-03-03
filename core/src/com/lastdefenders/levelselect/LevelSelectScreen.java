package com.lastdefenders.levelselect;

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
import com.lastdefenders.state.GameStateManager;
import com.lastdefenders.sound.LDAudio;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

/**
 * Screen for Level Select Menu
 *
 * @author Eric
 */
public class LevelSelectScreen extends AbstractScreen {

    private LevelSelectStage stage;
    private ScreenChanger screenChanger;

    public LevelSelectScreen(ScreenChanger screenChanger, GameStateManager gameStateManager,
        Resources resources, LDAudio audio, GooglePlayServices playServices) {

        super(gameStateManager);
        this.screenChanger = screenChanger;
        Viewport viewport = new ScalingViewport(Scaling.stretch, Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT,
            new OrthographicCamera());
        this.stage = new LevelSelectStage(screenChanger, resources, audio, viewport, playServices);
        super.addInputProcessor(stage);
        audio.playMenuMusic(); // Play menu music in case the user comes directly to this screen.
        createBackListener();
    }

    private void createBackListener() {

        InputProcessor backProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {

                if ((keycode == Keys.ESCAPE) || (keycode == Keys.BACK)) {
                    Logger.info("LevelSelectScreen: Escape/Back pressed.");
                    screenChanger.changeToMenu();
                }
                return false;
            }
        };
        super.addInputProcessor(backProcessor);
    }

    @Override
    public void resize(int width, int height) {

        stage.getViewport().setScreenSize(width, height); // update the size of Viewport
        super.resize(width, height);
    }

    @Override
    public void show() {

        Logger.info("Level select screen: show");
        super.show();
    }

    @Override
    public void renderElements(float delta) {

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {

        Logger.info("Level Select Screen Dispose");
        super.dispose();
        stage.dispose();
    }
}
