package com.foxholedefense.levelselect;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.foxholedefense.screen.AbstractScreen;
import com.foxholedefense.screen.ScreenChanger;
import com.foxholedefense.state.GameStateManager;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

/**
 * Screen for Level Select Menu
 *
 * @author Eric
 */
public class LevelSelectScreen extends AbstractScreen {
    private LevelSelectStage stage;
    private ScreenChanger screenChanger;

    public LevelSelectScreen(ScreenChanger screenChanger, GameStateManager gameStateManager, Resources resources, FHDAudio audio) {
        super(gameStateManager);
        this.screenChanger = screenChanger;
        this.stage = new LevelSelectStage(screenChanger, resources, audio, getViewport());
        super.addInputProcessor(stage);
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
