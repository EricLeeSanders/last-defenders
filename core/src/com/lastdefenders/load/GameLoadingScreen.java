package com.lastdefenders.load;


import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.screen.AbstractScreen;
import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.sound.AudioManager;
import com.lastdefenders.sound.MusicPlayer;
import com.lastdefenders.state.GameStateManager;
import com.lastdefenders.store.StoreManager;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

public class GameLoadingScreen extends AbstractScreen {

    private static final float MIN_LOAD_TIME = 2f;
    private static final float MAX_LOAD_TIME = 4f;
    private Resources resources;
    private AudioManager audio;
    private ScreenChanger screenChanger;
    private Stage stage;
    private StoreManager storeManager;
    private float loadTime = 0;

    public GameLoadingScreen(GameStateManager gameStateManager, ScreenChanger screenChanger,
        Resources resources, AudioManager audio, StoreManager storeManager, Viewport viewport, Stage stage) {

        super(gameStateManager);
        this.resources = resources;
        this.screenChanger = screenChanger;
        this.audio = audio;
        this.storeManager = storeManager;
        this.stage = stage;
        super.addViewport(viewport);
        super.addInputProcessor(stage);
        createBackListener();
    }

    private void createBackListener() {

        InputProcessor backProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                // Don't allow return while loading;
                return false;
            }
        };
        super.addInputProcessor(backProcessor);
    }

    @Override
    public void show() {

        Logger.info("Game loading screen: show");
        super.show();

        resources.loadAssetSync(Resources.LOAD_ATLAS, TextureAtlas.class);
        TextureAtlas atlas = resources.getAsset(Resources.LOAD_ATLAS, TextureAtlas.class);
        Image image = new Image(atlas.findRegion("img-loading"));
        image.setSize(230,66);
        image.setPosition(Resources.VIRTUAL_WIDTH / 2, Resources.VIRTUAL_HEIGHT / 2, Align.center);

        loadTime = 0;
        stage.addActor(image);

        load();
    }

    @Override
    public void renderElements(float delta) {

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void render(float delta) {

        loadTime += delta;
        super.render(delta);

        /*
            Need to wait for resources to finish loading. Also have a min load time so the loading
            screen doesn't just flash for the user.
         */
        if (resources.getManager().update() && loadTime >= MIN_LOAD_TIME) {
            /*
                If possible, we want to wait for the store manager to finish installing. However,
                we don't want to force the user to wait.
             */
            if(storeManager.installed() || loadTime >= MAX_LOAD_TIME) {
                finishedLoading();
            }
        }
    }

    @Override
    public void dispose() {

        Logger.info("Game Loading Screen Dispose");
        super.dispose();
        stage.dispose();
    }

    private void load() {

        resources.loadSkin();
        resources.loadAsset(Resources.MENU_ATLAS, TextureAtlas.class);
        resources.loadAsset(Resources.LEVEL_SELECT_ATLAS, TextureAtlas.class);
        audio.load();

    }

    private void finishedLoading() {

        resources.initFont();
        screenChanger.changeToMenu();

    }
}
