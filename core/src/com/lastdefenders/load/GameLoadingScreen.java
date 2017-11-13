package com.lastdefenders.load;


import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.lastdefenders.screen.AbstractScreen;
import com.lastdefenders.screen.ScreenChanger;
import com.lastdefenders.state.GameStateManager;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;

public class GameLoadingScreen extends AbstractScreen {

    private static final float MIN_LOAD_TIME = 3.50f;
    private Resources resources;
    private LDAudio audio;
    private ScreenChanger screenChanger;
    private Stage stage;
    private float loadTime = 0;

    public GameLoadingScreen(GameStateManager gameStateManager, ScreenChanger screenChanger,
        Resources resources, LDAudio audio) {

        super(gameStateManager);
        this.resources = resources;
        this.screenChanger = screenChanger;
        this.audio = audio;
        this.stage = new Stage(
            new ScalingViewport(Scaling.stretch, Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT,
                new OrthographicCamera()));
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
        float x = ActorUtil
            .calcBotLeftPointFromCenter(Resources.VIRTUAL_WIDTH / 2, image.getWidth());
        float y = ActorUtil
            .calcBotLeftPointFromCenter(Resources.VIRTUAL_HEIGHT / 2, image.getHeight());
        image.setPosition(x, y);
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
        if (resources.getManager().update() && loadTime >= MIN_LOAD_TIME) {
            finishedLoading();
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
